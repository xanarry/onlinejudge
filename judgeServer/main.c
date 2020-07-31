#include <stdio.h>
#include <string.h>
#include <ctype.h>
#include <stdlib.h>
#include <unistd.h>
#include <signal.h>
#include <sys/stat.h>
#include "compile.h"
#include "judge.h"
#include "global.h"
#include "socketUtils.h"
#include "utils.h"
#include "io.h"

//默认配置数据
int debug = 1;
int port = 8888;
int outputFileSize = 8192;
char testPointDir[FILE_PATH_LEN] = "./testpoints/";
char restrictedSyscalls[100][25] = {0};

//标志程序是否需要继续运行
static volatile int keepRunning = 1;
//标志程序是否正处于网络阻塞等待中
static volatile int waitingForConnection = 1;

/*
捕获ctrl+c中断程序
函数先将keepRunning设置为0，如果程序刚评测完一个用户程序，检查到这个标志变量为0
后就会直接清理文件退出。但是设置标志变量后，程序可能处于阻塞状态，没法感知到这个
修改，这个时候发送一个连接请求，并发送几个字节破坏阻塞状态。
*/
void intHandler(int dummy) {
    puts("捕获ctrl c命令, 退出程序");
    keepRunning = 0;
    if (waitingForConnection == 1) {
        int fd = connectTo("127.0.0.1", port);
        if (fd < 0) {
            exit(0);
        }
        char *flag = "EXIT";
        write_n_byte(fd, flag, 4);
        shutdown(fd, SHUT_RDWR);
    }
}


/*
将结果发送到客户端
*/
void writeResponse(int fd, char *content) {
    int hlen = strlen(content); //字符串长度(主机序)
    int nlen = htonl(hlen); //转为网络序
    write_n_byte(fd, (char *) &nlen, 4);//发送数据长度
    write_n_byte(fd, content, hlen);//发送数据本身
    shutdown(fd, SHUT_WR);//关闭写
}


/*
从程序所在目录的oj.conf文件中加载配置数据
*/
void loadConfiguration() {
    FILE *fp = fopen("./oj.conf", "r");
    if (fp == NULL) {
        puts("加载配置文件失败，使用默认配置");
        return;
    }
    const int LINE_LEN = 512;
    char line[LINE_LEN];

    while (fgets(line, LINE_LEN, fp) != NULL) {
        int i = 0;
        while (isspace(line[i])) {
            i++;
        }
        char key[64];
        if (strlen(line) > 0 && line[0] != '\n' && line[i] != '#') {
            char *start = NULL;
            if ((start = strstr(line, "debug")) != NULL) {
                sscanf(start, "%[0-9a-zA-Z\t ]=%d", key, &debug);
            }

            if ((start = strstr(line, "port")) != NULL) {
                sscanf(start, "%[0-9a-zA-Z\t ]=%d", key, &port);
            }

            if ((start = strstr(line, "outputFileSize")) != NULL) {
                sscanf(start, "%[0-9a-zA-Z\t ]=%d", key, &outputFileSize);
            }

            if ((start = strstr(line, "testPointDir")) != NULL) {
                sscanf(start, "%[0-9a-zA-Z\t ]=%s", key, testPointDir);
            }

            if ((start = strstr(line, "restrictedSyscalls")) != NULL) {
                //read debug
                char syscalls[LINE_LEN];
                sscanf(start, "%[0-9a-zA-Z\t ]=%[^\n]", key, syscalls);
                //read restrict syscalls
                int cnt = 0;
                char *pch;
                pch = strtok(syscalls, " ,");
                while (pch != NULL) {
                    strcpy(restrictedSyscalls[cnt++], pch);
                    pch = strtok(NULL, " ,");
                }
                strcpy(restrictedSyscalls[cnt], "");
            }
        }
    }
}


void showConfig() {
    printf("         debug: %d\n", debug);
    printf("          port: %d\n", port);
    printf("outputFileSize: %d\n", outputFileSize);
    printf("  testPointDir: %s\n", testPointDir);
    printf("  rst_syscalls: ");
    for (int i = 0; strlen(restrictedSyscalls[i]) > 0; i++) {
        printf("%s ", restrictedSyscalls[i]);
    }
    puts("");
}



int main(int argc , char *argv[]) {
    //捕获ctrl_c命令
    signal(SIGINT, intHandler);

    //加载配置信息
    puts("加载配置信息");
    loadConfiguration();
    showConfig();

    //创建临时目录，保存运行过程中产生的数据
    char tempdir[] = "./tmp";
    if (access(tempdir, 0) == -1) {
        if (mkdir(tempdir, 0777) < 0) {
            perror ("tempdir: 创建临时运行目录失败");
            exit (EXIT_FAILURE);
        }
    }
    printf("临时运行目录: %s\n", tempdir);


    int serverSocket = setupServer(port);
    if (serverSocket < 0) {
        perror("setup_server: failed to setup server\n");
        return 0;
    }
    printf("程序正在监听端口%d\n", port);

    //程序持续运行，直到keepRunning被修改为0
    while (keepRunning) {
        struct sockaddr_in clientAddr;
        waitingForConnection = 1; //设置阻塞标志
        int fd = acceptNewConn(serverSocket, &clientAddr);
        waitingForConnection = 0; //释放阻塞标志

        //如果已经ctrl+c
        if (keepRunning == 0) {
            char nonSense[BUF_SIZE] = {0};
            //将输入数据读完，然后直接退出循环
            while(read(fd, nonSense, BUF_SIZE) > 0);
            shutdown(fd, SHUT_RDWR);//关闭套接字
            break;
        }

        //构造请求与相应对象
        struct request *req = (struct request *) malloc (sizeof(struct request));
        struct response *resp = (struct response *) malloc (sizeof(struct response));
        resp->req = req;
        resp->judgeTime = currentTimestamp();

        //解析请求头部信息
        parseRequest(fd, req);

        //创建相关文件夹和文件
        if (createSorcecodeFile(req, tempdir) != 0) {
            perror("createSorcecodeFile 创建运行目录和文件失败");
            continue;
        }

        //构造编译，运行命令
        prepareToJudge(req, resp);
        //检查测试数据
        checkTestPoints(req);

        puts("\n新的评测请求");
        printRequest(req);


        //编译代码
        char runningDir[256] = {0};
        sprintf(runningDir, "%s/%d", tempdir, req->submitID);
        int retVal = compile(req, resp);
        printf("compile ret: %d\n", retVal);
        if (retVal == 0) {
            puts("\n开始评测");
            doJudge(req, resp);
        }

        puts("\n评测结果");
        printResponse(resp);

        char json[2 * BUF_SIZE];
        convertRespToJson(resp, json);

        puts("\n响应结果json");
        writeResponse(fd, json);
        puts(json);
    }

    //关闭服务套接字
    close(serverSocket);
    //删除目录和文件
    char clearCmd[128];
    sprintf(clearCmd, "rm -rf %s", tempdir);
    system(clearCmd);
    return 0;
}