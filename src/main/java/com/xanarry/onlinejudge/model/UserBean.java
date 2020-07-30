package com.xanarry.onlinejudge.model;

/**
 * Created by xanarry on 17-12-30.
 */
public class UserBean {
    private int userID;
    private String email;
    private String userName;
    private String password;
    private long registerTime;
    private long lastLoginTime;
    private short userType;
    private String preferLanguage;
    private int accepted;
    private int submitted;
    private String bio;
    private boolean sendCode;

    public UserBean() {
    }

    public UserBean(int userID, String email, String userName, String password, long registerTime, long lastLoginTime, short userType, String preferLanguage, int accepted, int submitted, String bio, boolean sendCode) {
        this.userID = userID;
        this.email = email;
        this.userName = userName;
        this.password = password;
        this.registerTime = registerTime;
        this.lastLoginTime = lastLoginTime;
        this.userType = userType;
        this.preferLanguage = preferLanguage;
        this.accepted = accepted;
        this.submitted = submitted;
        this.bio = bio;
        this.sendCode = sendCode;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(long registerTime) {
        this.registerTime = registerTime;
    }

    public long getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(long lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public short getUserType() {
        return userType;
    }

    public void setUserType(short userType) {
        this.userType = userType;
    }

    public String getPreferLanguage() {
        return preferLanguage;
    }

    public void setPreferLanguage(String preferLanguage) {
        this.preferLanguage = preferLanguage;
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

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public boolean isSendCode() {
        return sendCode;
    }

    public void setSendCode(boolean sendCode) {
        this.sendCode = sendCode;
    }

    @Override
    public String toString() {
        return "UserBean{" +
                "userID=" + userID +
                ", email='" + email + '\'' +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", registerTime=" + registerTime +
                ", lastLoginTime=" + lastLoginTime +
                ", userType=" + userType +
                ", preferLanguage='" + preferLanguage + '\'' +
                ", accepted=" + accepted +
                ", submitted=" + submitted +
                ", bio='" + bio + '\'' +
                ", sendCode=" + sendCode +
                '}';
    }
}
