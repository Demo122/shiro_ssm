package com.danqing.controller;

import com.danqing.pojo.Permission;
import com.danqing.pojo.Role;
import com.danqing.pojo.User;
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
import java.util.Iterator;
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
        System.out.println(name);

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

    @RequestMapping("/listPermission")
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

}
