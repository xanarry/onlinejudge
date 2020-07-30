#ifndef UTILS_H
#define UTILS_H

#include "global.h"

void parseCmd(char *cmdline, char *cmds[]);
char *replace(char *str, char ch);
char *escapeSpace(char *str);
char *trimTail(char *string);
long getFileSize(const char *path);
long long currentTimestamp();
void replaceSpecialChar(char *src, char *desc);
void convertRespToJson(struct response *resp, char *json);
void printRequest(struct request *req);
void printResponse(struct response *resp);


#endif