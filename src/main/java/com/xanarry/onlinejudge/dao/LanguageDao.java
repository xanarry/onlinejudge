package com.xanarry.onlinejudge.dao;

import com.xanarry.onlinejudge.model.LanguageBean;

import java.util.List;

/**
 * Created by xanarry on 18-1-1.
 */
public interface LanguageDao {
    void insertLanguage(LanguageBean languageBean);

    LanguageBean getLanguageByID(int languageID);

    short getLanguageID(String language);

    List<LanguageBean> getLanguageList();
}
