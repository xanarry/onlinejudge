package com.xanarry.onlinejudge.model;

/**
 * Created by xanarry on 17-12-30.
 */
public class ContestProblemBean {
    private int contestID;
    private int problemID;
    private String title;
    private String innerID;
    private int accepted;
    private int submitted;

    public ContestProblemBean() {
    }

    public ContestProblemBean(int contestID, int problemID, String title, String innerID, int accepted, int submitted) {
        this.contestID = contestID;
        this.problemID = problemID;
        this.title = title;
        this.innerID = innerID;
        this.accepted = accepted;
        this.submitted = submitted;
    }

    public int getContestID() {
        return contestID;
    }

    public void setContestID(int contestID) {
        this.contestID = contestID;
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

    public String getInnerID() {
        return innerID;
    }

    public void setInnerID(String innerID) {
        this.innerID = innerID;
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

    @Override
    public String toString() {
        return "ContestProblemBean{" +
                "contestID=" + contestID +
                ", problemID=" + problemID +
                ", title='" + title + '\'' +
                ", innerID='" + innerID + '\'' +
                ", accepted=" + accepted +
                ", submitted=" + submitted +
                '}';
    }
}
