package com.xanarry.onlinejudge.model;

/**
 * Created by xanarry on 17-12-30.
 */
public class SubmitRecordBean {
    private int submitID;

    private int userID;

    private int problemID;

    private int contestID;

    private String result;

    private String language;

    private int codeLength;

    private int timeConsume;

    private int memConsume;

    private Long submitTime;

    private Long judgeTime;

    private String sourceCode;

    public SubmitRecordBean() {
    }

    public SubmitRecordBean(int submitID, int userID, int problemID, int contestID, String result, String language, int codeLength, int timeConsume, int memConsume, Long submitTime, Long judgeTime, String sourceCode) {
        this.submitID = submitID;
        this.userID = userID;
        this.problemID = problemID;
        this.contestID = contestID;
        this.result = result;
        this.language = language;
        this.codeLength = codeLength;
        this.timeConsume = timeConsume;
        this.memConsume = memConsume;
        this.submitTime = submitTime;
        this.judgeTime = judgeTime;
        this.sourceCode = sourceCode;
    }


    public int getSubmitID() {
        return submitID;
    }

    public void setSubmitID(int submitID) {
        this.submitID = submitID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getProblemID() {
        return problemID;
    }

    public void setProblemID(int problemID) {
        this.problemID = problemID;
    }

    public int getContestID() {
        return contestID;
    }

    public void setContestID(int contestID) {
        this.contestID = contestID;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public int getCodeLength() {
        return codeLength;
    }

    public void setCodeLength(int codeLength) {
        this.codeLength = codeLength;
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

    @Override
    public String toString() {
        return "SubmitRecordBean{" +
                "submitID=" + submitID +
                ", userID=" + userID +
                ", problemID=" + problemID +
                ", contestID=" + contestID +
                ", result='" + result + '\'' +
                ", language='" + language + '\'' +
                ", codeLength=" + codeLength +
                ", timeConsume=" + timeConsume +
                ", memConsume=" + memConsume +
                ", submitTime=" + submitTime +
                ", judgeTime=" + judgeTime +
                ", sourceCode='" + sourceCode + '\'' +
                '}';
    }

    public Long getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(Long submitTime) {
        this.submitTime = submitTime;
    }

    public Long getJudgeTime() {
        return judgeTime;
    }

    public void setJudgeTime(Long judgeTime) {
        this.judgeTime = judgeTime;
    }

    public String getSourceCode() {
        return sourceCode;
    }

    public void setSourceCode(String sourceCode) {
        this.sourceCode = sourceCode;
    }
}
