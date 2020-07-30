package com.xanarry.onlinejudge.controller;


import com.xanarry.onlinejudge.controller.beans.MessageBean;
import com.xanarry.onlinejudge.controller.beans.PageBean;
import com.xanarry.onlinejudge.dao.LanguageDao;
import com.xanarry.onlinejudge.dao.UserDao;
import com.xanarry.onlinejudge.dao.ViewSubmitRecordDao;
import com.xanarry.onlinejudge.model.LanguageBean;
import com.xanarry.onlinejudge.model.UserBean;
import com.xanarry.onlinejudge.utils.Consts;
import com.xanarry.onlinejudge.utils.SendMail;
import com.xanarry.onlinejudge.utils.Tools;
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

import static org.apache.commons.codec.digest.DigestUtils.sha1Hex;

@Controller
public class UserController {
    @Autowired
    private UserDao userDao;

    @Autowired
    private LanguageDao languageDao;

    @Autowired
    private ViewSubmitRecordDao viewSubmitRecordDao;


    @GetMapping(value = "/user-list")
    public String userListGet(HttpServletRequest request, ModelMap mp) {
        String keyword = request.getParameter("keyword");
        if (keyword == null) {
            keyword = "";
        }

        String strPage = request.getParameter("page");
        int page = strPage != null ? Integer.parseInt(strPage) : 1;


        List<UserBean> userList = userDao.getUserList(keyword, (page - 1) * Consts.COUNT_PER_PAGE, Consts.COUNT_PER_PAGE);
        int recordCount = userDao.getcountOfSearch(keyword);

        //获取分页信息
        PageBean pageBean = Utils.getPagination(recordCount, page, request);


        mp.addAttribute("tableTitle", "用户列表(" + recordCount + ")");
        mp.addAttribute("userList", userList);
        mp.addAttribute("pagination", pageBean);
        return "/user/user-list";
    }

    @GetMapping(value = "/user-chart")
    public String userChartGet(HttpServletRequest request, ModelMap mp) {
        String strPage = request.getParameter("page");
        int page = strPage != null ? Integer.parseInt(strPage) : 1;

        List<UserBean> userList = userDao.getChart((page - 1) * Consts.COUNT_PER_PAGE, Consts.COUNT_PER_PAGE);
        int recordCount = userDao.getCount();
        //获取分页信息
        PageBean pageBean = Utils.getPagination(recordCount, page, request);


        mp.addAttribute("tableTitle", "排行榜");
        mp.addAttribute("userList", userList);
        mp.addAttribute("pagination", pageBean);
        return "/user/user-chart";
    }


    @ResponseBody
    @PostMapping(value = "/send-retrieve-password-email", produces = MediaType.APPLICATION_JSON_VALUE)
    public String sendRetrievePasswordEmailPost(HttpServletRequest request, HttpServletResponse response) {
        String userName = request.getParameter("inputUserName");
        String email = request.getParameter("inputEmail");


        UserBean userBean = userDao.getUserByEmail(email);


        String jsonPattern = "{\"userNameCheck\":%s, \"emailCheck\":%s, \"sendMail\":%s}";
        String json = "{\"userNameCheck\": false, \"emailCheck\":false, \"sendMail\":false}";
        if (userBean != null && userBean.getUserName().equals(userName)) {
            String code = Tools.saltBase64Encode(new Date().getTime() + "#" + email);
            String url = new String(request.getRequestURL()).replace(request.getRequestURI(), "") + "/retrieve-password?pattern=" + code;
            SendMail.sendMail(new String[]{email}, "oj系统邮件-找回密码", "请点击以下链接找回密码, 30分钟以内有效!<br>" + url);
            json = String.format(jsonPattern, true, true, true);
        } else {
            json = String.format(jsonPattern, false, false, true);
        }

        return json;
    }

    @GetMapping(value = "/user")
    public String showUserGet(HttpServletRequest request, HttpServletResponse response, ModelMap mp) {
        String strUserID = request.getParameter("userID");
        MessageBean messageBean = new MessageBean("错误", "错误", "用户ID不存在!", "/", "回到首页");

        if (strUserID != null) {
            Integer userID = Integer.parseInt(strUserID);

            UserBean userBean = userDao.getUserByID(userID);
            List<LanguageBean> languages = languageDao.getLanguageList();
            List<Integer> acceptedProblem = viewSubmitRecordDao.getUserAcceptedProblems(userID);


            if (userBean != null) {
                mp.addAttribute("user", userBean);
                mp.addAttribute("acceptedProblem", acceptedProblem);
                mp.addAttribute("languages", languages);
                return "user/user-information";
            } else {
                messageBean = new MessageBean("错误", "错误", "用户ID不存在!", "/", "回到首页");
            }
        }
        return Utils.getErrorPageURL(messageBean);
    }


    @GetMapping(value = "/user-delete")
    public String deleteUserGet(HttpServletRequest request) {
        String strUserID = request.getParameter("userID");
        MessageBean messageBean = new MessageBean("错误", "错误", "目标用户不存在!", request.getHeader("referer"), "返回");

        if (strUserID != null) {
            Integer userID = Integer.parseInt(strUserID);
            //删除用户本身信息
            UserBean userBean = userDao.getUserByID(userID);
            String userName = userBean.getUserName();

            /*数据库中定义的触发器将会删除和该用户相关的: 提交信息, 比赛信息, 讨论信息*/
            userDao.deleteUserById(userID);
            messageBean = new MessageBean("提示", "提示", userName + " 已经被删除!", request.getHeader("referer"), "返回");
        }
        return "redirect:" + Utils.getErrorPageURL(messageBean);
    }

    @PostMapping(value = "/user-edit", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String editUserPost(HttpServletRequest request, HttpServletResponse response) {
        String strUserID = request.getParameter("inputUserID");
        Integer userID = Integer.parseInt(strUserID);
        String userName = request.getParameter("inputUserName");
        String oldPassword = request.getParameter("inputOldPassword");
        String password = request.getParameter("inputPassword");
        String bio = request.getParameter("inputBio");
        String sendCode = request.getParameter("inputSendCode");
        String preferLanguage = request.getParameter("inputPreferLanguage");


        UserBean userBean = userDao.getUserByID(userID);

        boolean userNameExist = userDao.checkUserNameExist(userName) && !userName.equals(userBean.getUserName());
        boolean oldPasswordOk = userBean.getPassword().equals(sha1Hex(oldPassword));

        if (!userNameExist && oldPasswordOk) {

            userBean.setUserName(userName);
            userBean.setPassword(sha1Hex(password));
            userBean.setBio(bio);
            userBean.setSendCode(sendCode != null && sendCode.equals("true"));
            userBean.setPreferLanguage(preferLanguage);

            System.out.println("ok, new user info: " + userBean);

            userDao.updateUser(userBean);

            userNameExist = false;
            oldPasswordOk = true;

        }


        String jsonPattern = "{\"userNameExist\" : %s, \"correctOldPassword\" :%s}";
        String json = String.format(jsonPattern, userNameExist, oldPasswordOk);
        return json;
    }


    @GetMapping(value = "/retrieve-password")
    public String retrievePasswordGet(HttpServletRequest request, HttpServletResponse response, ModelMap mp) {
        String code = request.getParameter("pattern");

        if (code != null && code.length() > 10) {
            String msg = Tools.saltBase64Decode(code);
            System.out.println(msg);
            String[] c = msg.split("#");

            if (new Date().getTime() - Long.parseLong(c[0]) > 30 * 60 * 1000) {
                MessageBean messageBean = new MessageBean();
                messageBean.setTitle("错误");
                messageBean.setHeader("错误");
                messageBean.setMessage("该链接已经失效!");
                messageBean.setUrl("/");
                messageBean.setLinkText("返回首页");
                return "redirect:" + Utils.getErrorPageURL(messageBean);
            }
            mp.addAttribute("email", c[1]);
        }
        return "/user/user-retrieve-password";
    }

    @PostMapping(value = "/retrieve-password")
    public String retrievePasswordPost(HttpServletRequest request, HttpServletResponse response) {
        String email = request.getParameter("inputRetrieveEmail");
        String password = request.getParameter("inputNewPassword");


        UserBean userBean = userDao.getUserByEmail(email);
        userBean.setPassword(sha1Hex(password.getBytes()));

        userDao.updateUser(userBean);


        MessageBean messageBean = new MessageBean();
        messageBean.setTitle("消息");
        messageBean.setHeader("消息");
        messageBean.setMessage("密码设置成功");
        messageBean.setUrl("/");
        messageBean.setLinkText("返回首页");
        return "redirect:" + Utils.getErrorPageURL(messageBean);
    }

    @PostMapping(value = "/update-user-type")
    public String updateUserTypePost(HttpServletRequest request, HttpServletResponse response) {
        String strUserID = request.getParameter("inputUserID");
        String strUserType = request.getParameter("inputUserType");
        Integer userID = strUserID != null && strUserID.length() > 0 ? Integer.parseInt(strUserID) : null;
        Short userType = Short.parseShort(strUserType);


        UserBean userBean = userDao.getUserByID(userID);
        userBean.setUserType(userType);
        userDao.updateUser(userBean);

        return "redirect:/user?userID=" + userID;
    }
}
