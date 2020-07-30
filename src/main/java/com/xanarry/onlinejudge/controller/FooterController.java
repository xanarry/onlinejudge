package com.xanarry.onlinejudge.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class FooterController {
    @GetMapping(value = "/faq")
    public String getFaq() {
        return "/faq";
    }

    @GetMapping(value = "/about")
    public String getAbout() {
        return "/about";
    }

    @GetMapping(value = "/contact-me")
    public String getContactMe() {
        return "/contact-me";
    }

    @GetMapping(value = "/judge-server")
    public String getJudgeServer() {
        return "/judge-server";
    }
}
