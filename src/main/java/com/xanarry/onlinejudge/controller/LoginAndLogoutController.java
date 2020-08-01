package com.xanarry.onlinejudge.controller;


import com.xanarry.onlinejudge.dao.UserDao;
import com.xanarry.onlinejudge.model.UserBean;
import com.xanarry.onlinejudge.utils.Tools;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

/**
 * Created by xanarry on 18-1-2.
 */

@Controller
public class LoginAndLogoutController {
    @Autowired
    private UserDao userDao;

    @GetMapping(value = "/ajaxGetValidateCode")
    public void ajaxGetValidateCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int a = (int) (Math.random() * 10) + 1;
        int b = (int) (Math.random() * 10) + 1;
        int validateCode = a + b;
        String optr = "+";
        if ((a + b) % 2 == 0) {
            optr = "*";
            validateCode = a * b;
        }
        String randStr = String.format("%d%s%d=?", a, optr, b);
        System.out.println("randStr: " + randStr + " session: " + validateCode);

        request.getSession().setAttribute("validateCode", validateCode + "");

        BufferedImage bufferedImage = Tools.getValidateCode(100, 38, randStr);
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);

        response.setContentType("image/jpeg");

        // 将图像输出到Servlet输出流中。
        ServletOutputStream sos = response.getOutputStream();
        ImageIO.write(bufferedImage, "jpeg", sos);
        sos.close();
    }

    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String login(HttpServletRequest request, HttpServletResponse response) {
        String email = request.getParameter("inputEmail");
        String password = request.getParameter("inputPassword");
        String validateCode = request.getParameter("inputValidateCode");
        String srememberMe = request.getParameter("inputRememberMe");

        boolean rememberMe = srememberMe != null && srememberMe.equals("1");
        String sessionValidateCode = (String) request.getSession().getAttribute("validateCode");
        UserBean userBean = userDao.getUserByEmail(email);

        String jsonPattern = "{\"userExist\" : %s, \"correctPassword\" : %s, \"correctValidateCode\" :%s}";
        boolean userExist = true;
        boolean correctPassword = true;
        boolean correctValidateCode = true;

        if (userBean != null) {
            //用户存在, 检查密码
            if (userBean.getPassword().equals(DigestUtils.sha1Hex(password))) {
                //密码正确,检查验证码
                if (validateCode.equals(sessionValidateCode)) {
                    HttpSession session = request.getSession();
                    session.setAttribute("userID", userBean.getUserID());
                    session.setAttribute("userName", userBean.getUserName());
                    session.setAttribute("userType", userBean.getUserType());
                    if (rememberMe) { //记住一周
                        session.setMaxInactiveInterval(7 * 24 * 60 * 60);
                    }

                    //set new login time
                    userBean.setLastLoginTime(new Date().getTime());
                    userDao.updateUser(userBean);
                } else {
                    //验证码错误
                    correctValidateCode = false;
                }
            } else {
                //密码错误
                correctPassword = false;
            }

        } else {
            //用户不存在
            userExist = false;
        }

        return String.format(jsonPattern, userExist, correctPassword, correctValidateCode);
    }


    @GetMapping(value = "/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.getSession().invalidate();
        return "redirect:/";
    }

    @GetMapping(value = "/getCaptcha")
    public void getCaptcha(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.getSession().setAttribute("validateCode", "2");
        BufferedImage bufferedImage = Tools.getValidateCode(1, 1, "1+1=?");
        // flush it in the response
        response.setHeader("Cache-Control", "no-store");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");
        OutputStream responseOutputStream = response.getOutputStream();
        ImageIO.write(bufferedImage, "jpeg", responseOutputStream);
        responseOutputStream.flush();
        responseOutputStream.close();
    }
}
