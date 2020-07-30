package com.xanarry.onlinejudge.dao;

import com.xanarry.onlinejudge.model.SystemErrorBean;
import org.springframework.data.repository.query.Param;

public interface SystemErrorDao {
    void addErrorMessage(@Param("systemError") SystemErrorBean systemError);

    void deleteErrorMessage(@Param("submitID") Integer submitID);

    SystemErrorBean getSystemErrorMessage(@Param("submitID") Integer submitID);
}
