package com.xanarry.onlinejudge.model;

public class ViewSubmitRecordBean {
    private int submitID;
    private int problemID;
    private String contestTitle;
    private String problemTitle;
    private int userID;
    private String userName;
    private int contestID;
    private String result;
    private String language;
    private String sourceCode;
    private int codeLength;
    private int timeConsume;
    private int memConsume;
    private long submitTime;
    private long judgeTime;
    private String compileResult;

    public ViewSubmitRecordBean() {
    }

    public ViewSubmitRecordBean(int submitID, int problemID, String contestTitle, String problemTitle, int userID, String userName, int contestID, String result, String language, String sourceCode, int codeLength, int timeConsume, int memConsume, long submitTime, long judgeTime, String compileResult) {
        this.submitID = submitID;
        this.problemID = problemID;
        this.contestTitle = contestTitle;
        this.problemTitle = problemTitle;
        this.userID = userID;
        this.userName = userName;
        this.contestID = contestID;
        this.result = result;
        this.language = language;
        this.sourceCode = sourceCode;
        this.codeLength = codeLength;
        this.timeConsume = timeConsume;
        this.memConsume = memConsume;
        this.submitTime = submitTime;
        this.judgeTime = judgeTime;
        this.compileResult = compileResult;
    }

    public int getSubmitID() {
        return submitID;
    }

    public void setSubmitID(int submitID) {
        this.submitID = submitID;
    }

    public int getProblemID() {
        return problemID;
    }

    public void setProblemID(int problemID) {
        this.problemID = problemID;
    }

    public String getContestTitle() {
        return contestTitle;
    }

    public void setContestTitle(String contestTitle) {
        this.contestTitle = contestTitle;
    }

    public String getProblemTitle() {
        return problemTitle;
    }

    public void setProblemTitle(String problemTitle) {
        this.problemTitle = problemTitle;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public String getSourceCode() {
        return sourceCode;
    }

    public void setSourceCode(String sourceCode) {
        this.sourceCode = sourceCode;
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

    public long getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(long submitTime) {
        this.submitTime = submitTime;
    }

    public long getJudgeTime() {
        return judgeTime;
    }

    public void setJudgeTime(long judgeTime) {
        this.judgeTime = judgeTime;
    }

    public String getCompileResult() {
        return compileResult;
    }

    public void setCompileResult(String compileResult) {
        this.compileResult = compileResult;
    }

    @Override
    public String toString() {
        return "ViewSubmitRecordBean{" +
                "submitID=" + submitID +
                ", problemID=" + problemID +
                ", contestTitle='" + contestTitle + '\'' +
                ", problemTitle='" + problemTitle + '\'' +
                ", userID=" + userID +
                ", userName='" + userName + '\'' +
                ", contestID=" + contestID +
                ", result='" + result + '\'' +
                ", language='" + language + '\'' +
                ", sourceCode='" + sourceCode + '\'' +
                ", codeLength=" + codeLength +
                ", timeConsume=" + timeConsume +
                ", memConsume=" + memConsume +
                ", submitTime=" + submitTime +
                ", judgeTime=" + judgeTime +
                ", compileResult='" + compileResult + '\'' +
                '}';
    }
}
