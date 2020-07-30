package com.xanarry.onlinejudge.judge.beans;


public class Request {
    private int submitID;
    private int problemID;
    private int timeLimit;
    private int memLimit;
    private int codeLength;
    private String language;
    private int testpointCount;
    private String sourcecode;


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

    public int getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(int timeLimit) {
        this.timeLimit = timeLimit;
    }

    public int getMemLimit() {
        return memLimit;
    }

    public void setMemLimit(int memLimit) {
        this.memLimit = memLimit;
    }

    public int getCodeLength() {
        return codeLength;
    }

    public void setCodeLength(int codeLength) {
        this.codeLength = codeLength;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public int getTestpointCount() {
        return testpointCount;
    }

    public void setTestpointCount(int testpointCount) {
        this.testpointCount = testpointCount;
    }

    public String getSourcecode() {
        return sourcecode;
    }

    public void setSourcecode(String sourcecode) {
        this.sourcecode = sourcecode;
    }

    @Override
    public String toString() {
        return "Request{" +
                "submitID=" + submitID +
                ", problemID=" + problemID +
                ", timeLimit=" + timeLimit +
                ", memLimit=" + memLimit +
                ", codeLength=" + codeLength +
                ", language='" + language + '\'' +
                ", testpointCount='" + testpointCount + '\'' +
                ", sourcecode='" + sourcecode + '\'' +
                '}';
    }
}
