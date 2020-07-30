#ifndef JUDGE_H
#define JUDGE_H

void showJudgeResult(struct response *resp);
void doJudge(struct request *req, struct response *resp);
int checkTestPoints(struct request *req);
void prepareToJudge(struct request *req, struct response *resq);

#endif