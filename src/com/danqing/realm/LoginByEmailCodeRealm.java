package com.danqing.realm;

import com.danqing.pojo.User;
import com.danqing.service.PermissionService;
import com.danqing.service.RoleService;
import com.danqing.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

public class LoginByEmailCodeRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private PermissionService permissionService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection)  {
        // 能进入到这里，表示账号已经通过验证了
        String userName = (String) principalCollection.getPrimaryPrincipal();

        //通过principalCollection.getPrimaryPrincipal()获取的可能是username，也可能是email
        //需要进行如下判断

        User user=userService.getByName(userName);
        //查找为null,说明不是username,则一定是email
        if(null==user){
            user=userService.getByEmail(userName);
        }

        // 通过service获取角色和权限
        Set<String> permissions = permissionService.listPermissions(user.getName());
        Set<String> roles = roleService.listRoleNames(user.getName());

        // 授权对象
        SimpleAuthorizationInfo s = new SimpleAuthorizationInfo();
        // 把通过service获取到的角色和权限放进去
        s.setStringPermissions(permissions);
        s.setRoles(roles);
        return s;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

        // 获取账号密码
        String email = token.getPrincipal().toString();

        //通过email查找
        User user=userService.getByEmail(email);

        if (user==null){
            throw new UnknownAccountException("账号不存在");
        }else{
            // 获取数据库中的密码
            String activeCode = user.getActiveCode();

            //认证信息里的信息和token中的信息去比对
            SimpleAuthenticationInfo a = new SimpleAuthenticationInfo(email, activeCode,getName());
            SecurityUtils.getSubject().getSession().setAttribute("user", user);
            return a;
        }

    }

}