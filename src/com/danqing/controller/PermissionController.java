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

import java.util.*;

@Controller
@RequestMapping("permission")
public class PermissionController {
    @Autowired
    PermissionService permissionService;

    @Autowired
    RolePermissionService rolePermissionService;

    /**
     * searchBean 包含查找项，和查找内容 即searchItem 按说明查找，searchcontent 查找条件
     * @param searchBean
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "searchRole",method = RequestMethod.POST)
    public String searchRole(@RequestBody searchBean searchBean){

        ResponseJSON res=new ResponseJSON();
        List<Permission> data=new ArrayList<>();

        //使用pageHelper分页
        PageHelper.startPage(searchBean.getPage(), searchBean.getNums());

        //获取查找到的总数
        int count=0;

        if ("name".equals(searchBean.getSearchItem())){
            //根据名字查找
            Permission p=permissionService.get(searchBean.getSearchContent());
            if (null!=p){
                data.add(p);
                count=data.size();
            }else {
                count=0;
            }
        }
        if ("category".equals(searchBean.getSearchItem())){
            //按类别查找
            data=permissionService.selectByCategory(searchBean.getSearchContent());
            count=permissionService.getTotalSelectByCategory(searchBean.getSearchContent());
        }
        if("menu".equals(searchBean.getSearchItem())){
            //查找所有菜单
            String menu1=searchBean.getSearchContent();
            Boolean menu=null;
            if("true".equals(menu1)||"false".equals(menu1)){
                //将string装boolean值
                menu=Boolean.valueOf(searchBean.getSearchContent());
            }
            data=permissionService.selectByMenu(menu);
            count=permissionService.getTotalSelectByMenu(searchBean.getSearchContent());
        }

        for (Permission datum : data) {
            System.out.println(datum);
        }


        //为什么设为0呢，应为layui的table异步请求成功的code要求是0.。。。。。。，否则就是请求失败 有点子坑呀
        res.setCode(0);
        res.setMsg("search successfuly!");
        res.setDataCount(count);
        res.setData(data);

        return JSONObject.toJSON(res).toString();
    }

    @ResponseBody
    @RequestMapping("listPermission")
    public String list(@RequestParam(value = "page", defaultValue = "1") int page, @RequestParam(value = "nums", defaultValue = "10") int nums) {
        //使用pageHelper分页
        PageHelper.startPage(page, nums);
        //再查找
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

    /**
     * 编辑权限信息
     * @param model
     * @param permission
     * @return
     */
    @RequestMapping("editPermissionPage/{id}")
    public String editPermissionPage(Model model, Permission permission) {
        Permission p = permissionService.get(permission.getId());

        model.addAttribute("permission",p);

        return "editPermission";
    }

    @ResponseBody
    @RequestMapping(value = "checkPermissionName",method = RequestMethod.POST,produces="application/json;charset=UTF-8")
    public String checkPermissionName(String permissionName){
        ResponseJSON res=new ResponseJSON();
        Permission p=permissionService.get(permissionName);
        if (p!=null){
            //不空，说明有这个权限名
            res.setCode(ResponseStatusEnum.Do_FAIELD.getStatus());
            res.setMsg("权限名已存在！");
        }else {
            res.setCode(ResponseStatusEnum.Do_SUCCESSFUL.getStatus());
            res.setMsg("权限名可用！");
        }

        return JSONObject.toJSON(res).toString();
    }

    @ResponseBody
    @RequestMapping(value = "checkUrl",method = RequestMethod.POST,produces="application/json;charset=UTF-8")
    public String checkUrl(String url){
        ResponseJSON res=new ResponseJSON();
        Permission p=permissionService.getByUrl(url);
        if (p!=null){
            //不空，说明有这个url
            res.setCode(ResponseStatusEnum.Do_FAIELD.getStatus());
            res.setMsg("url已存在！");
        }else {
            res.setCode(ResponseStatusEnum.Do_SUCCESSFUL.getStatus());
            res.setMsg("url可用！");
        }

        return JSONObject.toJSON(res).toString();
    }
}