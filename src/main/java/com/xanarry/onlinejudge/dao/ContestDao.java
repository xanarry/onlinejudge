package com.xanarry.onlinejudge.dao;

import com.xanarry.onlinejudge.model.ContestBean;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by xanarry on 18-1-1.
 */
public interface ContestDao {
    int addContest(@Param("contest") ContestBean contest);

    void deleteContest(@Param("contestID") Integer contestID);

    void updateContest(@Param("contest") ContestBean contest);

    ContestBean getContestByID(@Param("contestID") Integer contestID);

    void getContestByTitle(@Param("title") String title);

    List<ContestBean> getContestList(@Param("start") Integer start, @Param("count") Integer count);

    int getCount();
}
