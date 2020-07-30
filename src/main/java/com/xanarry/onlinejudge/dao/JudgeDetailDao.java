package com.xanarry.onlinejudge.dao;

import com.xanarry.onlinejudge.model.JudgeDetailBean;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by xanarry on 18-1-1.
 */
public interface JudgeDetailDao {
    //insert
    int insertJudgeDetail(@Param("judgeDetail") JudgeDetailBean judgeDetail);

    //delete
    void deleteJudgeDetail(@Param("submitID") int submitID);

    //update rejudge may user this function
    void updateJudegeDetail(@Param("newJudgeDetail") JudgeDetailBean newJudgeDetail);

    //select
    List<JudgeDetailBean> getJudegeDetailBySubmitID(@Param("submitID") int submitID);

    JudgeDetailBean getJudgeDetailByTestPointID(@Param("submitID") int submitID, @Param("testPointID") int testPointID);
}
