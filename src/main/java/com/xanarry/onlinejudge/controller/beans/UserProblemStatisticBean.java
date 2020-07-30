package com.xanarry.onlinejudge.controller.beans;

public class UserProblemStatisticBean {
    private int problemID;
    private String innerID;
    private boolean accepted;
    private boolean firstAccepted;
    private int tryTimes;
    private Long timeConsume;
    private String strTimeConsume;

    public UserProblemStatisticBean() {
    }

    public UserProblemStatisticBean(int problemID, String innerID, boolean accepted, boolean firstAccepted, int tryTimes, Long timeConsume, String strTimeConsume) {
        this.problemID = problemID;
        this.innerID = innerID;
        this.accepted = accepted;
        this.firstAccepted = firstAccepted;
        this.tryTimes = tryTimes;
        this.timeConsume = timeConsume;
        this.strTimeConsume = strTimeConsume;
    }

    public int getProblemID() {
        return problemID;
    }

    public void setProblemID(int problemID) {
        this.problemID = problemID;
    }

    public String getInnerID() {
        return innerID;
    }

    public void setInnerID(String innerID) {
        this.innerID = innerID;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public boolean isFirstAccepted() {
        return firstAccepted;
    }

    public void setFirstAccepted(boolean firstAccepted) {
        this.firstAccepted = firstAccepted;
    }

    public int getTryTimes() {
        return tryTimes;
    }

    public void setTryTimes(int tryTimes) {
        this.tryTimes = tryTimes;
    }

    public Long getTimeConsume() {
        return timeConsume;
    }

    public void setTimeConsume(Long timeConsume) {
        this.timeConsume = timeConsume;
    }

    public String getStrTimeConsume() {
        return strTimeConsume;
    }

    public void setStrTimeConsume(String strTimeConsume) {
        this.strTimeConsume = strTimeConsume;
    }

    @Override
    public String toString() {
        return "UserProblemStatisticBean{" +
                "problemID=" + problemID +
                ", innerID='" + innerID + '\'' +
                ", accepted=" + accepted +
                ", firstAccepted=" + firstAccepted +
                ", tryTimes=" + tryTimes +
                ", timeConsume=" + timeConsume +
                ", strTimeConsume='" + strTimeConsume + '\'' +
                '}';
    }
}