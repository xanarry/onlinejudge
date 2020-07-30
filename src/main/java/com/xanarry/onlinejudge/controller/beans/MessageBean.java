package com.xanarry.onlinejudge.controller.beans;

/**
 * Created by xanarry on 18-1-1.
 * <p>
 * 这个bean用来存放返回到用户提示,或者是警告信息, 与message.jsp配套
 */
public class MessageBean {
    private String title;
    private String header;
    private String message;
    private String url;
    private String linkText;

    public MessageBean() {
    }

    public MessageBean(String title, String header, String message, String url, String linkText) {
        this.title = title;
        this.header = header;
        this.message = message;
        this.url = url;
        this.linkText = linkText;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLinkText() {
        return linkText;
    }

    public void setLinkText(String linkText) {
        this.linkText = linkText;
    }

    @Override
    public String toString() {
        return "MessageBean{" +
                "title='" + title + '\'' +
                ", header='" + header + '\'' +
                ", message='" + message + '\'' +
                ", url='" + url + '\'' +
                ", linkText='" + linkText + '\'' +
                '}';
    }
}
