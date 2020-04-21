package com.danqing.pojo;

/**
 * Description: shiro_ssm
 * Created by danqing on 2020/4/19 1:06
 */
public class UserExt {
    private User user;
    private Ids roleIds;


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

    public Ids getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(Ids roleIds) {
        this.roleIds = roleIds;
    }

}
