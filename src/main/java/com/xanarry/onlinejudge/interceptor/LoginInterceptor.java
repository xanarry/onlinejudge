package com.xanarry.onlinejudge.interceptor;


import com.xanarry.onlinejudge.controller.Utils;
import com.xanarry.onlinejudge.controller.beans.MessageBean;
import com.xanarry.onlinejudge.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

public class LoginInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    UserDao userDao;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uri = request.getRequestURI();
        HttpSession session = request.getSession();

        //TODO  开发阶段，使用默认用户登陆, 生产环境请移除下面这行代码
        //session.setAttribute("userID", 1);session.setAttribute("userName", "xanarry");session.setAttribute("userType", 2);

        TreeSet<String> forAdmin = new TreeSet<>();
        forAdmin.add("/send-email");
        forAdmin.add("/configuration");
        forAdmin.add("/contest-delete");
        forAdmin.add("/problem-delete");
        forAdmin.add("/user-delete");
        forAdmin.add("/test-point-delete");
        forAdmin.add("/contest-problem-delete");
        forAdmin.add("/contest-user-delete");
        forAdmin.add("/discuss-delete");

        TreeSet<String> forAdvancedUser = new TreeSet<>();
        forAdvancedUser.add("/admin");
        forAdvancedUser.add("/user-list");
        forAdvancedUser.add("/contest-add");
        forAdvancedUser.add("/contest-edit");
        forAdvancedUser.add("/contest-problem-edit");
        forAdvancedUser.add("/discuss-set-first");
        forAdvancedUser.add("/problem-add");
        forAdvancedUser.add("/problem-edit");
        forAdvancedUser.add("/rejudge");
        forAdvancedUser.add("/add-test-point");


        Short userType = (Short) session.getAttribute("userType");
        if (userType == null) {
            userType = -1;
        }

        String referer = request.getHeader("referer");
        MessageBean messageBean = new MessageBean("错误", "权限错误", "当前权限无法完成此操作!", referer != null ? referer : "/", "返回");


        if (forAdmin.contains(uri)) {
            if (userType == 2) {
                return true;
            } else {
                response.sendRedirect(Utils.getErrorPageURL(messageBean));
                return false;
            }
        } else if (forAdvancedUser.contains(uri)) {
            if (userType >= 1) {
                return true;
            } else {
                response.sendRedirect(Utils.getErrorPageURL(messageBean));
                return false;
            }
        } else {
            //正常页面, 通过验证
            return true;
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}