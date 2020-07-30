package com.xanarry.onlinejudge.dao;

import com.xanarry.onlinejudge.model.UserBean;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by xanarry on 18-1-1.
 */
public interface UserDao {
    //insert
    int addNewUser(@Param("user") UserBean user);

    //delete
    void deleteUserById(@Param("userID") Integer userID);


    //update
    void updateUser(@Param("user") UserBean user);

    //select one
    UserBean getUserByID(@Param("userID") int userID);

    UserBean getUserByEmail(@Param("email") String email);


    //select list
    List<String> getUserEmailList();

    List<UserBean> getUserList(@Param("keyword") String keyword, @Param("start") Integer start, @Param("count") Integer count);

    Integer getcountOfSearch(@Param("keyword") String keyword);

    List<UserBean> getChart(@Param("start") Integer start, @Param("count") Integer count);

    //check
    boolean checkEmailExist(@Param("email") String email);

    boolean checkUserNameExist(@Param("userName") String userName);

    int getCount();
}
