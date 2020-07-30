package com.xanarry.onlinejudge.controller;


import com.xanarry.onlinejudge.OjConfiguration;
import com.xanarry.onlinejudge.controller.beans.MessageBean;
import com.xanarry.onlinejudge.controller.beans.PageBean;
import com.xanarry.onlinejudge.dao.ProblemDao;
import com.xanarry.onlinejudge.judge.JudgeClient;
import com.xanarry.onlinejudge.model.ProblemBean;
import com.xanarry.onlinejudge.utils.Consts;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * Created by xanarry on 18-1-4.
 */
@Controller
public class ProblemController {
    @Autowired
    OjConfiguration configuration;

    @Autowired
    public ProblemDao problemDao;

    @GetMapping(value = "/problem-add")
    public String getProblemAddPage() {
        return "/problem/problem-edit";
    }

    @GetMapping(value = "/problem-edit")
    public String getProblemEditPage(@RequestParam(value = "problemID", defaultValue = "-1") Integer problemID, ModelMap mp) {
        if (problemID != -1) {
            ProblemBean problemBean = problemDao.getProblemByID(problemID);
            mp.addAttribute("problem", problemBean);
            return "problem/problem-edit";
        } else {
            return "redirect:/problem-list";
        }
    }


    @PostMapping(value = "/ajax-check-problem-exist", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String checkProblemExist(@RequestParam(value = "problemID", defaultValue = "-1") Integer problemID) {
        String jsonPattern = "{\"problemID\": %s, \"exist\": %s}";
        String json = "{\"problemID\": " + problemID + ", \"exist\": false}";
        if (problemDao.getProblemByID(problemID - 1000) != null) {
            json = String.format(jsonPattern, problemID, true);
        }
        System.out.println(json);
        return json;
    }

    @PostMapping(value = "/problem-add")
    public String addProblem(ProblemBean problemBean) {
        problemBean.setCreateTime(new Date().getTime());
        System.out.println("add problem: " + problemBean);
        problemDao.addProblem(problemBean);
        return "redirect:/test-point-list?problemID=" + problemBean.getProblemID();
    }

    @PostMapping(value = "/problem-update")
    public String updateProblem(ProblemBean problemBean) {
        problemBean.setCreateTime(new Date().getTime());
        System.out.println("update problem: " + problemBean);
        problemDao.updateProblemByID(problemBean);
        return "redirect:/problem?problemID=" + problemBean.getProblemID();
    }

    @GetMapping(value = "/problem-delete")
    public String problemDeleteGet(@RequestParam(value = "problemID", defaultValue = "-1") Integer problemID, RedirectAttributes attrs) {
        MessageBean messageBean = new MessageBean("错误", "错误", "题目参数不正确!", "/problem-list", "返回题目列表");
        if (problemID != -1) {
            problemDao.deleteProblemByID(problemID);
            //todo 修改测试点路径
            String testPointSavePath = configuration.getTestPointHome() + "/" + (1000 + problemID);
            //删除测试点文件
            try {
                FileUtils.deleteDirectory(new File(testPointSavePath));
            } catch (IOException e) {
                e.printStackTrace();
            }
            messageBean = new MessageBean("信息", "信息", "题目已经成功删除!", "/problem-list", "返回题目列表");
        }

        return "redirect:" + Utils.getErrorPageURL(messageBean);
    }


    @GetMapping(value = "/problem-list")
    public String getProblems(@RequestParam(value = "page", defaultValue = "1") Integer page,
                              HttpSession session,
                              HttpServletRequest request, ModelMap mp) {
        List<ProblemBean> problemBeanList;
        Integer userID = (Integer) session.getAttribute("userID");
        if (userID != null) {
            problemBeanList = problemDao.getProblesOrderByIDForLogin(
                    userID,
                    JudgeClient.ACCEPTED,
                    (page - 1) * Consts.COUNT_PER_PAGE,
                    Consts.COUNT_PER_PAGE);
        } else {
            problemBeanList = problemDao.getProblemsOrderByID((page - 1) * Consts.COUNT_PER_PAGE, Consts.COUNT_PER_PAGE);
        }

        int recordCount = problemDao.getCount();
        //获取分页信息
        PageBean pageBean = Utils.getPagination(recordCount, page, request);


        mp.addAttribute("tableTitle", "题目列表(" + recordCount + ")");
        mp.addAttribute("pagination", pageBean);
        mp.addAttribute("problemList", problemBeanList);
        return "/problem/problem-list";
    }


    @GetMapping(value = "/problem-search")
    public String searchProblem(@RequestParam(value = "inputProblemKeyword", defaultValue = "") String problemKeyword,
                                @RequestParam(value = "page", defaultValue = "1") Integer page, ModelMap mp, HttpServletRequest request) {
        boolean isDigital = true;
        for (char c : problemKeyword.toCharArray()) {
            if (!Character.isDigit(c)) {
                isDigital = false;
                break;
            }
        }

        int problemID = isDigital ? Integer.parseInt(problemKeyword) : 0;

        List<ProblemBean> problemBeanList = problemDao.searchProblem(problemID, problemKeyword, (page - 1) * Consts.COUNT_PER_PAGE, Consts.COUNT_PER_PAGE);
        int recordCount = problemDao.getSearchResultCount(problemID, problemKeyword);
        //获取分页信息
        PageBean pageBean = Utils.getPagination(recordCount, page, request);


        mp.addAttribute("tableTitle", "搜索结果(" + recordCount + ")");
        mp.addAttribute("pagination", pageBean);
        mp.addAttribute("problemList", problemBeanList);
        return "problem/problem-list";
    }


    @GetMapping(value = "/problem")
    public String showProblem(@RequestParam(value = "problemID", defaultValue = "-1") Integer problemID,
                              HttpServletRequest request,
                              ModelMap mp,
                              RedirectAttributes attrs) {
        if (problemID != null) {
            ProblemBean problemBean = problemDao.getProblemByID(problemID);
            mp.addAttribute("problem", problemBean);
            return "/problem/problem";
        } else {
            String referer = request.getHeader("referer");
            MessageBean messageBean = new MessageBean("错误", "错误", "题目参数不正确!", referer != null ? referer : "/problem-list", "返回题目列表");
            attrs.addFlashAttribute("message", messageBean);
            return "redirect:/error";
        }
    }

}

