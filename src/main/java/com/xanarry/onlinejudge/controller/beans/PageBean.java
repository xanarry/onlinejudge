package com.xanarry.onlinejudge.controller.beans;

public class PageBean {
    private int currentPage;//当前页面
    private int countPerPage;//每页记录数
    private int maxPageCount;//最大页码
    private int recordCount;//记录总数
    private String baseURL;

    public PageBean() {
    }

    public PageBean(int currentPage, int countPerPage, int maxPageCount, int recordCount, String baseURL) {
        this.currentPage = currentPage;
        this.countPerPage = countPerPage;
        this.maxPageCount = maxPageCount;
        this.recordCount = recordCount;
        this.baseURL = baseURL;
    }

    @Override
    public String toString() {
        return "PageBean{" +
                "currentPage=" + currentPage +
                ", countPerPage=" + countPerPage +
                ", maxPageCount=" + maxPageCount +
                ", recordCount=" + recordCount +
                ", baseURL='" + baseURL + '\'' +
                '}';
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getCountPerPage() {
        return countPerPage;
    }

    public void setCountPerPage(int countPerPage) {
        this.countPerPage = countPerPage;
    }

    public int getMaxPageCount() {
        return maxPageCount;
    }

    public void setMaxPageCount(int maxPageCount) {
        this.maxPageCount = maxPageCount;
    }

    public int getRecordCount() {
        return recordCount;
    }

    public void setRecordCount(int recordCount) {
        this.recordCount = recordCount;
    }

    public String getBaseURL() {
        return baseURL;
    }

    public void setBaseURL(String baseURL) {
        this.baseURL = baseURL;
    }
}
