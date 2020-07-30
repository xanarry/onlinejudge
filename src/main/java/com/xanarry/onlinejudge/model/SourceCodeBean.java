package com.xanarry.onlinejudge.model;

/**
 * Created by xanarry on 17-12-30.
 */
public class SourceCodeBean {
    private int submitID;
    private String sourceCode;
    private int sourceCodeLength;

    public SourceCodeBean() {
    }

    public SourceCodeBean(int submitID, String sourceCode, int sourceCodeLength) {
        this.submitID = submitID;
        this.sourceCode = sourceCode;
        this.sourceCodeLength = sourceCodeLength;
    }

    public int getSubmitID() {
        return submitID;
    }

    public void setSubmitID(int submitID) {
        this.submitID = submitID;
    }

    public String getSourceCode() {
        return sourceCode;
    }

    public void setSourceCode(String sourceCode) {
        this.sourceCode = sourceCode;
    }

    public int getSourceCodeLength() {
        return sourceCodeLength;
    }

    public void setSourceCodeLength(int sourceCodeLength) {
        this.sourceCodeLength = sourceCodeLength;
    }

    @Override
    public String toString() {
        return "SourceCodeBean{" +
                "submitID=" + submitID +
                ", sourceCode='" + sourceCode + '\'' +
                ", sourceCodeLength=" + sourceCodeLength +
                '}';
    }
}
