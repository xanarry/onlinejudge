package com.xanarry.onlinejudge.controller;

import com.xanarry.onlinejudge.dao.ContestDao;
import com.xanarry.onlinejudge.dao.DiscussDao;
import com.xanarry.onlinejudge.model.ContestBean;
import com.xanarry.onlinejudge.model.DiscussBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@Controller
public class IndexController {
    @Autowired
    private ContestDao contest;

    @Autowired
    private DiscussDao discuss;

    @RequestMapping(value = {"/", "/index"})
    public String getIndexPage(HttpServletRequest request, ModelMap mp) throws ServletException, IOException {
        List<ContestBean> contestBeanList = contest.getContestList(0, 5);
        List<DiscussBean> discussBeanList = discuss.getDiscussTitleList(null, null, null, 0, 5);

        HttpSession session = request.getSession();
        session.setAttribute("userID", 1);
        session.setAttribute("userName", "xanarry");
        session.setAttribute("userType", 2);

        mp.addAttribute("latestContest", contestBeanList);
        mp.addAttribute("latestDiscuss", discussBeanList);
        return "index";
    }
}
