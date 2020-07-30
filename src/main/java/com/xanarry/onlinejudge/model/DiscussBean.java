package com.xanarry.onlinejudge.model;

/**
 * Created by xanarry on 17-12-30.
 */
public class DiscussBean {
    private int postID;
    private int directFID;
    private int rootID;
    private int type;
    private int porcID;
    private String theme;
    private String title;
    private String content;
    private long postTime;
    private int userID;
    private String userName;
    private int reply;
    private int watch;
    private short first;

    public DiscussBean() {
    }

    public DiscussBean(int postID, int directFID, int rootID, int type, int porcID, String theme, String title, String content, long postTime, int userID, String userName, int reply, int watch, short first) {
        this.postID = postID;
        this.directFID = directFID;
        this.rootID = rootID;
        this.type = type;
        this.porcID = porcID;
        this.theme = theme;
        this.title = title;
        this.content = content;
        this.postTime = postTime;
        this.userID = userID;
        this.userName = userName;
        this.reply = reply;
        this.watch = watch;
        this.first = first;
    }

    public int getPostID() {
        return postID;
    }

    public void setPostID(int postID) {
        this.postID = postID;
    }

    public int getDirectFID() {
        return directFID;
    }

    public void setDirectFID(int directFID) {
        this.directFID = directFID;
    }

    public int getRootID() {
        return rootID;
    }

    public void setRootID(int rootID) {
        this.rootID = rootID;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getPorcID() {
        return porcID;
    }

    public void setPorcID(int porcID) {
        this.porcID = porcID;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getPostTime() {
        return postTime;
    }

    public void setPostTime(long postTime) {
        this.postTime = postTime;
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

    public int getReply() {
        return reply;
    }

    public void setReply(int reply) {
        this.reply = reply;
    }

    public int getWatch() {
        return watch;
    }

    public void setWatch(int watch) {
        this.watch = watch;
    }

    public short getFirst() {
        return first;
    }

    public void setFirst(short first) {
        this.first = first;
    }

    @Override
    public String toString() {
        return "DiscussBean{" +
                "postID=" + postID +
                ", directFID=" + directFID +
                ", rootID=" + rootID +
                ", type=" + type +
                ", porcID=" + porcID +
                ", theme='" + theme + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", postTime=" + postTime +
                ", userID=" + userID +
                ", userName='" + userName + '\'' +
                ", reply=" + reply +
                ", watch=" + watch +
                ", first=" + first +
                '}';
    }
}
