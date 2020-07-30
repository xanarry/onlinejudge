#include "utils.h"
#include <string.h>
#include <stdio.h>
#include <sys/stat.h>
#include <ctype.h>
#include <sys/time.h>

void parseCmd(char *cmdline, char *cmds[]) {
    char *pch = strtok(cmdline,"   ");
    int i = 0;
    while (pch != NULL) {
        //printf ("%s\n",pch);
        cmds[i++] = pch;
        pch = strtok (NULL, "   ");
    }
    cmds[i] = NULL;
}


char *replace(char *str, char ch) {
    int lastPos = 0;
    for (int i = 0; i < strlen(str); i++) {
        if (str[i] != ch) {
            str[lastPos++] = str[i];
        }
    }
    str[lastPos] = '\0';
    return str;
}


char *escapeSpace(char *str) {
    int lastPos = 0;
    for (int i = 0; i < strlen(str); i++) {
        if (str[i] != ' ' && str[i] != '\t' && str[i] != '\n') {
            str[lastPos++] = str[i];
        }
    }
    str[lastPos] = '\0';
    return str;
}

long long currentTimestamp() {
    struct timeval te;
    gettimeofday(&te, NULL); // get current time
    long long milliseconds = te.tv_sec * 1000LL + te.tv_usec / 1000; // calculate milliseconds
    return milliseconds;
}

void replaceSpecialChar(char *src, char *desc) {
    int len = strlen(src), i = 0, j = 0;
    for (; i < len; i++) {
        if (src[i] == '\"') { /* " -> \" */
            desc[j++] = '\\';
            desc[j++] = '\"';
        }
        else if (src[i] == '\n') { /* \n -> \\n */
            desc[j++] = '\\';
            desc[j++] = 'n';
        }
        else if (src[i] == '\r') { /* \r -> \\r */
            desc[j++] = '\\';
            desc[j++] = 'r';
        }
        else if (src[i] == '\\') { /* \ -> \\  */
            desc[j++] = '\\';
            desc[j++] = '\\';
        }
        else {
            desc[j++] = src[i];
        }
    }
    desc[j] = '\0';
}

char *trimTail(char *string) {
    int j;
    int str_len = strlen(string);
    /*删除行末空格与换行*/
    for (j = str_len - 1; j >= 0 && (isspace(string[j]) || string[j] == '\n'); j--);
    string[j + 1] = '\0';
    return string;
}


long getFileSize(const char *path) {
    struct stat statbuff;
    if(stat(path, &statbuff) < 0) {
        return -1;
     } else {
        return statbuff.st_size;
     }
}


/*
响应给web服务器的评测结果
{
    "submitID":1,
    "judgeTime":1595864815847,
    "compileRetVal":0,
    "compileResult":"",
    "timeConsume":21,
    "memConsume":35232,
    "finalResult":"Accepted",
    "message":"",
    "judgeDetails":[
        {"testPointID":1, "timeConsume":20, "memConsume":35232, "returnVal":0, "result":"Accepted"},
        {"testPointID":2, "timeConsume":16, "memConsume":35232, "returnVal":0, "result":"Accepted"}
    ]
}
*/
void convertRespToJson(struct response *resp, char *json) {
    const char *resultPattern =    "{\"testPointID\":%d, \"timeConsume\":%d, \"memConsume\":%d, \"returnVal\":%d, \"result\":\"%s\"}";
    char results[4096] = {0};
    if (resp->compileRetVal == 0) {
        for (int i = 0; i < resp->req->testPointCount; i++) {
            char buf[128] = "";
            sprintf(buf, resultPattern, resp->testDetails[i].testPointID, resp->testDetails[i].timeConsume, resp->testDetails[i].memConsume, resp->testDetails[i].returnVal, result_list[resp->testDetails[i].result]);
            strcat(results, buf);
            if (i < resp->req->testPointCount - 1) {
                strcat(results, ","); 
            }
        }
    }
    char formatedCompileResult[BUF_SIZE + BUF_SIZE / 2];
    replaceSpecialChar(resp->compileResult, formatedCompileResult);
    //puts(formatedCompileResult);
    const char *jsonPattern = "{\"submitID\": %d, \"judgeTime\":%lld, \"compileRetVal\":%d, \"compileResult\":\"%s\", \"timeConsume\":%d, \"memConsume\":%d, \"finalResult\":\"%s\", \"message\":\"%s\", \"judgeDetails\": [%s]}";
    sprintf(json, jsonPattern, resp->req->submitID, resp->judgeTime, resp->compileRetVal, formatedCompileResult, resp->timeConsume, resp->memConsume, result_list[resp->finalResult], resp->message, results);
}



void printRequest(struct request *req) {
    printf("submitID: %d\n", req->submitID);
    printf("problemID: %d\n", req->problemID);
    printf("timeLimit: %d\n", req->timeLimit);
    printf("memLimit: %d\n", req->memLimit);
    printf("language: %s\n", req->language);
    printf("codeLength: %d\n", req->codeLength);
    printf("submitDir: %s\n", req->submitDir);
    printf("sorcecodePath: %s\n", req->sorcecodePath);
    printf("compileCmd: %s\n", req->compileCmd);
    printf("executeCmd: %s\n", req->executeCmd);
    printf("codeLength: %d\n", req->testPointCount);
    puts("test points input file:");
    for (int i = 0; i < req->testPointCount; i++) {
        printf(" %d:%s\n", i + 1, req->testPointInputFiles[i]);
    }
}

/*
struct request *req;
int compileRetVal;
char *compileResult;
int timeConsume;
int memConsume;
int finalResult;
char *message;
struct testPointDetail *testDetails;
*/
void printResponse(struct response *resp) {
    puts("-----------------------------------------");
    printf("submitID: %d\n", resp->req->submitID);
    printf("judgeTime: %lld\n", resp->judgeTime);
    printf("compileRetVal: %d\n", resp->compileRetVal);
    printf("compileResult: \n%s\n", resp->compileResult);
    printf("finalResult: %s\n", result_list[resp->finalResult]);

    printf("timeConsume: %d\n", resp->timeConsume);
    printf("memConsume: %d\n", resp->memConsume);
    printf("message: %s\n", resp->message);

    /*
    int  timeConsume;
    int  memConsume;
    int  returnVal;
    int  result;
    */
    if (resp->compileRetVal == 0) { //编译OK才能运行
        puts("test details:");
        for (int i = 0; i < resp->req->testPointCount; i++) {
            printf("result: %s, returnVal:%d timeConsume:%d, memConsume:%d\n", result_list[resp->testDetails[i].result], resp->testDetails[i].returnVal, 
            resp->testDetails[i].timeConsume, resp->testDetails[i].memConsume);
        }
    }
    puts("-----------------------------------------");
}
