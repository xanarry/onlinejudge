package com.xanarry.onlinejudge.model;

/**
 * Created by xanarry on 17-12-30.
 */
public class LanguageBean {
    private int languageID;
    private String language;

    public LanguageBean() {
    }

    public LanguageBean(int languageID, String language) {
        this.languageID = languageID;
        this.language = language;
    }

    public int getLanguageID() {
        return languageID;
    }

    public void setLanguageID(int languageID) {
        this.languageID = languageID;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    @Override
    public String toString() {
        return "LanguageBean{" +
                "languageID=" + languageID +
                ", language='" + language + '\'' +
                '}';
    }
}
