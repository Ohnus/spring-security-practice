package com.example.security7_practice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    // 로그인 페이지 제공
    @GetMapping("/user/login")
    public String login(){
        return "login";
    }
}
