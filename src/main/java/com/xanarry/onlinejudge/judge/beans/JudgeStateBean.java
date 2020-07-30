package com.xanarry.onlinejudge.judge.beans;

public class JudgeStateBean {
    private int submitID;
    private String state;

    public JudgeStateBean() {
    }

    public JudgeStateBean(int submitID, String state) {
        this.submitID = submitID;
        this.state = state;
    }

    public int getSubmitID() {
        return submitID;
    }

    public void setSubmitID(int submitID) {
        this.submitID = submitID;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "JudgeStateBean{" +
                "submitID=" + submitID +
                ", state='" + state + '\'' +
                '}';
    }
}
