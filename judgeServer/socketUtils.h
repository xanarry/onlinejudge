
#ifndef SOCKETUTILS_H
#define SOCKETUTILS_H

#include <arpa/inet.h>//include IPPOTO_TCP, struct sockaddr_in,
#include "global.h"


int setupServer(const int port);
int connectTo(char *ip, int port);
void parseRequest(int socket, struct request *req);
int acceptNewConn(int server_socket, struct sockaddr_in * client_addr);



/*
packageLength: 4bit
submitID: 4bit
problemID: 4bit
timeLimit: 4bit
memLimit: 4bit
language: 4bit
codeLength: 4bit
sorcecode: packageLength - headerLength
*/

#endif //SOCKETUTILS_H
