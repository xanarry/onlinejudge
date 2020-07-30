package com.xanarry.onlinejudge.model;

/**
 * Created by xanarry on 17-12-30.
 */
public class ImagePathBean {
    private int imageID;
    private String absPath;
    private String rltPath;

    public ImagePathBean() {
    }

    public ImagePathBean(int imageID, String absPath, String rltPath) {
        this.imageID = imageID;
        this.absPath = absPath;
        this.rltPath = rltPath;
    }

    public int getImageID() {
        return imageID;
    }

    public void setImageID(int imageID) {
        this.imageID = imageID;
    }

    public String getAbsPath() {
        return absPath;
    }

    public void setAbsPath(String absPath) {
        this.absPath = absPath;
    }

    public String getRltPath() {
        return rltPath;
    }

    public void setRltPath(String rltPath) {
        this.rltPath = rltPath;
    }

    @Override
    public String toString() {
        return "ImagePathBean{" +
                "imageID=" + imageID +
                ", absPath='" + absPath + '\'' +
                ", rltPath='" + rltPath + '\'' +
                '}';
    }
}
