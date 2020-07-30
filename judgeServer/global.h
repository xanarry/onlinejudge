#ifndef GLOBAL_H
#define GLOBAL_H

//使用数值表示语言, 便于比较, 可以任意数值, 只要每个值不同即可
#define LANG_C       20
#define LANG_CPP     21
#define LANG_JAVA    22
#define LANG_PYTHON2 23
#define LANG_PYTHON3 24


#define FILE_PATH_LEN 256
#define BUF_SIZE 8192

//一下常量作为result_list数组的下标
#define QUEUING 0
#define COMPILING 1
#define RUNNING 2
#define ACCEPTED 3
#define PRESENTATION_ERROR 4
#define WRONG_ANSWER 5
#define TIME_LIMIT_EXCEEDED 6
#define MEMORY_LIMIT_EXCEEDED 7
#define OUTPUT_LIMIT_EXCEEDED 8
#define RUNTIME_ERROR 9
#define SYSTEM_ERROR 10
#define COMPILATION_ERROR 11


struct request {
    int socketFd;
    int submitID;
    int problemID;
    int timeLimit;
    int memLimit;
    int codeLength;
    char language[20];
    int testPointCount;
    char *testPointInputFiles[100];

    char sorcecodePath[FILE_PATH_LEN];
    char submitDir[FILE_PATH_LEN];
    char executableFile[FILE_PATH_LEN];
    char *compileCmd;
    char *executeCmd;
};

//{"testPointID":1,"timeConsume":234,"memConsume":3421,result":"accepted"},
struct testPointDetail
{
    int  testPointID;
    int  timeConsume;
    int  memConsume;
    int  returnVal;
    int  result;
};


struct response {
    struct request *req;
    long long judgeTime;
    int compileRetVal;
    char compileResult[BUF_SIZE + 10];
    int timeConsume;
    int memConsume;
    int finalResult;
    char message[256];
    struct testPointDetail *testDetails;
};


//评测状态发生改变时候, 相应如下格式json给web server
/*
{
    "submitID":1,
    "state":"System Error"
}
*/
struct Judge_state
{
    int submitID;
    char state[32];
};



extern const char * result_list[];
extern const char* languages[];
extern const char* language_suffixs[];
extern int outputFileSize;
extern char testPointDir[FILE_PATH_LEN];
extern char restrictedSyscalls[100][25];
extern char *syscalls[];

int getLanguageIndex(char *language);


#endif