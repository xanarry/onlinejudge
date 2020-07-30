package com.xanarry.onlinejudge.model;

/**
 * Created by xanarry on 17-12-30.
 */
public class CompileInfoBean {
    private int submitID;
    private String compileResult;

    public CompileInfoBean() {
    }

    public CompileInfoBean(int submitID, String compileResult) {
        this.submitID = submitID;
        this.compileResult = compileResult;
    }

    public int getSubmitID() {
        return submitID;
    }

    public void setSubmitID(int submitID) {
        this.submitID = submitID;
    }

    public String getCompileResult() {
        return compileResult;
    }

    public void setCompileResult(String compileResult) {
        this.compileResult = compileResult;
    }

    @Override
    public String toString() {
        return "CompileInfoBean{" +
                "submitID=" + submitID +
                ", compileResult='" + compileResult + '\'' +
                '}';
    }
}
