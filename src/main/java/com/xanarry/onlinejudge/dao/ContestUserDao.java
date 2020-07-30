package com.xanarry.onlinejudge.dao;

import com.xanarry.onlinejudge.model.ContestUserBean;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by xanarry on 18-1-1.
 */
public interface ContestUserDao {
    int addUser(@Param("contestUser") ContestUserBean contestUser);

    void deleteUser(@Param("contestID") Integer contestID, @Param("userID") Integer userID);

    boolean checkUserRegistered(@Param("contestID") int contestID, @Param("userID") Integer userID);

    List<ContestUserBean> getContestUserList(@Param("contestID") Integer contestID);

    Integer getContestUserCount(@Param("contestID") Integer contestID);

    int getCount();
}
