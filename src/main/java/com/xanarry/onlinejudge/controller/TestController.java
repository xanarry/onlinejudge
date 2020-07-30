package com.xanarry.onlinejudge.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Arrays;

@Controller
public class TestController {
    @GetMapping(value = "/test")
    String getTest(ModelMap mp) {

        mp.addAttribute("list", Arrays.asList(100, 100, 98, 90, 90, 90, 89, 78, 76, 45, 45, 45));
        return "test";
    }
}
