package com.xanarry.onlinejudge.controller.beans;

import java.util.TreeMap;

public class RankBean {
    private int rank;
    private int userID;
    private String userName;
    private int AC_Count;
    private long totalTimeConsume;
    private TreeMap<Integer, UserProblemStatisticBean> problems;

    public RankBean() {
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getAC_Count() {
        return AC_Count;
    }

    public void setAC_Count(int AC_Count) {
        this.AC_Count = AC_Count;
    }

    public long getTotalTimeConsume() {
        return totalTimeConsume;
    }

    public void setTotalTimeConsume(long totalTimeConsume) {
        this.totalTimeConsume = totalTimeConsume;
    }

    public TreeMap<Integer, UserProblemStatisticBean> getProblems() {
        return problems;
    }

    public void setProblems(TreeMap<Integer, UserProblemStatisticBean> problems) {
        this.problems = problems;
    }

    @Override
    public String toString() {
        return "RankBean{" +
                "rank=" + rank +
                ", userID=" + userID +
                ", userName='" + userName + '\'' +
                ", AC_Count=" + AC_Count +
                ", totalTimeConsume=" + totalTimeConsume +
                ", problems=" + problems +
                '}';
    }
}
