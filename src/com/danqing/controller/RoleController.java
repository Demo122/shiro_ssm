package com.danqing.controller;

import com.alibaba.fastjson.JSONObject;
import com.danqing.pojo.*;
import com.danqing.service.PermissionService;
import com.danqing.service.RolePermissionService;
import com.danqing.service.RoleService;
import com.danqing.service.UserRoleService;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("role")
public class RoleController {
    @Autowired
    RoleService roleService;
    @Autowired
    RolePermissionService rolePermissionService;
    @Autowired
    PermissionService permissionService;

    @Autowired
    UserRoleService userRoleService;


    @ResponseBody
    @RequestMapping("listRole")
    public String list(@RequestParam(value = "page", defaultValue = "1") int page, @RequestParam(value = "nums", defaultValue = "10") int nums) {
        //使用pageHelper分页
        PageHelper.startPage(page, nums);
        List<Role> roles = roleService.list();
        //获取角色总数
        int count = roleService.getTotal();

        Map<String, Object> response = new HashMap<>();
        response.put("code", 0);
        response.put("msg", "");
        response.put("count", count);
        response.put("data", roles);
        return JSONObject.toJSON(response).toString();
    }

    @RequestMapping("addRolePage")
    public String addRolePage(){
        return "addRole";
    }

    /**
     * 添加角色
     * @param role
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "addRole",method = RequestMethod.POST)
    public String addRole(@RequestBody Role role){

        ResponseJSON res=new ResponseJSON();
        try {
            //1.将新角色存入数据库
            roleService.add(role);
            res.setCode(ResponseStatusEnum.Do_SUCCESSFUL.getStatus());
            res.setMsg("添加成功");
        } catch (Exception e) {
            res.setCode(ResponseStatusEnum.Do_FAIELD.getStatus());
            res.setMsg("添加失败");
        }

        return JSONObject.toJSON(res).toString();
    }

    //删除角色
    @ResponseBody
    @RequestMapping(value = "deleteRole", method = RequestMethod.DELETE)
    public Map delete(@RequestBody long id) {
        Map<String, Object> res = new HashMap<>();
        try {
            //1.删除角色表中的角色
            roleService.delete(id);
            //2.删除与角色表关联的user_role 表中的 记录
            userRoleService.deleteByRole(id);
            //3.删除于角色表关联的role_permission 表中的记录
            rolePermissionService.deleteByRole(id);
            res.put("msg", "删除角色成功！");

        } catch (Exception e) {
            res.put("msg", "删除角色失败！");
        }
        return res;
    }

    /**
     * 编辑角色页面
     * @param model
     * @param role
     * @return
     */
    @RequestMapping("editRolePage")
    public String editRolePage(Model model,Role role) {
        Role r = roleService.get(role.getId());

        model.addAttribute("role",r);

        return "editRole";
    }

    /**
     * 请求要授权角色的信息
     * @param role
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "editRolePermission",method = RequestMethod.POST)
    public Map editRolePermission(@RequestBody Role role){
        Map<String,Object> res=new HashMap<>();

        //1.查找所有权限
        List<Permission> ps = permissionService.list();
        res.put("ps",ps);

        //2.查找当前角色的权限
        List<Permission> rolePerm = permissionService.list(role);
        //获取当前角色所有权限的id，因为已经查找了所有权限，前端只需知道当前角色的权限id即可
        List<Long> ids=new ArrayList<>();
        for (Permission permission : rolePerm) {
            ids.add(permission.getId());
        }

        res.put("rolePermIds",ids);


        return res;
    }

    /**
     * 增加权限
     * @param roleExt
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "updataRolePermission",method = RequestMethod.PUT)
    public String addRolePermission(@RequestBody RoleExt roleExt){
        System.out.println("角色权限添加------"+roleExt.toString());
        ResponseJSON res=new ResponseJSON();
        Role role=roleExt.getRole();
        long[] ids=roleExt.getIds().getIds();
        try{
            rolePermissionService.addPermission(role,ids);
            res.setCode(ResponseStatusEnum.Do_SUCCESSFUL.getStatus());
            res.setMsg("授权成功！");
        }catch (Exception e){
            res.setCode(ResponseStatusEnum.Do_FAIELD.getStatus());
            res.setMsg("授权失败！");
        }

        return JSONObject.toJSON(res).toString();
    }


    /**
     * 删除角色权限
     * @param roleExt
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "updataRolePermission",method = RequestMethod.DELETE)
    public String deleteRolePermission(@RequestBody RoleExt roleExt){
        System.out.println("角色权限-删除------"+roleExt.toString());

        ResponseJSON res=new ResponseJSON();
        Role role=roleExt.getRole();
        long[] ids=roleExt.getIds().getIds();
        try{
            rolePermissionService.deletePermission(role,ids);
            res.setCode(ResponseStatusEnum.Do_SUCCESSFUL.getStatus());
            res.setMsg("删除权限成功！");
        }catch (Exception e){
            res.setCode(ResponseStatusEnum.Do_FAIELD.getStatus());
            res.setMsg("删除权限失败！");
        }

        return JSONObject.toJSON(res).toString();
    }




    /**
     * 更新role信息
     * @param role
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "updateRole",method = RequestMethod.POST)
    public String update(@RequestBody Role role) {
        System.out.println(role);
        ResponseJSON res=new ResponseJSON();
        try{
            roleService.update(role);
            res.setCode(ResponseStatusEnum.Do_SUCCESSFUL.getStatus());
            res.setMsg("update role successfuly!");
        }catch (Exception e){
            res.setCode(ResponseStatusEnum.Do_FAIELD.getStatus());
            res.setMsg("update role failed!");
        }
        return JSONObject.toJSON(res).toString();
    }


    @RequestMapping("editRolePermissionPage")
    public String editRolePermissionPage(Model model,Role role){
        Role r = roleService.get(role.getId());

        model.addAttribute("role",r);

        return "editRolePermission";
    }

    /**
     * 检查角色名称是否合法
     * @param rolename
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "checkRolename",method = RequestMethod.POST,produces="application/json;charset=UTF-8")
    public String checkRolename(String rolename){
//        System.out.println(rolename);
        ResponseJSON res=new ResponseJSON();
        Role role=roleService.getByName(rolename);
        if (role!=null){
            //不空，说明有这个角色
            res.setCode(ResponseStatusEnum.Do_FAIELD.getStatus());
            res.setMsg("改角色已存在！");
        }else {
            res.setCode(ResponseStatusEnum.Do_SUCCESSFUL.getStatus());
            res.setMsg("角色名称名可用！");
        }

        return JSONObject.toJSON(res).toString();
    }





}