package com.xanarry.onlinejudge.model;

/**
 * Created by xanarry on 17-12-30.
 */
public class ProblemBean {
    private int problemID;
    private String title;
    private String desc;
    private String inputDesc;
    private String outputDesc;
    private String inputSample;
    private String outputSample;
    private String hint;
    private String source;
    private String background;
    private Long createTime;
    private int staticLangTimeLimit;
    private int staticLangMemLimit;
    private int dynamicLangTimeLimit;
    private int dynamicLangMemLimit;
    private int accepted;
    private int submitted;
    private String result;/*当用户登录后查看题目时, 本字段记录用户本题是否通过*/

    public ProblemBean() {
    }

    public ProblemBean(int problemID, String title, String desc, String inputDesc, String outputDesc, String inputSample, String outputSample, String hint, String source, String background, Long createTime, int staticLangTimeLimit, int staticLangMemLimit, int dynamicLangTimeLimit, int dynamicLangMemLimit, int accepted, int submitted, String result) {
        this.problemID = problemID;
        this.title = title;
        this.desc = desc;
        this.inputDesc = inputDesc;
        this.outputDesc = outputDesc;
        this.inputSample = inputSample;
        this.outputSample = outputSample;
        this.hint = hint;
        this.source = source;
        this.background = background;
        this.createTime = createTime;
        this.staticLangTimeLimit = staticLangTimeLimit;
        this.staticLangMemLimit = staticLangMemLimit;
        this.dynamicLangTimeLimit = dynamicLangTimeLimit;
        this.dynamicLangMemLimit = dynamicLangMemLimit;
        this.accepted = accepted;
        this.submitted = submitted;
        this.result = result;
    }

    public int getProblemID() {
        return problemID;
    }

    public void setProblemID(int problemID) {
        this.problemID = problemID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getInputDesc() {
        return inputDesc;
    }

    public void setInputDesc(String inputDesc) {
        this.inputDesc = inputDesc;
    }

    public String getOutputDesc() {
        return outputDesc;
    }

    public void setOutputDesc(String outputDesc) {
        this.outputDesc = outputDesc;
    }

    public String getInputSample() {
        return inputSample;
    }

    public void setInputSample(String inputSample) {
        this.inputSample = inputSample;
    }

    public String getOutputSample() {
        return outputSample;
    }

    public void setOutputSample(String outputSample) {
        this.outputSample = outputSample;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public int getStaticLangTimeLimit() {
        return staticLangTimeLimit;
    }

    public void setStaticLangTimeLimit(int staticLangTimeLimit) {
        this.staticLangTimeLimit = staticLangTimeLimit;
    }

    public int getStaticLangMemLimit() {
        return staticLangMemLimit;
    }

    public void setStaticLangMemLimit(int staticLangMemLimit) {
        this.staticLangMemLimit = staticLangMemLimit;
    }

    public int getDynamicLangTimeLimit() {
        return dynamicLangTimeLimit;
    }

    public void setDynamicLangTimeLimit(int dynamicLangTimeLimit) {
        this.dynamicLangTimeLimit = dynamicLangTimeLimit;
    }

    public int getDynamicLangMemLimit() {
        return dynamicLangMemLimit;
    }

    public void setDynamicLangMemLimit(int dynamicLangMemLimit) {
        this.dynamicLangMemLimit = dynamicLangMemLimit;
    }

    public int getAccepted() {
        return accepted;
    }

    public void setAccepted(int accepted) {
        this.accepted = accepted;
    }

    public int getSubmitted() {
        return submitted;
    }

    public void setSubmitted(int submitted) {
        this.submitted = submitted;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "ProblemBean{" +
                "problemID=" + problemID +
                ", title='" + title + '\'' +
                ", desc='" + desc + '\'' +
                ", inputDesc='" + inputDesc + '\'' +
                ", outputDesc='" + outputDesc + '\'' +
                ", inputSample='" + inputSample + '\'' +
                ", outputSample='" + outputSample + '\'' +
                ", hint='" + hint + '\'' +
                ", source='" + source + '\'' +
                ", background='" + background + '\'' +
                ", createTime=" + createTime +
                ", staticLangTimeLimit=" + staticLangTimeLimit +
                ", staticLangMemLimit=" + staticLangMemLimit +
                ", dynamicLangTimeLimit=" + dynamicLangTimeLimit +
                ", dynamicLangMemLimit=" + dynamicLangMemLimit +
                ", accepted=" + accepted +
                ", submitted=" + submitted +
                ", result='" + result + '\'' +
                '}';
    }
}
