package com.xanarry.onlinejudge.dao;

import com.xanarry.onlinejudge.model.CompileInfoBean;
import org.springframework.data.repository.query.Param;

/**
 * Created by xanarry on 18-1-1.
 */
public interface CompileInfoDao {
    //insert
    int insertCompileResult(@Param("compileResult") CompileInfoBean compileResult);

    //delete
    void deleteCompileResult(@Param("submitID") int submitID);

    //update  needn't update function

    //select
    CompileInfoBean getCompileResult(@Param("submitID") int submitID);

}
