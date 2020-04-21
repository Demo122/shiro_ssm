package com.danqing.pojo;

/**
 * Description: shiro_ssm
 * Created by danqing on 2020/4/22 2:55
 */
public class RoleExt {
    private Role role;
    private Ids ids;

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "RoleExt{" +
                "role=" + role +
                ", ids=" + ids +
                '}';
    }

    public Ids getIds() {
        return ids;
    }

    public void setIds(Ids ids) {
        this.ids = ids;
    }
}
