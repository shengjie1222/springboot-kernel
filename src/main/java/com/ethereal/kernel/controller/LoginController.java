package com.ethereal.kernel.controller;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Jie Jie
 * date 2024-03-21
 */
@Controller
@Slf4j
@RequestMapping
public class LoginController {


    @Value("${web.socket.port}")
    private int socketPort;

    @ApiOperation(value = "跳转到登录页面",httpMethod="GET")
    @RequestMapping("/toLogin")
    public String toLogin() {
        return "login";
    }

    @ApiOperation(value = "执行登录",notes = "成功后跳转到主页面",httpMethod="GET")
    @RequestMapping("/doLogin")
    public String doLogin() {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("当前登陆用户：" + name);
        return "login";
    }

    @ApiOperation(value = "跳转到主页面(传输状态页)",httpMethod="GET")
    @RequestMapping("/index")
    public String transferIndex(Model model, HttpServletRequest request) {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        model.addAttribute("socketPort", socketPort);
        log.info("当前登陆用户：" + name);
        return "index";
    }
}
