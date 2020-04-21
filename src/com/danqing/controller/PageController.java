package com.danqing.controller;

import com.danqing.pojo.Permission;
import com.danqing.pojo.Role;
import com.danqing.service.PermissionService;
import com.danqing.service.RoleService;
import com.danqing.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

//专门用于显示页面的控制器
@Controller
@RequestMapping("")
public class PageController {
    @Autowired
    UserService userService;

    @Autowired
    RoleService roleService;

    @Autowired
    PermissionService permissionService;

    @RequestMapping(value = "index")
    public String index(Model model, HttpServletRequest req) {

        String name = (String) req.getSession().getAttribute("name");
//        System.out.println(name);

        List<Role> roles = roleService.listRoles(name);
        List<Permission> ps = new ArrayList<>();
        for (Role role : roles) {
            for (Permission permission : permissionService.list(role)) {
                if (permission != null && permission.getMenu()) {
                    ps.add(permission);
//                    System.out.println(permission.getName());
                }
            }

        }

        model.addAttribute("ps", ps);

        return "index";
    }

    @RequestMapping("listUser")
    public String listUser() {
        return "listUser";
    }

    @RequestMapping("listRole")
    public String listRole() {
        return "listRole";
    }

    @RequestMapping("listPermission")
    public String listPermission() {
        return "listPermission";
    }

    // @RequiresPermissions("deleteOrder")
    @RequestMapping("deleteOrder")
    public String deleteOrder() {
        return "deleteOrder";
    }

    // @RequiresRoles("productManager")
    @RequestMapping("deleteProduct")
    public String deleteProduct() {
        return "deleteProduct";
    }

    @RequestMapping("listProduct")
    public String listProduct() {
        return "listProduct";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login() {
        return "login";
    }

    @RequestMapping("unauthorized")
    public String noPerms() {
        return "unauthorized";
    }




}
