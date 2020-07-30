package com.xanarry.onlinejudge.model;

/**
 * Created by xanarry on 17-12-30.
 */
public class JudgeDetailBean {
    private int submitID;
    private int testPointID;
    private int timeConsume;
    private int memConsume;
    private int returnVal;
    private String result;

    public JudgeDetailBean() {
    }

    public JudgeDetailBean(int submitID, int testPointID, int timeConsume, int memConsume, int returnVal, String result) {
        this.submitID = submitID;
        this.testPointID = testPointID;
        this.timeConsume = timeConsume;
        this.memConsume = memConsume;
        this.returnVal = returnVal;
        this.result = result;
    }

    public int getSubmitID() {
        return submitID;
    }

    public void setSubmitID(int submitID) {
        this.submitID = submitID;
    }

    public int getTestPointID() {
        return testPointID;
    }

    public void setTestPointID(int testPointID) {
        this.testPointID = testPointID;
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

    public int getReturnVal() {
        return returnVal;
    }

    public void setReturnVal(int returnVal) {
        this.returnVal = returnVal;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "JudgeDetailBean{" +
                "submitID=" + submitID +
                ", testPointID=" + testPointID +
                ", timeConsume=" + timeConsume +
                ", memConsume=" + memConsume +
                ", returnVal=" + returnVal +
                ", result='" + result + '\'' +
                '}';
    }
}
