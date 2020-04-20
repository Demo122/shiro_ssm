package com.danqing.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.github.pagehelper.PageHelper;
import com.danqing.pojo.Role;
import com.danqing.pojo.User;
import com.danqing.pojo.UserExt;
import com.danqing.service.RoleService;
import com.danqing.service.UserRoleService;
import com.danqing.service.UserService;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("user")
public class UserController {
    @Autowired
    UserRoleService userRoleService;
    @Autowired
    UserService userService;
    @Autowired
    RoleService roleService;

    @ResponseBody
    @RequestMapping("listUser")
    public String list(@RequestParam(value = "page",defaultValue = "1") int page,@RequestParam(value = "nums",defaultValue = "10") int nums) {
//        System.out.println(page+"---"+nums);
        //使用pageHelper分页
        PageHelper.startPage(page, nums);
        List<User> us = userService.list();
        //获取用户总数
        int count=userService.getTotal();
        Map<String, Object> res = new HashMap<>();
        res.put("code", 0);
        res.put("msg", "");
        res.put("count", count);
        res.put("data", us);
        //使用jsp的方式
//        model.addAttribute("us", us);
//        Map<User, List<Role>> user_roles = new HashMap<>();
//        for (User user : us) {
//            List<Role> roles = roleService.listRoles(user);
//            user_roles.put(user, roles);
//        }
//        model.addAttribute("user_roles", user_roles);

//        return "listUser";
        return JSONObject.toJSON(res).toString();
    }

    @RequestMapping("editUser")
    public String edit(Model model, long id) {
        List<Role> rs = roleService.list();
        model.addAttribute("rs", rs);
        User user = userService.get(id);
        model.addAttribute("user", user);

        List<Role> roles = roleService.listRoles(user);
        model.addAttribute("currentRoles", roles);

        return "editUser";
    }

    @ResponseBody
    @RequestMapping(value = "deleteUser", method = RequestMethod.DELETE)
    public String delete(@RequestBody long id) {
//        System.out.println(id);
        Map<String, Object> res = new HashMap<>();
        try {
            userService.delete(id);
            res.put("code", 1);
        } catch (Exception e) {
            res.put("code", 2);
        }
        return JSONObject.toJSON(res).toString();
    }

    @ResponseBody
    @RequestMapping(value = "deleteSelectUser", method = RequestMethod.DELETE)
    public String deleteSelectUser(@RequestBody List<Long> ids) {

        Map<String, Object> res = new HashMap<>();
        try {
            //删除选中用户
            for (Long id : ids) {
                userService.delete(id);
            }
            res.put("code", true);
        } catch (Exception e) {
            res.put("code", false);
        }
        return JSONObject.toJSON(res).toString();
    }

    @ResponseBody
    @RequestMapping(value = "updateUser",method =RequestMethod.POST)
    public String update(@RequestBody UserExt userExt) {
//        System.out.println(userExt.toString());
//        //使用map来接受多对象参数，或者使用包装类
        User user=userExt.getUser();

        long[] roleIds=userExt.getRoleIds().getRoleIds();
//
        System.out.println(user.toString()+"--"+roleIds.toString());
        //设置用户角色
        userRoleService.setRoles(user, roleIds);

        //获取数据库中的密码，
        String password = userService.get(user.getId()).getPassword();
        //如果不一样，说明密码修改了，重新生成盐和加密后的密码
        if (!user.getPassword().equals(password)) {
            String salt = new SecureRandomNumberGenerator().nextBytes().toString();
            int times = 2;
            String algorithmName = "md5";
            String encodedPassword = new SimpleHash(algorithmName, password, salt, times).toString();
            user.setSalt(salt);
            user.setPassword(encodedPassword);
        }
        // 如果传过来的密码和数据中的一样，那就没有修改密码

        userService.update(user);
        Map<String,Object> res=new HashMap<>();
        res.put("msg","更新成功！");

        return JSONObject.toJSON(res).toString();

    }

    @RequestMapping("addUserPage")
    public String addUserPage() {
        return "addUser";
    }

    @ResponseBody
    @RequestMapping(value = "addUser", method = RequestMethod.POST)
    public String add(@RequestBody User user) {

//        System.out.println(user.toString());
        String salt = new SecureRandomNumberGenerator().nextBytes().toString();
        int times = 2;
        String algorithmName = "md5";

        String encodedPassword = new SimpleHash(algorithmName, user.getPassword(), salt, times).toString();

        user.setPassword(encodedPassword);
        user.setSalt(salt);


        Map<String, Object> res = new HashMap<>();
        try {
            userService.add(user);

            res.put("msg", "添加成功");
        } catch (Exception e) {

            res.put("msg", "添加失败");
        }

        return JSONObject.toJSON(res).toString();
    }

    @ResponseBody
    @RequestMapping(value = "getUserRoles/{id}",method = RequestMethod.GET)
    public String getUserRoles(@PathVariable long id){
        User user = userService.get(id);
        List<Role> roles = roleService.listRoles(user);
        List<String> roleName=new ArrayList<>();
        //只返回权限名字，描述 desc_
        for (Role role : roles) {
            roleName.add(role.getDesc_());
        }
        if(roleName.isEmpty()){
            roleName.add("无");
        }
//        Map<String,Object> res=new HashMap<>();
//        res.put("roles",roleName);
        return JSONObject.toJSON(roleName).toString();
    }


    @ResponseBody
    @RequestMapping(value ="updateUser",method = RequestMethod.GET)
    public String updateUserStatus(@RequestParam Boolean status,@RequestParam long id){
//        System.out.println(status+"--"+id);
        User u=userService.get(id);
        u.setStatus(status);
        userService.update(u);
        Map<String,Object> res=new HashMap<>();
        res.put("msg","update successfuly!");
        return JSONObject.toJSON(res).toString();
    }

    @ResponseBody
    @RequestMapping(value ="updateSelectUserStatus",method = RequestMethod.POST)
    public String updateSelectUserStatus(@RequestBody String str){
//        System.out.println("--"+ids.toString());

        //使用fastjson 将json string转对象
        JSONObject jsonObject=JSONObject.parseObject(str);
        Boolean status=jsonObject.getBoolean("status");
//        Boolean status= JSON.parseObject(String.valueOf(status1),Boolean.class);
        JSONArray ids1=jsonObject.getJSONArray("ids");
        List<Long> ids=JSON.parseObject(ids1.toString(),new TypeReference<List<Long>>(){});

        System.out.println(status+"---"+ids.toString());

        Map<String,Object> map=new HashMap<>();
        try{
            for (long id : ids) {
                User u=userService.get(id);
                u.setStatus(status);
                userService.update(u);
            }
           map.put("code",true);
        }catch (Exception e){
            map.put("code",false);
        }
        return JSONObject.toJSON(map).toString() ;
    }
}