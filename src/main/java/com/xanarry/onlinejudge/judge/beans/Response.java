package com.xanarry.onlinejudge.judge.beans;

/*
{
    "submitID":1,
    "judgeTime":1595864815847,
    "compileRetVal":0,
    "compileResult":"",
    "timeConsume":21,
    "memConsume":35232,
    "finalResult":"Accepted",
    "message":"",
    "judgeDetail":[
        {"testPointID":1, "timeConsume":20, "memConsume":35232, "returnVal":0, "result":"Accepted"},
        {"testPointID":2, "timeConsume":16, "memConsume":35232, "returnVal":0, "result":"Accepted"}
    ]
}
*/


import com.xanarry.onlinejudge.model.JudgeDetailBean;

import java.util.ArrayList;

public class Response {
    private int submitID;
    private long judgeTime;
    private int compileRetVal;
    private String compileResult;
    private int timeConsume;
    private int memConsume;
    private String finalResult;
    private String message;

    public ArrayList<JudgeDetailBean> getJudgeDetails() {
        return judgeDetails;
    }

    public void setJudgeDetails(ArrayList<JudgeDetailBean> judgeDetails) {
        this.judgeDetails = judgeDetails;
    }

    private ArrayList<JudgeDetailBean> judgeDetails;

    public int getSubmitID() {
        return submitID;
    }

    public void setSubmitID(int submitID) {
        this.submitID = submitID;
    }

    public long getJudgeTime() {
        return judgeTime;
    }

    public void setJudgeTime(long judgeTime) {
        this.judgeTime = judgeTime;
    }

    public int getCompileRetVal() {
        return compileRetVal;
    }

    public void setCompileRetVal(int compileRetVal) {
        this.compileRetVal = compileRetVal;
    }

    public String getCompileResult() {
        return compileResult;
    }

    public void setCompileResult(String compileResult) {
        this.compileResult = compileResult;
    }

    public int getTimeConsume() {
        return timeConsume;
    }

    public void setTimeConsume(int timeConsume) {
        this.timeConsume = timeConsume;
    }

    public int getMemConsume() {
        return memConsume;
    }

    public void setMemConsume(int memConsume) {
        this.memConsume = memConsume;
    }

    public String getFinalResult() {
        return finalResult;
    }

    public void setFinalResult(String finalResult) {
        this.finalResult = finalResult;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    @Override
    public String toString() {
        return "Response:" +
                "\nsubmitID=" + submitID +
                "\njudgeTime=" + judgeTime +
                "\ncompileRetVal=" + compileRetVal +
                "\ncompileResult=\n'" + compileResult + '\'' +
                "\ntimeConsume=" + timeConsume +
                "\nmemConsume=" + memConsume +
                "\nfinalResult=" + finalResult +
                "\nmessage=" + message +
                "\njudgeDetails=\n  " + String.join("\n  ", judgeDetails.stream().map(Object::toString).toArray(String[]::new));
    }
}
