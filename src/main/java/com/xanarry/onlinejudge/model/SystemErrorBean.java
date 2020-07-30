package com.xanarry.onlinejudge.model;

public class SystemErrorBean {
    private int submitID;
    private String errorMessage;

    public SystemErrorBean() {
    }

    public SystemErrorBean(int submitID, String errorMessage) {
        this.submitID = submitID;
        this.errorMessage = errorMessage;
    }

    public int getSubmitID() {
        return submitID;
    }

    public void setSubmitID(int submitID) {
        this.submitID = submitID;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String toString() {
        return "SystemErrorBean{" +
                "submitID=" + submitID +
                ", errorMessage='" + errorMessage + '\'' +
                '}';
    }
}
