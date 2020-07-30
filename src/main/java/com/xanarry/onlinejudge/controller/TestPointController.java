package com.xanarry.onlinejudge.controller;


import com.xanarry.onlinejudge.OjConfiguration;
import com.xanarry.onlinejudge.dao.ProblemDao;
import com.xanarry.onlinejudge.dao.TestPointDao;
import com.xanarry.onlinejudge.model.ProblemBean;
import com.xanarry.onlinejudge.model.TestPointBean;
import com.xanarry.onlinejudge.utils.Tools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by xanarry on 18-1-6.
 */

@Controller
public class TestPointController {
    @Autowired
    private OjConfiguration configuration;

    @Autowired
    private TestPointDao testPointDao;

    @Autowired
    private ProblemDao problemDao;


    @PostMapping(value = "/add-test-point")
    public String addTestPoint(HttpServletRequest request) {
        String strProblemID = request.getParameter("inputProblemID");
        String inputData = request.getParameter("inputInputData");
        String outputData = request.getParameter("inputOutputData");

        Integer problemID = Integer.parseInt(strProblemID);

        //设置文件保存路径, 网站根目录/test-point/题目ID/[1.in|1.out]
        String testPointSavePath = configuration.getTestPointHome() + "/" + (1000 + problemID);

        System.out.println("testPointSavePath: " + testPointSavePath);

        int testPointID = Tools.saveTestPoint(testPointSavePath, inputData, outputData);
        if (testPointID == -1) {
            System.out.println("测试点文件处理出错");
        }

        TestPointBean testPointBean = new TestPointBean();
        //设置题目ID
        testPointBean.setProblemID(Integer.parseInt(strProblemID));
        //设置测试点编号
        testPointBean.setTestPointID(testPointID);
        //设置输入文本路径
        testPointBean.setInputTextPath((1000 + problemID) + "/input/" + testPointID + ".in");
        //设置输入文本长度
        testPointBean.setInputTextLength(inputData.length());
        //设置输出文本路径
        testPointBean.setOutputTextPath((1000 + problemID) + "/output/" + testPointID + ".out");
        //设置输出文本长度
        testPointBean.setOutputTextLength(outputData.length());

        System.out.println("add new test point: " + testPointBean);

        testPointDao.addTestPoint(testPointBean);
        return "redirect:/test-point-list?problemID=" + problemID;
    }

    @GetMapping(value = "/delete-test-point")
    public String deleteTestPoint(HttpServletRequest request) {
        String strProblemID = request.getParameter("problemID");
        String strTestPointID = request.getParameter("testPointID");

        Integer problemID = Integer.parseInt(strProblemID);
        Integer testPointID = Integer.parseInt(strTestPointID);

        String testPointSavePath = configuration.getTestPointHome() + "/" + (1000 + problemID);

        //删除文件
        if (Tools.deleteTestPoint(testPointSavePath, testPointID)) {
            //从数据库删除记录
            testPointDao.deleteTestPoint(problemID, testPointID);
        } else {
            System.out.println(String.format("题目: %d, 测试点: %d, 删除失败", problemID, testPointID));
        }

        return "redirect:/test-point-list?problemID=" + problemID;
    }


    @GetMapping(value = "/test-point-list")
    public String getTestPointList(HttpServletRequest request) {
        String strProblemID = request.getParameter("problemID");
        Integer problemID = Integer.parseInt(strProblemID);

        System.out.println("get problemID: " + strProblemID + " testpointList");

        List<TestPointBean> testPoints = testPointDao.getTestPointList(problemID);
        ProblemBean problemBean = problemDao.getProblemByID(problemID);


        request.setAttribute("testPointList", testPoints);
        request.setAttribute("problem", problemBean);
        return "/problem/test-point-list";
    }


    @GetMapping(value = "/show-test-point")
    public String getTestPoint(HttpServletRequest request) {
        String strProblemID = request.getParameter("problemID");
        String strTestPointID = request.getParameter("testPointID");


        Integer problemID = Integer.parseInt(strProblemID);
        Integer testPointID = Integer.parseInt(strTestPointID);


        ProblemBean problemBean = problemDao.getProblemByID(problemID);


        TestPointBean testPointBean = testPointDao.getTestPoint(problemID, testPointID);

        String inputText = Tools.readFileToString(configuration.getTestPointHome() + "/" + testPointBean.getInputTextPath());
        String outputText = Tools.readFileToString(configuration.getTestPointHome() + "/" + testPointBean.getOutputTextPath());

        request.setAttribute("testPoint", testPointBean);
        request.setAttribute("problem", problemBean);
        request.setAttribute("inputText", inputText);
        request.setAttribute("outputText", outputText);
        return "/problem/test-point";
    }


}
