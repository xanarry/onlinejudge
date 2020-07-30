package com.xanarry.onlinejudge.model;

/**
 * Created by xanarry on 17-12-30.
 */
public class ContestUserBean {
    private int contestID;
    private int userID;
    private String userName;

    public ContestUserBean() {
    }

    public ContestUserBean(int contestID, int userID, String userName) {
        this.contestID = contestID;
        this.userID = userID;
        this.userName = userName;
    }

    public int getContestID() {
        return contestID;
    }

    public void setContestID(int contestID) {
        this.contestID = contestID;
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

    @Override
    public String toString() {
        return "ContestUserBean{" +
                "contestID=" + contestID +
                ", userID=" + userID +
                ", userName=" + userName +
                '}';
    }
}
