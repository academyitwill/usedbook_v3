package com.lotu_us.usedbook.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MemberController {

    @GetMapping("/login")
    public String loginForm(){
        return "login";
    }

    @GetMapping("/join")
    public String joinForm(){
        return "join";
    }
}
