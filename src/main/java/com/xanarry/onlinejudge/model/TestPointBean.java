package com.xanarry.onlinejudge.model;

/**
 * Created by xanarry on 18-1-5.
 */
public class TestPointBean {
    private int problemID;
    private int testPointID;
    private String inputTextPath;
    private int inputTextLength;
    private String outputTextPath;
    private int outputTextLength;

    public TestPointBean() {
    }

    public TestPointBean(int problemID, int testPointID, String inputTextPath, int inputTextLength, String outputTextPath, int outputTextLength) {
        this.problemID = problemID;
        this.testPointID = testPointID;
        this.inputTextPath = inputTextPath;
        this.inputTextLength = inputTextLength;
        this.outputTextPath = outputTextPath;
        this.outputTextLength = outputTextLength;
    }

    public int getProblemID() {
        return problemID;
    }

    public void setProblemID(int problemID) {
        this.problemID = problemID;
    }

    public int getTestPointID() {
        return testPointID;
    }

    public void setTestPointID(int testPointID) {
        this.testPointID = testPointID;
    }

    public String getInputTextPath() {
        return inputTextPath;
    }

    public void setInputTextPath(String inputTextPath) {
        this.inputTextPath = inputTextPath;
    }

    public int getInputTextLength() {
        return inputTextLength;
    }

    public void setInputTextLength(int inputTextLength) {
        this.inputTextLength = inputTextLength;
    }

    public String getOutputTextPath() {
        return outputTextPath;
    }

    public void setOutputTextPath(String outputTextPath) {
        this.outputTextPath = outputTextPath;
    }

    public int getOutputTextLength() {
        return outputTextLength;
    }

    public void setOutputTextLength(int outputTextLength) {
        this.outputTextLength = outputTextLength;
    }

    @Override
    public String toString() {
        return "TestPointBean{" +
                "problemID=" + problemID +
                ", testPointID=" + testPointID +
                ", inputTextPath='" + inputTextPath + '\'' +
                ", inputTextLength=" + inputTextLength +
                ", outputTextPath='" + outputTextPath + '\'' +
                ", outputTextLength=" + outputTextLength +
                '}';
    }
}
