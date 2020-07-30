#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <fcntl.h>
#include <unistd.h>
#include <sys/wait.h>
#include <sys/resource.h>
#include <sys/ptrace.h>
#include <sys/user.h> 
#include <sys/mman.h>
#include "global.h"
#include "io.h"
#include "utils.h"

char *escapeEmptyTail(char *str) {
    int end = strlen(str) - 1;
    while (end >= 0 && (str[end] == ' ' || str[end] == '\t' || str[end] == '\n')) {
        end--;
    }
    int len = end + 1;
    str[len] = '\0';

    int lastPos = len - 1;
    int i = lastPos;
    for (; i >= 0; ) {
        while (i >= 0 && str[i] != '\n') {
            str[lastPos--] = str[i--];
        }
        if (lastPos >= 0 && i >= 0) {
            str[lastPos--] = str[i--]; //'\n'
        }

        while (i >= 0 && (str[i] == ' ' || str[i] == '\t')) {
            i--;
        }
    }

    return str + lastPos + 1;
}

void checkTestPoints(struct request *req) {
    char inputFileDir[FILE_PATH_LEN] = {0};
    char outputFileDir[FILE_PATH_LEN] = {0};

    //显示的题目ID是数据库的ID加1000
    sprintf(inputFileDir, "%s/%d/input", testPointDir, req->problemID + 1000);
    sprintf(outputFileDir, "%s/%d/output", testPointDir, req->problemID + 1000);

    for (int i = 0; i < req->testPointCount; i++) {
        req->testPointInputFiles[i] = (char *) malloc(sizeof(char) * FILE_PATH_LEN);
        sprintf(req->testPointInputFiles[i], "%s/%d.in", inputFileDir, i + 1);
        char outputFile[FILE_PATH_LEN];
        sprintf(outputFile, "%s/%d.out", outputFileDir, i + 1);

        //printf("%s:%d  %s:%d", req->testPointInputFiles[i], access(req->testPointInputFiles[i], R_OK), outputFile, access(outputFile, R_OK));

        if (access(req->testPointInputFiles[i], R_OK) != 0 || access(outputFile, R_OK) != 0) {
            free(req->testPointInputFiles[i]);
            req->testPointInputFiles[i] = NULL;
        }
    }
}

void prepareToJudge(struct request *req, struct response *resq) {
    req->compileCmd = (char *) malloc(sizeof(char) * FILE_PATH_LEN * 2);
    req->executeCmd = (char *) malloc(sizeof(char) * FILE_PATH_LEN * 2);

    if (strcasecmp(req->language, "c") == 0) {
        sprintf(req->executableFile, "%s/%s", req->submitDir, "Main");
        sprintf(req->compileCmd, "gcc %s -o %s -O2 -Wall -lm --static -std=c99", req->sorcecodePath, req->executableFile);
        sprintf(req->executeCmd, "%s %s", req->executableFile, req->executableFile);
    }

    if (strcasecmp(req->language, "c++") == 0 || strcasecmp(req->language, "cpp") == 0) {
        sprintf(req->executableFile, "%s/%s", req->submitDir, "Main");
        sprintf(req->compileCmd, "g++ %s -o %s -O2 -Wall -lm --static", req->sorcecodePath, req->executableFile);
        sprintf(req->executeCmd, "%s %s", req->executableFile, req->executableFile);
    }

    if (strcasecmp(req->language, "java") == 0) {
        sprintf(req->compileCmd, "javac -J-Xms64m -J-Xmx256m %s", req->sorcecodePath);
        sprintf(req->executeCmd, "java -cp %s Main", req->submitDir);
    }

    if (strcasecmp(req->language, "python2") == 0) {
        sprintf(req->compileCmd, "python2 -m py_compile %s", req->sorcecodePath);
        sprintf(req->executeCmd, "python2 %s", req->sorcecodePath);
    }

    if (strcasecmp(req->language, "python3") == 0) {
        sprintf(req->compileCmd, "python3 -m py_compile %s", req->sorcecodePath);
        sprintf(req->executeCmd, "python3 %s", req->sorcecodePath);
    }
}

//java使用缺页数*页面大小
int getJavaMemUsage(int pid, struct rusage usage) {
    //java use pagefault
    int m_minflt;
    //printf("pagefault: %d, pagesize: %d\n",usage.ru_minflt,  getpagesize());
    m_minflt = usage.ru_minflt * getpagesize() / 1024;
    return m_minflt;
}



int getMemUsage(pid_t pid, struct rusage usage) {
    char status_path[32];
    int fd;
    sprintf(status_path, "/proc/%d/status", pid);
    if ((fd = open(status_path, O_RDONLY)) < 0)
        return 0;

    char buf[BUF_SIZE];
    read(fd, buf, BUF_SIZE);
    close(fd);

    int val = 0;
    char *start = strstr(buf, "VmPeak");
    if (start)
        sscanf(start, "%*s %d", &val);
    return val;
}



int getTimeConsume(struct rusage usage) {
    int timeConsume = 0;//user time + system time
    timeConsume =  (usage.ru_utime.tv_sec + usage.ru_stime.tv_sec) * 1000 + (usage.ru_utime.tv_usec + usage.ru_stime.tv_usec) / 1000;
    return timeConsume;
}



int checkRestrictSyscall(char *syscall_name) {
    for (int i = 0; strlen(restrictedSyscalls[i]) > 0; i++)
        if (strcmp(restrictedSyscalls[i], syscall_name) == 0)
            return 1;
    return 0;
}


struct testPointDetail run(char **cmds, int tLimit, int mLimit, char *userStdinFile, char *userStdoutFile, char *userStderrFile, int (*getMemConsume)(int, struct rusage)) {
    //最大CPU时间限制
    struct rlimit timeLimit;
    /*struct Submit中接收到的时间是毫秒, 在此将毫秒转换为秒*/
    int t_sec = tLimit / 1000;
    t_sec = t_sec > 0 ? t_sec : 1;

    //软限制为规定时间+1s
    timeLimit.rlim_cur = t_sec + 1; 
    // max's value max greater than cur's value, oterwise can't capture SIGXCPU signal
    timeLimit.rlim_max =5 * t_sec; 

    //最大输出, 在配置文件中设置
    struct rlimit outputLimit;
    outputLimit.rlim_cur = outputFileSize;
    outputLimit.rlim_max = 5 * outputFileSize;

    struct testPointDetail detail;

    int status;
    int pid = fork();
    if (pid < 0) {
        perror("fork in run");
        detail.result = SYSTEM_ERROR;
        return detail;
    }

    if (pid == 0) {
        // 用户程序程序
        nice(19);
        //设置CPU时间限制
        if (setrlimit(RLIMIT_CPU, &timeLimit) < 0)
            perror("setrlimit RLIMIT_CPU");

        //设置内存限制
        if (setrlimit(RLIMIT_FSIZE, &outputLimit) < 0)
            perror("setrlimit RLIMIT_FSIZE");

        //重定向 stdin/stdout/stderr
        freopen(userStdinFile, "r", stdin);
        freopen(userStdoutFile, "w", stdout);
        freopen(userStderrFile, "w", stderr);

        alarm(0);
        alarm(t_sec + 1);//set timmer

        //设置跟踪子进程
        
        ptrace(PTRACE_TRACEME, 0, NULL, NULL);
        execvp(cmds[0], (char *const *) cmds);
        exit(1);
    }

    //评测机进程
    int maxMemUsage = -1;
    int resultIndex = RUNNING;

    struct rusage usage;
    struct user_regs_struct regs;

    //与子进程交互，直到子进程退出
    while (1) {
        if (wait4(pid, &status, 0, &usage) < 0)
            perror("wait4");

        //跟踪子程序使用了什么系统调用
        ptrace(PTRACE_GETREGS, pid, NULL, &regs); //if user program is a dead loop, orig_rax may be a negtive number, so need to check here
        if ((int) regs.orig_rax >= 0) {
            //printf("SystemCall id:%d, %s\n", regs.orig_rax, syscalls[(int) regs.orig_rax]);
            //检查系统调用是否在禁用列表
            if (checkRestrictSyscall(syscalls[(int) regs.orig_rax])) {
                //printf("==================user used restrict syscall: %s\n", syscalls[(int)regs.orig_rax]);
                resultIndex = RUNTIME_ERROR;
                ptrace(PTRACE_KILL, 0, NULL, NULL);
                break;
            }
        }

        //获取运行中内存消耗的峰值                             //其他程序冲proc中文件获取
        int memPeak = getMemConsume(pid, usage);
        //更新最大峰值
        maxMemUsage = memPeak > maxMemUsage ? memPeak : maxMemUsage;


        //内存超限
        if (memPeak > mLimit) {//设置内存限制上线
            resultIndex = MEMORY_LIMIT_EXCEEDED;
            ptrace(PTRACE_KILL, 0, NULL, NULL);
            break;
        }


        if (WIFEXITED(status)) //正常退出
            break;

        if (WIFSTOPPED(status))//用户程序暂停执行
        {
            int sig = WSTOPSIG(status);
            //printf("WIFSTOPPED signal:%d\n", sig);
            switch (sig) {
                case SIGALRM:
                    alarm(0);
                case SIGXCPU:
                    resultIndex = TIME_LIMIT_EXCEEDED;
                    ptrace(PTRACE_KILL, pid, NULL, NULL);
                    break;
                case SIGXFSZ:
                    resultIndex = OUTPUT_LIMIT_EXCEEDED;
                    ptrace(PTRACE_KILL, pid, NULL, NULL);
                    break;
                case SIGFPE:
                case SIGSEGV:
                    resultIndex = RUNTIME_ERROR;
                    ptrace(PTRACE_KILL, pid, NULL, NULL);
                    break;
            }
        }


        if (WIFSIGNALED(status))//程序收到信号,异常退出
        {
            int sig = WTERMSIG(status);
            //printf("WIFSIGNALED signal:%d\n", sig);
            switch (sig) {
                case SIGALRM:
                    alarm(0);
                case SIGXCPU:
                    resultIndex = TIME_LIMIT_EXCEEDED;
                    break;
                case SIGXFSZ:
                    resultIndex = OUTPUT_LIMIT_EXCEEDED;
                    break;
                case SIGKILL:
                    break;//kill什么都不做
                default:
                    resultIndex = RUNTIME_ERROR;
            }
            ptrace(PTRACE_KILL, pid, NULL, NULL);
            break;
        }

        ptrace(PTRACE_SYSCALL, pid, NULL, NULL);
    }


    detail.timeConsume = getTimeConsume(usage);;
    detail.memConsume = maxMemUsage;
    detail.result = resultIndex;
    detail.returnVal = WEXITSTATUS(status);

    //检查是否有标准错误的输出, 有则认为runtime error
    if (getFileSize(userStderrFile) > 0) {
        detail.timeConsume = -1;
        detail.memConsume = -1;
        detail.result = RUNTIME_ERROR;
    }
    return detail;
}






int checkAnswer(const char *standardOutput, const char *userOutput) {
    //printf("compare: %s %s\n", standardOutput, userOutput);

    int stdFd = open(standardOutput, O_RDONLY); //打开标准答案
    if (stdFd == -1) {
        perror("checkAnswer: 打开标准答案错误");
        return SYSTEM_ERROR;
    }

    int userFd = open(userOutput, O_RDONLY);//打开用户答案
    if (userFd == -1) {
        perror("checkAnswer: 打开用户答案错误");
        return SYSTEM_ERROR;
    }

    //映射正确答案到内存
    int stdLen = lseek(stdFd, 0, SEEK_END);
    char *standardOutputContent = mmap(NULL, stdLen, PROT_WRITE, MAP_PRIVATE, stdFd, 0);

    //映射用户输出到内存
    int userLen = lseek(userFd, 0, SEEK_END);
    char *userOutputContent = mmap(0, userLen, PROT_WRITE, MAP_PRIVATE, userFd, 0);

    standardOutputContent = escapeEmptyTail(standardOutputContent);
    userOutputContent = escapeEmptyTail(userOutputContent);

    close(userFd);
    close(stdFd);
    //100%内容对比
    if (strcmp(standardOutputContent, userOutputContent) == 0) {
        return ACCEPTED; //完全相同: AC
    } else {
        //#除去空格,tab,换行相同: PE
        standardOutputContent = escapeSpace(standardOutputContent);
        userOutputContent = escapeSpace(userOutputContent);
        if (strcmp(standardOutputContent, userOutputContent) == 0) {
            return PRESENTATION_ERROR;
        }
    }
    //走到如此地步只能WA
    return WRONG_ANSWER;
}


void doJudge(struct request *req, struct response *resp) {
    char userStdoutFile[FILE_PATH_LEN] = {0};
    char userStderrFile[FILE_PATH_LEN] = {0};
    
    sprintf(userStderrFile, "%s/%s", req->submitDir, "error.out");
    sprintf(userStdoutFile, "%s/user.out", req->submitDir);

    typedef int(*memGetter)(int, struct rusage);
    memGetter memGetterPointer = (strcasecmp(req->language, "java") == 0) ? getJavaMemUsage : getMemUsage;
    resp->testDetails = (struct testPointDetail *) malloc(sizeof(struct testPointDetail) * req->testPointCount);
    
    int maxTimeVal = -1;
    int maxMemVal = -1;
    resp->finalResult = ACCEPTED;
    
    //将命令行字符串解析为命令行参数，因为parseCmd调用strtok函数，该函数会破坏executeCmd字符的结构
    //不能放到循环内重复解析。
    char *cmds[128];
    parseCmd(req->executeCmd, cmds); //将字符串命令解析为单个指令的数组

    for (int i = 0; i < req->testPointCount; i++) {
        //puts("before run");
        struct testPointDetail detail = run(cmds, req->timeLimit, req->memLimit, req->testPointInputFiles[i], userStdoutFile, userStderrFile, memGetterPointer);
        //puts("after run");

        detail.testPointID = i + 1;
        //程序在规定时间加1000ms内正常结束, RUNNING状态不会更新
        if (detail.result == RUNNING) {
            //时间内存取最大值
            maxTimeVal = detail.timeConsume > maxTimeVal ? detail.timeConsume : maxTimeVal;
            maxMemVal = detail.memConsume > maxMemVal ? detail.memConsume : maxMemVal;

            if (detail.timeConsume >= req->timeLimit) {
                detail.result = TIME_LIMIT_EXCEEDED; //设置超时
            } else {   //检查答案
                char standardOutput[FILE_PATH_LEN];
                //sprintf(userStdoutFile, "%s/user%d.out", req->submitDir, i + 1);
                sprintf(standardOutput, "%s/%d/output/%d.out", testPointDir, req->problemID + 1000, i + 1);
                detail.result = checkAnswer(standardOutput, userStdoutFile);
            }
        }

        if (detail.result != ACCEPTED) {
            resp->finalResult = detail.result;
        }

        resp->testDetails[i] = detail;
        printf("#%d -> result: %s, returnVal:%d timeConsume:%d, memConsume:%d\n", i + 1, result_list[detail.result], detail.returnVal, detail.timeConsume, detail.memConsume);
    }
    
    resp->memConsume = maxMemVal;
    resp->timeConsume = maxTimeVal;
    //resp->message; 无

}