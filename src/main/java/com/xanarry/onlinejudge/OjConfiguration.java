package com.xanarry.onlinejudge;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class OjConfiguration {
    @Value("${oj.image-home}")
    private String imageHome;

    @Value("${oj.test-point-home}")
    private String testPointHome;

    @Value("${oj.host}")
    private String judgeServerIP;

    @Value("${oj.port}")
    private Integer judgeServerPort;

    public String getImageHome() {
        return imageHome;
    }

    public void setImageHome(String imageHome) {
        this.imageHome = imageHome;
    }

    public String getTestPointHome() {
        return testPointHome;
    }

    public void setTestPointHome(String testPointHome) {
        this.testPointHome = testPointHome;
    }

    public String getJudgeServerIP() {
        return judgeServerIP;
    }

    public void setJudgeServerIP(String judgeServerIP) {
        this.judgeServerIP = judgeServerIP;
    }

    public Integer getJudgeServerPort() {
        return judgeServerPort;
    }

    public void setJudgeServerPort(Integer judgeServerPort) {
        this.judgeServerPort = judgeServerPort;
    }

    @Override
    public String toString() {
        return "imageHome='" + imageHome + '\'' +
                "\ntestPointHome='" + testPointHome + '\'' +
                "\njudgeServerIP='" + judgeServerIP + '\'' +
                "\njudgeServerPort=" + judgeServerPort;
    }
}
