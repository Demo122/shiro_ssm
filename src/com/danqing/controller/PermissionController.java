package com.danqing.controller;

import com.alibaba.fastjson.JSONObject;
import com.danqing.pojo.*;
import com.danqing.service.PermissionService;
import com.danqing.service.RolePermissionService;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("permission")
public class PermissionController {
    @Autowired
    PermissionService permissionService;

    @Autowired
    RolePermissionService rolePermissionService;

    @ResponseBody
    @RequestMapping("listPermission")
    public String list(@RequestParam(value = "page", defaultValue = "1") int page, @RequestParam(value = "nums", defaultValue = "10") int nums) {
        //使用pageHelper分页
        PageHelper.startPage(page, nums);
        List<Permission> ps = permissionService.list();
        //获取用户总数
        int count = permissionService.getTotal();

        Map<String, Object> response = new HashMap<>();
        response.put("code", 0);
        response.put("msg", "");
        response.put("count", count);
        response.put("data", ps);
        return JSONObject.toJSON(response).toString();
    }

//    @RequestMapping("editPermission")
//    public String list(Model model, long id) {
//        Permission permission = permissionService.get(id);
//        model.addAttribute("permission", permission);
//        return "editPermission";
//    }

    @ResponseBody
    @RequestMapping("updatePermission")
    public String update(@RequestBody Permission permission) {
        ResponseJSON res=new ResponseJSON();
        try{
            permissionService.update(permission);
            res.setCode(ResponseStatusEnum.Do_SUCCESSFUL.getStatus());
            res.setMsg("update permission successfuly!");
        }catch (Exception e){
            res.setCode(ResponseStatusEnum.Do_FAIELD.getStatus());
            res.setMsg("update permission failed!");
        }
        return JSONObject.toJSON(res).toString();
    }


    //删除权限
    @ResponseBody
    @RequestMapping(value = "deletePermission", method = RequestMethod.DELETE)
    public Map delete(@RequestBody long id) {
        Map<String, Object> res = new HashMap<>();
        try {
            //1.删除权限表中的权限
            permissionService.delete(id);
            //2.删除与权限表关联的role_permission表中的 记录
            rolePermissionService.deleteByPermission(id);
            res.put("msg", "删除权限成功！");

        } catch (Exception e) {
            res.put("msg", "删除权限失败！");
        }
        return res;
    }

    //批量删除权限
    @ResponseBody
    @RequestMapping(value = "deleteSelectPermission", method = RequestMethod.DELETE)
    public String deleteSelectPermission(@RequestBody Ids ids) {
        ResponseJSON res=new ResponseJSON();
        try {
            for (long id : ids.getIds()) {
                //1.删除权限表中的权限
                permissionService.delete(id);
                //2.删除与权限表关联的role_permission表中的 记录
                rolePermissionService.deleteByPermission(id);
            }
            res.setCode(ResponseStatusEnum.Do_SUCCESSFUL.getStatus());
            res.setMsg("删除权限成功！");
        } catch (Exception e) {
            res.setCode(ResponseStatusEnum.Do_FAIELD.getStatus());
            res.setMsg("删除权限失败！");
        }

        return JSONObject.toJSON(res).toString();
    }

    @RequestMapping("addPermissionPage")
    public String addPermissionPage() {
        return "addPermission";
    }


    //添加新权限
    @ResponseBody
    @RequestMapping(value = "addPermission", method = RequestMethod.POST)
    public Map addPermission(@RequestBody Permission permission) {
//        System.out.println(permission);

        Map<String, Object> res = new HashMap<>();
        try {
            //1.将新权限存入数据库
            permissionService.add(permission);
            //2.将新权限赋给admin

            //从数据库中获取新权限，主要是获取新权限的id，因为数据库中的id是设置自增，必须存入再取
            Permission newPermission = permissionService.get(permission.getName());

            RolePermission rolePermission = new RolePermission();
            rolePermission.setPid(newPermission.getId());
            //admin角色id为1
            rolePermission.setRid((long) 1);

            //添加角色权限
            rolePermissionService.addPermission(rolePermission);

            res.put("msg", "添加成功");
        } catch (Exception e) {

            res.put("msg", "添加失败");
        }

        return res;
    }

    @RequestMapping("editPermissionPage/{id}")
    public String editPermissionPage(Model model, Permission permission) {
        Permission p = permissionService.get(permission.getId());

        model.addAttribute("permission",p);

        return "editPermission";
    }

}