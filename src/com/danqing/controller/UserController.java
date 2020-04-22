package com.danqing.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.danqing.pojo.*;
import com.danqing.service.RoleService;
import com.danqing.service.UserRoleService;
import com.danqing.service.UserService;
import com.github.pagehelper.PageHelper;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

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
            //1.删除用户
            userService.delete(id);
            //2.删除与该用户关联的角色
            userRoleService.deleteByUser(id);
            //设置返回状态码，1成功，2失败，-1没有权限，在shiro的过滤器里已经设置返回了
            res.setCode(ResponseStatusEnum.Do_SUCCESSFUL.getStatus());
            res.setMsg("删除成功");
        } catch (Exception e) {
            res.setCode(ResponseStatusEnum.Do_FAIELD.getStatus());
            res.setMsg("删除失败");
        }
        return JSONObject.toJSON(res).toString();
    }

    /**
     * 删除选中的用户
     * @param ids
     * @return
     */
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

        //返回json数据对象
        ResponseJSON res=new ResponseJSON();

        //1.获取浏览器传过来的user
        User user=userExt.getUser();
        //2.获取传来的 需要更改的的角色ID
        long[] roleIds=userExt.getRoleIds().getIds();
//
        System.out.println(user.toString()+"--"+roleIds.toString());

        //3.根据userId获取该用户在数据库中的信息
        User userInDB =userService.get(user.getId());

        //4.判断用户名是否修改
        // 如果修改了，需要检查下新的用户名是否和数据库中的其他用户名重复，要保证用户名的唯一性
        if (!user.getName().equals(userInDB.getName())){
            User hasSameNameUser=userService.getByName(user.getName());
            //如果数据库中已存在拥有该用户名的用户，则更新用户信息失败，返回失败信息
            if (null!=hasSameNameUser){
                res.setCode(ResponseStatusEnum.Do_FAIELD.getStatus());
                res.setMsg("更新失败，用户名已存在！");
                return JSONObject.toJSON(res).toString();
            }
        }

        //5.判断用户密码是否修改
        //获取数据库中的密码，
        String password = userInDB.getPassword();
        //如果不一样，说明密码修改了，重新生成盐和加密后的密码
        if (!user.getPassword().equals(password)) {
            String salt = new SecureRandomNumberGenerator().nextBytes().toString();
            int times = 2;
            String algorithmName = "md5";
            String encodedPassword = new SimpleHash(algorithmName, password, salt, times).toString();
            user.setSalt(salt);
            user.setPassword(encodedPassword);
        }

        //6.更新用户信息
        userService.update(user);

        //7.更新用户角色
        userRoleService.setRoles(user, roleIds);

        res.setCode(ResponseStatusEnum.Do_SUCCESSFUL.getStatus());
        res.setMsg("更新成功！");
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


    /**
     * 编辑用户信息页面 包括基本信息和角色信息
     * @param model
     * @param user
     * @return
     */
    @RequestMapping("editUserPage/{id}")
    public String editUserPage(Model model, User user) {
        User u = userService.get(user.getId());
        List<Role> roles = roleService.list();
        List<Role> userRoles = roleService.listRoles(user);
//        除去重复的role
        //在使用ArrayList时，当尝试用foreach或者Iterator遍历集合时进行删除或者插入元素的操作时，
        // 会抛出这样的异常：java.util.ConcurrentModificationException
        /** 解决办法  **/
        /**
         *   在使用迭代器遍历时，可使用迭代器的remove方法，因为Iterator的remove方法中 有如下的操作：
         expectedModCount = modCount；
         所以避免了ConcurrentModificationException的异常
         * */

        Iterator<Role> iterator = roles.iterator();
        while (iterator.hasNext()) {
            Role role = iterator.next();
            for (Role userRole : userRoles) {
                if (userRole.getName().equals(role.getName())) {
                    iterator.remove();
//                    System.out.println("删除："+role.getName());
                }
            }

        }

        model.addAttribute("user", u);
        model.addAttribute("roles", roles);
        model.addAttribute("userRoles", userRoles);
        return "editUser";
    }

    /**
     *  检查用户名是否重复，重复不能提交
     * @param username
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "checkUsername",method = RequestMethod.POST,produces="application/json;charset=UTF-8")
    public String checkUsername(String username){
        System.out.println(username);
        ResponseJSON res=new ResponseJSON();
        User u=userService.getByName(username);
        if (u!=null){
            //不空，说明有这个用户名的用户
            res.setCode(ResponseStatusEnum.Do_FAIELD.getStatus());
            res.setMsg("用户名已存在！");
        }else {
            res.setCode(ResponseStatusEnum.Do_SUCCESSFUL.getStatus());
            res.setMsg("用户名可用！");
        }

        return JSONObject.toJSON(res).toString();
    }
}