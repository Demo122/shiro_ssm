package com.danqing.controller;

import com.alibaba.fastjson.JSONObject;
import com.danqing.pojo.ResponseJSON;
import com.danqing.pojo.ResponseStatusEnum;
import com.danqing.pojo.User;
import com.danqing.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

@Controller
@RequestMapping("")
public class LoginController {
    @Autowired
    UserService userService;

    /**
     *   ajax请求时不能再后台重定向的
     *      支持 用户名和email登录
     * @param user
     * @return
     * @throws IOException
     */
    @ResponseBody
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(@RequestBody User user) throws IOException {

//        System.out.println(user.toString());
        //创建返回json的对象
        ResponseJSON res = new ResponseJSON();

        //1.先根据name查找到status,判断该账号是否启用
        //如果没有启用,转发到error方法，返回json数据提示
        try{
            if (!isEnable(user.getName())) {
                //设置被禁用的状态码
                res.setCode(ResponseStatusEnum.NOT_ENABLE.getStatus());
                res.setMsg("该账户被禁用了，请联系管理员");
                return JSONObject.toJSON(res).toString();
            }
        }catch (UnknownAccountException e){
            res.setCode(ResponseStatusEnum.No_ACCOUNT.getStatus());
            res.setMsg(e.getMessage());
            return JSONObject.toJSON(res).toString();
        }


        //如果启用了在进行认证操作
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(user.getName(), user.getPassword());
        try {
            subject.login(token);
            //shiro的这个session和httpsession是串通好了的
            Session session = subject.getSession();
            //获取认证后存在session中的user
            User u= (User) session.getAttribute("user");

            session.setAttribute("name", u.getName());

            // attributes.addFlashAttribute(key,value); 将参数以post的方式传过去
            // @ModelAttribute("key") String key, //接收controller重定向传过来的参数（post）
            //attr.addFlashAttribute("name",name);

            //设置登录成功的json数据
            res.setCode(ResponseStatusEnum.LOGIN_SUCCESS.getStatus());
            res.setUrl("index");
            return JSONObject.toJSON(res).toString();

        } catch (AuthenticationException e) {
            //设置登录失败的json数据
            res.setCode(ResponseStatusEnum.LOGIN_FAIL.getStatus());
            res.setUrl("login");
            res.setMsg("登录失败,用户名或密码错误");
            return JSONObject.toJSON(res).toString();
        }
    }


    /**
     * 返回用户的status状态
     *
     * @param username
     * @return
     */
    public Boolean isEnable(String username) {

        User byUserName = userService.getByName(username);
        //通过用户名找不到
        if (byUserName==null){
            //通过email找
            User byUserEmail=userService.getByEmail(username);
            if (byUserEmail==null){
                throw new UnknownAccountException("账号不存在");
            }else {
                return byUserEmail.getStatus();
            }
        }else {
            return byUserName.getStatus();
        }
    }

}
