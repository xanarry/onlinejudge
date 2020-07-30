#include <sys/socket.h>
#include <string.h>
#include <stdio.h>
#include <unistd.h>
#include "global.h"
#include "socketUtils.h"
#include "io.h"


int setupServer(const int port) {
    /*  创建服务端套接字,
        AF_INET表示使用ipv4类型的地址,
        SOCK_STREAM 表示使用面向连接的数据传输方式
        IPPROTO_TCP 表示使用 TCP 协议
    */
    int server_socket = socket(AF_INET, SOCK_STREAM, IPPROTO_TCP);

    //设置端口复用, 使得从新打开程序后能立即使用端口
    int mw_optval = 1;
    setsockopt(server_socket, SOL_SOCKET, SO_REUSEADDR, (char *) &mw_optval, sizeof(mw_optval));

    /*构建服务端的地址, IP地址和端口都保存在 sockaddr_in 结构体中*/
    struct sockaddr_in server_addr;
    memset(&server_addr, 0, sizeof(server_addr));//空位的内存用0补充
    server_addr.sin_family = AF_INET;//设置地址类型为ipv4
    server_addr.sin_addr.s_addr = INADDR_ANY;//设置ip
    server_addr.sin_port = htons(port);//设置端口


    //bind() 函数将套接字 serv_sock 与特定的IP地址和端口绑定, 此后通过ip+port的连接相当与直接此套接字
    if (bind(server_socket, (struct sockaddr *) &server_addr, sizeof(server_addr)) < 0) {
        printf("failed to bind port %d", port);
        return -1;
    }
    //让套接字处于被动监听状态。指套接字一直处于“睡眠”中，直到客户端发起请求才会被“唤醒”。
    listen(server_socket, 20);
    return server_socket;
}

int acceptNewConn(int server_socket, struct sockaddr_in *client_addr) {
    socklen_t client_addr_size = sizeof(*client_addr);
    return accept(server_socket, (struct sockaddr *) client_addr, (socklen_t*) &client_addr_size);
}


int connectTo(char *ip, int port) {
	struct sockaddr_in server;
	//Create socket
	int socketFd = socket(AF_INET , SOCK_STREAM , 0);
	if (socketFd == -1) {
		perror("connectTo: Could not create socket");
        return -1;
	}
		
	server.sin_addr.s_addr = inet_addr(ip);
	server.sin_family = AF_INET;
	server.sin_port = htons(port);

	//Connect to remote server
	if (connect(socketFd , (struct sockaddr *)&server , sizeof(server)) < 0) {
        perror("connect: error");
        return -1;
	}

    return socketFd;
}



void getFileDir(char *filePath, char *fileDir) {
    strcpy(fileDir, "./");
    int lastPost = strlen(filePath) - 1;
    while (lastPost >= 0 && filePath[lastPost] != '/') {
        lastPost--;
    }

    if (lastPost <= 0) {
        return;
    }

    for (int i = 0; i <= lastPost; i++) {
        fileDir[i] = filePath[i];
    }
    fileDir[lastPost + 1] = '\0';
}


/*
submitID: 4bit
problemID: 4bit
timeLimit: 4bit
memLimit: 4bit
language: 20bit
codeLength: 4bit
sorcecode: packageLength - headerLength
sorceCodePath: 文件路径
*/
void parseRequest(int socket, struct request *req) {
    memset(req->language, 0, 20);
    memset(req->sorcecodePath, 0, sizeof(char) * FILE_PATH_LEN);
    //保存socket文件描述符
    req->socketFd = socket;

    read_n_byte_to_buf(socket, (char *) &(req->submitID), 4);
    req->submitID = ntohl(req->submitID);

    read_n_byte_to_buf(socket, (char *) &(req->problemID), 4);
    req->problemID = ntohl(req->problemID);

    read_n_byte_to_buf(socket, (char *) &(req->timeLimit), 4);
    req->timeLimit = ntohl(req->timeLimit);

    read_n_byte_to_buf(socket, (char *) &(req->memLimit), 4);
    req->memLimit = ntohl(req->memLimit);

    read_n_byte_to_buf(socket, (char *) &(req->codeLength), 4);
    req->codeLength = ntohl(req->codeLength);

    read_n_byte_to_buf(socket, (char *) &(req->testPointCount), 4);
    req->testPointCount = ntohl(req->testPointCount);

    read_n_byte_to_buf(socket, (char *) &(req->language), 20);
}


