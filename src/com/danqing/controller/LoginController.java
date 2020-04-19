package com.danqing.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;

@Controller
@RequestMapping("")
public class LoginController {
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login( String name, String password) throws IOException {
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(name, password);
        try {
            subject.login(token);
            //shiro的这个session和httpsession是串通好了的
            Session session = subject.getSession();
            session.setAttribute("subject", subject);
            session.setAttribute("name",name);
            // attributes.addFlashAttribute(key,value); 将参数以post的方式传过去
            // @ModelAttribute("key") String key, //接收controller重定向传过来的参数（post）
            //attr.addFlashAttribute("name",name);
            return "redirect:index";

        } catch (AuthenticationException e) {

            return "login";
        }
    }

}
