package com.xanarry.onlinejudge.controller;


import com.xanarry.onlinejudge.controller.beans.MessageBean;
import com.xanarry.onlinejudge.controller.beans.PageBean;
import com.xanarry.onlinejudge.dao.ContestDao;
import com.xanarry.onlinejudge.dao.DiscussDao;
import com.xanarry.onlinejudge.dao.ProblemDao;
import com.xanarry.onlinejudge.model.DiscussBean;
import com.xanarry.onlinejudge.utils.Consts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

@Controller
public class DiscussController {
    @Autowired
    private DiscussDao discussDao;
    @Autowired
    private ProblemDao problemDao;
    @Autowired
    private ContestDao contestDao;

    @PostMapping(value = "/post-discuss")
    public String postDiscuss(HttpServletRequest request, HttpServletResponse response) {
        Integer userID = null;
        String userName = null;
        //从登录信息中提取用户发布用户的信息
        HttpSession session = request.getSession();
        userID = (Integer) session.getAttribute("userID");
        userName = (String) session.getAttribute("userName");

        //如果用户没用登录, 禁止发布消息
        if (userID == null || userName == null) {
            MessageBean messageBean = new MessageBean();
            messageBean.setTitle("错误");
            messageBean.setHeader("错误信息");
            messageBean.setMessage("请登录后再提交信息");
            messageBean.setLinkText("");
            messageBean.setUrl("#");
            return "redirect:" + Utils.getErrorPageURL(messageBean);
        }
        return "redirect:/discuss-detail?postID=" + postDiscuss(request, response, false).getRootID();
    }

    @PostMapping(value = "/post-original-discuss")
    public String postOriginalDiscuss(HttpServletRequest request, HttpServletResponse response) {
        Integer userID = null;
        String userName = null;
        //从登录信息中提取用户发布用户的信息
        HttpSession session = request.getSession();
        userID = (Integer) session.getAttribute("userID");
        userName = (String) session.getAttribute("userName");

        //如果用户没用登录, 禁止发布消息
        if (userID == null || userName == null) {
            MessageBean messageBean = new MessageBean();
            messageBean.setTitle("错误");
            messageBean.setHeader("错误信息");
            messageBean.setMessage("请登录后再提交信息");
            messageBean.setLinkText("");
            messageBean.setUrl("#");
            return "redirect:" + Utils.getErrorPageURL(messageBean);
        }
        postDiscuss(request, response, false);
        return "redirect:/discuss-list";
    }

    private DiscussBean postDiscuss(HttpServletRequest request, HttpServletResponse response, boolean isOriginal) {
        DiscussBean discussBean = new DiscussBean();
        if (!isOriginal) {
            //如果是回复某一个消息, 则需要设置回复的是哪一楼消息, 设置直接回复楼ID和间接楼ID
            String strDirectFID = request.getParameter("inputDirectFID");
            Integer directFID = strDirectFID != null && strDirectFID.length() > 0 ? Integer.parseInt(strDirectFID) : null;

            String strRootID = request.getParameter("inputRootID");
            Integer rootID = strRootID != null && strRootID.length() > 0 ? Integer.parseInt(strRootID) : null;

            discussBean.setDirectFID(directFID);
            discussBean.setRootID(rootID);

            //回复消息不需要设置theme and title具体数据
            discussBean.setTheme("");
            discussBean.setTitle("");

        } else {
            String title = request.getParameter("inputTitle");
            discussBean.setTitle(title);
        }


        //设置消息类型
        String strType = request.getParameter("inputType");
        Integer type = strType != null && strType.length() > 0 ? Integer.parseInt(strType) : null;
        discussBean.setType(type);

        //如果消息是关于题目或者比赛的, 设置题目或者比赛的ID
        if (type != Consts.FOR_MESSAGE) {
            String strPorcID = request.getParameter("inputPorcID");
            Integer porcID = strPorcID != null && strPorcID.length() > 0 ? Integer.parseInt(strPorcID) : null;
            discussBean.setPorcID(porcID);
            if (type == 0) {
                discussBean.setTheme(problemDao.getProblemByID(porcID).getTitle());
            } else {
                discussBean.setTheme(contestDao.getContestByID(porcID).getTitle());
            }
        } else {
            String theme = request.getParameter("inputTheme");
            if (theme == null || theme.length() == 0) {
                discussBean.setTheme("oj");
            } else {
                discussBean.setTheme(theme);
            }
        }

        //无论什么样的消息, 都必须要设置内容
        String content = request.getParameter("inputContent");

        Integer userID = null;
        String userName = null;

        //从登录信息中提取用户发布用户的信息
        HttpSession session = request.getSession();
        userID = (Integer) session.getAttribute("userID");
        userName = (String) session.getAttribute("userName");

        discussBean.setType(type);
        discussBean.setContent(content);
        discussBean.setPostTime(new Date().getTime());
        discussBean.setUserID(userID);
        discussBean.setUserName(userName);
        discussBean.setReply(0);
        discussBean.setWatch(0);


        discussDao.insertDiscuss(discussBean);

        //设置该信息为原创的信息, 即不是回复别人的
        if (discussBean.getRootID() == discussBean.getDirectFID() && discussBean.getRootID() == 0) {
            discussDao.setAsRoot(discussBean);
        } else {
            discussDao.updateReply(discussBean.getRootID());//对于回复消息, 将楼主的回复数量增加1
        }

        return discussBean;

    }


    @GetMapping(value = "/post-original-discuss")
    public String getPostOriginalDiscussPage(HttpServletRequest request, HttpServletResponse response) {
        return "/discuss/discuss-edit";
    }


    @GetMapping(value = "/discuss-delete")
    public String deleteDiscuss(HttpServletRequest request, HttpServletResponse response) {
        String strPostID = request.getParameter("postID");
        Integer postID = strPostID != null && strPostID.length() > 0 ? Integer.parseInt(strPostID) : null;


        DiscussBean discussBean = discussDao.getDiscussByPostID(postID);
        if (discussBean.getPostID() == discussBean.getRootID() && discussBean.getRootID() == discussBean.getDirectFID()) {
            /*这是一条发布的消息, 删除记录本身, 再删除该记录下的所有回复*/
            discussDao.deleteDiscussByPostID(postID);/*删除记录本身*/
            discussDao.deleteDiscussByRootID(postID);/*删除该记录下的所有回复*/
            System.out.println("删除记录");
        } else {
            /*这是一条回复消息*/
            discussDao.deleteDiscussByPostID(postID);/*删除记录本身*/

            discussDao.updateReply(discussBean.getRootID());/*更新回复主题的回复数量*/
            System.out.println("删除回复");
        }

        return "redirect:" + request.getHeader("referer");
    }

    @GetMapping(value = "/discuss-list")
    public String discussListGet(HttpServletRequest request, ModelMap mp) {
        String strPage = request.getParameter("page");
        int page = strPage != null ? Integer.parseInt(strPage) : 1;

        String strType = request.getParameter("type");
        Integer type = strType != null && strType.length() > 0 ? Integer.parseInt(strType) : null;

        String strPorcID = request.getParameter("porcID");
        Integer porcID = strPorcID != null && strPorcID.length() > 0 ? Integer.parseInt(strPorcID) : null;

        String theme = request.getParameter("theme");


        List<DiscussBean> discussList = discussDao.getDiscussTitleList(type, porcID, theme, (page - 1) * Consts.COUNT_PER_PAGE, Consts.COUNT_PER_PAGE);
        int recordCount = discussDao.getCountOfTitleList(type, porcID, theme);


        //获取分页信息
        PageBean pageBean = Utils.getPagination(recordCount, page, request);

        mp.addAttribute("pagination", pageBean);
        mp.addAttribute("tableTitle", "讨论(" + recordCount + ")");
        mp.addAttribute("discussList", discussList);
        return "/discuss/discuss-list";
    }


    @GetMapping(value = "/discuss-set-first")
    public String discussSetFirst(HttpServletRequest request) {
        String strPostID = request.getParameter("postID");
        Integer postID = strPostID != null && strPostID.length() > 0 ? Integer.parseInt(strPostID) : null;

        String val = request.getParameter("val");

        if (postID != null) {
            if (val != null && val.equals("1")) {
                discussDao.setFirst(postID, 1);
            } else {
                discussDao.setFirst(postID, 0);
            }
        }

        return "redirect:" + request.getHeader("referer");
    }

    @GetMapping(value = "/discuss-detail")
    public String getDiscussDetail(HttpServletRequest request, ModelMap mp) {
        String strPostID = request.getParameter("postID");
        Integer postID = strPostID != null && strPostID.length() > 0 ? Integer.parseInt(strPostID) : null;
        DiscussBean discussBean = discussDao.getDiscussByPostID(postID);
        discussDao.addWatch(postID);
        List<DiscussBean> replyList = discussDao.getDiscussListByRootID(postID);

        mp.addAttribute("discuss", discussBean);
        mp.addAttribute("replyList", replyList);
        return "/discuss/discuss-reply";
    }
}
