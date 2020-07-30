package com.xanarry.onlinejudge.dao;

import com.xanarry.onlinejudge.model.DiscussBean;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by xanarry on 18-1-1.
 */
public interface DiscussDao {
    int insertDiscuss(@Param("discuss") DiscussBean discuss);


    void setAsRoot(@Param("discuss") DiscussBean discuss);

    void addWatch(@Param("postID") Integer postID);

    void updateReply(@Param("postID") Integer postID);/*有删除回复的时候使用*/

    void setFirst(@Param("postID") Integer postID, @Param("val") Integer val);


    void deleteDiscussByPostID(@Param("postID") Integer postID);

    void deleteDiscussByRootID(@Param("rootID") Integer rootID);

    void deleteDiscussByUserD(@Param("userID") Integer userID);

    DiscussBean getDiscussByPostID(@Param("postID") Integer postID);

    List<DiscussBean> getDiscussListByRootID(@Param("rootID") Integer rootID);


    List<DiscussBean> getDiscussTitleList(@Param("type") Integer type, @Param("porcID") Integer porcID, @Param("theme") String theme, @Param("start") Integer start, @Param("count") Integer count);

    Integer getCountOfTitleList(@Param("type") Integer type, @Param("porcID") Integer porcID, @Param("theme") String theme);

    int getCount();
}
