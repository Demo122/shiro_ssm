package com.danqing.pojo;

/**
 * Description: shiro_ssm
 * Created by danqing on 2020/4/19 1:06
 */
public class UserExt {
    private User user;
    private RoleIds roleIds;

    @Override
    public String toString() {
        return "UserExt{" +
                "user=" + user +
                ", roleIds=" + roleIds +
                '}';
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public RoleIds getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(RoleIds roleIds) {
        this.roleIds = roleIds;
    }
}
