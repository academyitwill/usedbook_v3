package com.lotu_us.usedbook.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class MemberController {

    //@GetMapping("/login")
    @RequestMapping(value = "/login", method = {RequestMethod.GET, RequestMethod.POST})
    public String loginForm(){
        return "member/login";
    }

    @GetMapping("/join")
    public String joinForm(){
        return "member/join";
    }

    @GetMapping("/joinOk")
    public String joinOk(){
        return "member/joinOk";
    }

    @GetMapping("/findPassword")
    public String findPassword(){
        return "member/findPassword";
    }

    @GetMapping("/findPasswordEmailSend")
    public String findPasswordEmailSend(){
        return "member/findPasswordEmailSend";
    }
}
