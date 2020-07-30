package com.xanarry.onlinejudge.controller;


import com.xanarry.onlinejudge.controller.beans.MessageBean;
import com.xanarry.onlinejudge.controller.beans.PageBean;
import com.xanarry.onlinejudge.dao.*;
import com.xanarry.onlinejudge.judge.JudgeClient;
import com.xanarry.onlinejudge.model.*;
import com.xanarry.onlinejudge.utils.Consts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

/**
 * Created by xanarry on 18-1-7.
 */

@Controller
public class SubmitController {
    @Autowired
    private UserDao userDao;
    @Autowired
    private LanguageDao languageDao;
    @Autowired
    private ProblemDao problemDao;
    @Autowired
    private SubmitRecordDao submitRecordDao;
    @Autowired
    private ViewSubmitRecordDao viewSubmitRecordDao;
    @Autowired
    private JudgeDetailDao judgeDetailDao;
    @Autowired
    private CompileInfoDao compileInfoDao;
    @Autowired
    private SystemErrorDao systemErrorDao;


    @GetMapping(value = "/submit")
    public String getSubmit(HttpSession session,
                            //非比赛提交的代码比赛id设置为0，比赛的就大于1
                            @RequestParam(value = "contestID", defaultValue = "0") final Integer contestID,
                            @RequestParam(value = "problemID", defaultValue = "-1") final Integer problemID,
                            ModelMap mp) {

        //检查用户是否登录, 没有登录重定向到错误页面
        if (session.getAttribute("userID") == null) {
            MessageBean messageBean = new MessageBean("提示", "提示", "请登录再提交代码", "/", "回到首页");
            return "redirect:" + Utils.getErrorPageURL(messageBean);
        }

        Integer userID = (Integer) session.getAttribute("userID");

        //获取提交题目信息
        ProblemBean problemBean = problemDao.getProblemByID(problemID);

        //获取系统语言列表以供选择
        List<LanguageBean> languages = languageDao.getLanguageList();

        //获取提交代码的用户信息
        UserBean userBean = userDao.getUserByID(userID);


        //获取用户在之前对本题目的提交记录
        List<SubmitRecordBean> submitRecordBeans = submitRecordDao.getSubmitRecordList(0, problemID, userID, null, null, 0, 3);

        mp.addAttribute("problem", problemBean);
        mp.addAttribute("languages", languages);
        mp.addAttribute("user", userBean);
        mp.addAttribute("recordList", submitRecordBeans);
        return "/submittion/submit";
    }


    @PostMapping(value = "/submit")
    public String postSubmit(HttpSession session,
                             //非比赛提交的代码比赛id设置为0，比赛的就大于1
                             @RequestParam(value = "inputContestID", defaultValue = "0") final Integer contestID,
                             @RequestParam(value = "inputProblemID", defaultValue = "-1") final Integer problemID,
                             @RequestParam(value = "inputCode", defaultValue = "-1") final String code,
                             @RequestParam(value = "inputLanguage", defaultValue = "-1") final String language,
                             ModelMap mp) {

        //检查用户是否登录, 没有登录重定向到错误页面
        if (session.getAttribute("userID") == null) {
            MessageBean messageBean = new MessageBean("提示", "提示", "请登录再提交代码", "/", "回到首页");
            return "redirect:" + Utils.getErrorPageURL(messageBean);
        }
        Integer userID = (Integer) session.getAttribute("userID");


        SubmitRecordBean submitRecord = new SubmitRecordBean();//生成提交记录
        submitRecord.setProblemID(problemID);
        submitRecord.setUserID(userID);
        submitRecord.setContestID(contestID);//非比赛提交的代码统一设置为0
        submitRecord.setResult(JudgeClient.QUEUING);
        submitRecord.setLanguage(language);
        submitRecord.setSourceCode(code);
        submitRecord.setCodeLength(code.length());
        submitRecord.setSubmitTime(new Date().getTime());
        submitRecord.setJudgeTime(0L);

        //提交数据库
        submitRecordDao.addSubmitRecord(submitRecord);

        //提交代码, 任何与提交代码到评测机的相关的代码都必须在记录写入数据库之后, 后续状态与结果的更新由,judge client完成
        //网页需要手动刷新才能看到更新
        // Todo 发送任务到服务器
        JudgeClient.getInstance().sumbitJudgeTask(submitRecord);
        if (contestID != 0) {
            return "redirect:/contest-record-list?contestID=" + contestID;
        } else {
            return "redirect:/record-list";
        }
    }

    @GetMapping(value = "/rejudge")
    public String rejudgeGet(@RequestParam(value = "submitID", defaultValue = "-1") Integer submitID,
                             @RequestHeader(value = "referer", required = false) final String referer) {
        if (submitID == -1) {
            return "redirct:" + Utils.getErrorPageURL(new MessageBean("错误", "错误", "不存在的提交ID", referer, "返回提交记录"));
        }

        /*获取提交信息*/
        SubmitRecordBean submitRecord = submitRecordDao.getSubmitRecordByID(submitID);

        /*删除当前记录在数据库中的信息, 触发器会删除相关的judge-detail, compile-info*/
        submitRecordDao.deleteSubmitRecord(submitID);

        /*重置提交中需要更新的信息*/
        submitRecord.setSubmitTime(new Date().getTime());
        submitRecord.setResult(JudgeClient.QUEUING);/*重置评测结果*/
        submitRecord.setJudgeTime(0L);

        //提交数据库
        submitRecordDao.addSubmitRecord(submitRecord);
        // Todo 发送任务到服务器
        JudgeClient.getInstance().sumbitJudgeTask(submitRecord);

        if (submitRecord.getContestID() != 0) {
            return "redirect:/contest-record-list?contestID=" + submitRecord.getContestID();
        } else {
            return "redirect:/record-list";
        }
    }

    @GetMapping(value = "/record-list")
    public String getRecordList(HttpServletRequest request, ModelMap mp, HttpServletResponse response) {
        String strPage = request.getParameter("page");
        int page = strPage != null ? Integer.parseInt(strPage) : 1;

        String userName = request.getParameter("userName");
        String strProblemID = request.getParameter("problemID");
        String result = request.getParameter("result");
        String language = request.getParameter("language");

        userName = userName != null && userName.length() > 0 ? userName : null;
        Integer problemID = strProblemID != null && strProblemID.length() > 0 ? Integer.parseInt(strProblemID) - 1000 : null;
        result = result != null && result.length() > 0 ? result : null;
        language = language != null && language.length() > 0 ? language : null;


        List<ViewSubmitRecordBean> submitRecordBeans = viewSubmitRecordDao.getSubmitRecordList(0, problemID, userName, result, language, (page - 1) * Consts.COUNT_PER_PAGE, Consts.COUNT_PER_PAGE);

        //获取分页信息
        int recordCount = viewSubmitRecordDao.getCountOnCondition(0, problemID, userName, result, language);
        PageBean pageBean = Utils.getPagination(recordCount, page, request);


        mp.addAttribute("recordList", submitRecordBeans);
        mp.addAttribute("pagination", pageBean);
        return "/submittion/record-list";
    }


    @GetMapping(value = "/judge-detail")
    public String getRecordDetail(HttpServletRequest request, ModelMap mp, HttpServletResponse response) {
        String strSubmitID = request.getParameter("submitID");
        if (strSubmitID == null) {
            return "redirct:" + new MessageBean("错误", "错误", "不存在的提交ID", request.getHeader("referer"), "返回提交记录");
        }

        Integer submitID = Integer.parseInt(strSubmitID);

        List<JudgeDetailBean> judgeDetailList = judgeDetailDao.getJudegeDetailBySubmitID(submitID);

        SubmitRecordBean submitRecordBean = submitRecordDao.getSubmitRecordByID(submitID);

        CompileInfoBean compileInfoBean = compileInfoDao.getCompileResult(submitID);
        if (compileInfoBean == null) {
            compileInfoBean = new CompileInfoBean(submitID, "");
        }

        SystemErrorBean systemErrorBean = systemErrorDao.getSystemErrorMessage(submitID);

        mp.addAttribute("detailList", judgeDetailList);
        mp.addAttribute("systemError", systemErrorBean);
        mp.addAttribute("record", submitRecordBean);
        mp.addAttribute("compileInfo", compileInfoBean);
        return "/submittion/judge-detail";
    }
}