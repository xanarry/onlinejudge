#include "io.h"
#include "global.h"
#include <stdio.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <string.h>

void write_n_byte(int fd, char *buf, const ssize_t n) {
    ssize_t has_write = 0;
    while (has_write < n) {
        ssize_t len = write(fd, buf, n - has_write);
        if (len <= 0) {
            break;
        }
        has_write += len;
    }
}

void read_n_byte_to_file(int fd_from, int fd_to, const ssize_t n) {
    ssize_t has_read = 0;
    char buf[BUF_SIZE];
    while (has_read < n) {
        ssize_t len = read(fd_from, buf, n - has_read);
        if (len <= 0) {
            break;
        }
        //正常打开了文件才写入，否则只读取网络中的数据
        if (fd_to > 2) { 
            write_n_byte(fd_to, buf, len);
        }
        has_read += len;
    }
}

void read_n_byte_to_buf(int fd, char *buf, ssize_t n) {
    ssize_t has_read = 0;
    while (has_read < n) {
        ssize_t len = read(fd, buf + has_read, n - has_read);
        if (len <= 0) {
            break;
        }
        has_read += len;
    }
}


int createSorcecodeFile(struct request *req, char *tmpdir) {
    //创建提交目录
    sprintf(req->submitDir, "%s/%d", tmpdir, req->submitID);

    //如果父目录存在，子目录不存在，创建子目录
    if (access(tmpdir, F_OK) == 0 && access(req->submitDir, F_OK) != 0) {
        if (mkdir(req->submitDir, 0777) < 0) {
            perror("createSorcecodeFile 创建提交目录失败");
            return -1;
        }
    }

    ////代码的保存路径为“系统临时目录/提交ID/代码文件名”
    int langIndex = getLanguageIndex(req->language);
    if (langIndex != -1) {
        sprintf(req->sorcecodePath, "%s/%d/Main%s", tmpdir, req->submitID, language_suffixs[langIndex]);
    }
    

    int code_fd = open(req->sorcecodePath, O_CREAT|O_WRONLY|O_TRUNC, S_IRUSR|S_IWUSR|S_IRGRP|S_IWGRP|S_IROTH|S_IWOTH);
    //如果文件创建失败，read_n_byte_to_file不会写入内容
    read_n_byte_to_file(req->socketFd, code_fd, req->codeLength);
    if (code_fd < 0) {
        perror("createSorcecodeFile: 保存用户源代码失败");
        puts(req->sorcecodePath);
        req->sorcecodePath[0] = '\0';
        return -1;
    }

    return 0;
}