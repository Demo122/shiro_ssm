package com.danqing.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.danqing.pojo.*;
import com.github.pagehelper.PageHelper;
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
        ResponseJSON res=new ResponseJSON();
        try {
            userService.delete(id);
            //设置返回状态码，1成功，2失败，-1没有权限，在shiro的过滤器里已经设置返回了
            res.setCode(ResponseStatusEnum.Do_SUCCESSFUL.getStatus());
            res.setMsg("删除成功");
        } catch (Exception e) {
            res.setCode(ResponseStatusEnum.Do_FAIELD.getStatus());
            res.setMsg("删除失败");
        }
        return JSONObject.toJSON(res).toString();
    }

    @ResponseBody
    @RequestMapping(value = "deleteSelectUser", method = RequestMethod.DELETE)
    public String deleteSelectUser(@RequestBody List<Long> ids) {

        ResponseJSON res=new ResponseJSON();
        try {
            //删除选中用户
            for (Long id : ids) {
                userService.delete(id);
            }
            res.setCode(ResponseStatusEnum.Do_SUCCESSFUL.getStatus());
            res.setMsg("删除成功");
        } catch (Exception e) {
            res.setCode(ResponseStatusEnum.Do_FAIELD.getStatus());
            res.setMsg("删除失败");
        }
        return JSONObject.toJSON(res).toString();
    }


    /**
     * 更新用户信息，包括 用户信息 和 用户角色信息
     * 使用实体类来接受json字符串参数，这里使用的时包装类
     * 这里传过来的json对象是嵌套了json对象的，jsonData={"user":data.field,"roleIds":roleIdsJSON};
     *  jsonData中的key "user" 对应包装类中的user ，其value也是json对象，它的字段和user的属性一一对应
     *  roleIds也是一样，只要这样才能注入
     * */
    @ResponseBody
    @RequestMapping(value = "updateUser",method =RequestMethod.POST)
    public String update(@RequestBody UserExt userExt) {
//        System.out.println(userExt.toString());
//        //使用map来接受多对象参数，或者使用包装类
        User user=userExt.getUser();

        long[] roleIds=userExt.getRoleIds().getIds();
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


    /**
     * @desceiption 更新选中的单个用户状态
     *
     * 接受客户端传来的jspn对象，其实就是from表单参数，key-value这种
     * 然后通过request.getParam(key)获取，用注解就是@RequestParam
     * @param status
     * @param id
     * @return
     *
     * 问题：@ResponseBody 不知为何响应消息类型是text/plain，
     * 解决：在注解@RequestMapping增加一个produces参数项即可 produces="application/json;charset=UTF-8"
     */
    @ResponseBody
    @RequestMapping(value ="updateUserStatus",method = RequestMethod.POST,produces="application/json;charset=UTF-8")
    public String updateUserStatus(@RequestParam Boolean status,@RequestParam long id){
//        System.out.println(status+"--"+id);
        ResponseJSON res=new ResponseJSON();
        User u=userService.get(id);
        u.setStatus(status);

        try{
            userService.update(u);
            res.setCode(ResponseStatusEnum.Do_SUCCESSFUL.getStatus());
            res.setMsg("update status successfuly!");
        }catch (Exception e){
            res.setCode(ResponseStatusEnum.Do_FAIELD.getStatus());
            res.setMsg("update status failed!");
        }

        return JSONObject.toJSON(res).toString();
    }


    /**
     * 更新选中的一组用户状态
     * 接受json字符串，使用fastjson来转换
     * @param str
     * @return
     */
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

        ResponseJSON res=new ResponseJSON();
        try{
            for (long id : ids) {
                User u=userService.get(id);
                u.setStatus(status);
                userService.update(u);
            }
            res.setCode(ResponseStatusEnum.Do_SUCCESSFUL.getStatus());
        }catch (Exception e){
            res.setCode(ResponseStatusEnum.Do_FAIELD.getStatus());
        }
        return JSONObject.toJSON(res).toString() ;
    }
}