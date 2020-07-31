#include <sys/wait.h>
#include <unistd.h>
#include <stdlib.h>
#include <fcntl.h>
#include <stdio.h>
#include <string.h>
#include <dirent.h>
#include "global.h"
#include "compile.h"
#include "utils.h"
#include "io.h"

/*
return value:
    -1 denote: system error
    0  denote: compile successful
    other denote: failed to compile
*/
int compile(struct request *req, struct response *resp) {
    char *cmds[128];
    parseCmd(req->compileCmd, cmds); //将字符串命令解析为单个指令的数组

    int fd[2]; //两个文件描述符
    int ret = pipe(fd); //创建管道
    if (ret < 0) {
        perror("pipe()");
        return -1;
    }

    int pid = fork();
    if (pid < 0) {
        perror("fork in compile()");
        return -1;
    } else if (pid == 0) { //创建编译进程
        close(fd[0]); //关闭读
        if (dup2(fd[1], 2) < 0) { //将标准错误重定向到父进程的标准输入
            perror("dup2");
            return -1;
        }
        //chdir(req->submitDir); 切换到代码所在目录

        //创建子进程编程代码
        execvp(cmds[0], (char * const *) cmds);
        exit(0);
    }
    close(fd[1]); //关闭父进程写

    int status;
    if (waitpid(pid, &status, 0) < 0) {
        perror("doCompile waitpid");
        return -1;
    }

    //初始化缓冲区
    resp->compileResult[0] = '\0';
    //从管道读取编译错误信息，只读取开始的前8k显示给用户，因为构造相应json字符串的
    //上限是8192*2，保存过长的编译输出信息会导致构造是数组越界。
    read_n_byte_to_buf(fd[0], resp->compileResult, BUF_SIZE);
    //超过8k的数据丢弃
    char discard[BUF_SIZE] = {0};
    while (read(fd[0], discard, BUF_SIZE) > 0);
    trimTail(resp->compileResult); //删除尾部空白


    if (WIFEXITED(status)) { //进程正常退出
        resp->compileRetVal = WEXITSTATUS(status);
        //获取子进程的返回值, 如果通过编译返回0, 否则返回大于0
        if (resp->compileRetVal != 0) {
            resp->finalResult = COMPILATION_ERROR;
        } 
        return resp->compileRetVal;
    }
    return -1;
}