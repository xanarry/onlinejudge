package com.xanarry.onlinejudge.controller;

import com.xanarry.onlinejudge.controller.beans.MessageBean;
import com.xanarry.onlinejudge.dao.LanguageDao;
import com.xanarry.onlinejudge.dao.UserDao;
import com.xanarry.onlinejudge.model.LanguageBean;
import com.xanarry.onlinejudge.model.UserBean;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

/**
 * Created by xanarry on 18-1-1.
 */
@Controller
public class RegisterController {
    @Autowired
    private LanguageDao languageDao;
    @Autowired
    private UserDao userDao;

    @GetMapping(value = "/register")
    public String getRegisterPage(ModelMap mp) {
        List<LanguageBean> languageList = languageDao.getLanguageList();
        mp.addAttribute("languageList", languageList);
        return "/user/user-register";
    }

    @PostMapping(value = "/register")
    public String userRegister(HttpServletRequest request, HttpServletResponse response) {
        String userName = request.getParameter("inputUserName");
        String email = request.getParameter("inputEmail");
        String password = request.getParameter("inputPassword");
        String bio = request.getParameter("inputBio");
        String preferLanguage = request.getParameter("inputPreferLanguage");

        UserBean userBean = new UserBean();
        userBean.setUserName(userName);
        userBean.setEmail(email);
        userBean.setPassword(DigestUtils.sha1Hex(password));
        userBean.setRegisterTime(new Date().getTime());
        userBean.setLastLoginTime(new Date().getTime());
        userBean.setBio(bio);
        userBean.setPreferLanguage(preferLanguage);
        userBean.setSendCode(true);


        userDao.addNewUser(userBean);

        MessageBean messageBean = new MessageBean("注册成功", "Info", "恭喜您已经成功完成注册,登录使用系统吧!", "/", "Got It");
        return "redirect:" + Utils.getErrorPageURL(messageBean);
    }


    @PostMapping(value = "/ajax-check-register-info", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String ajaxCheckRegisterInfo(HttpServletRequest request, HttpServletResponse response) {
        String userName = request.getParameter("inputUserName");
        String email = request.getParameter("inputEmail");

        System.out.println("checking: " + userName + " " + email);

        Boolean userNameCheckVal = userDao.checkUserNameExist(userName);
        Boolean emailCheckVal = userDao.checkEmailExist(email);

        /*
         json: {"userNameExist": true, "emailExist": true}
         */

        String jsonPattern = "{\"userNameExist\": %s, \"emailExist\": %s}";
        return String.format(jsonPattern, userNameCheckVal, emailCheckVal);
    }
}
