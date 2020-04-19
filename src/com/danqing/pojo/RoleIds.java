package com.danqing.pojo;

import java.util.Arrays;

/**
 * Description: shiro_ssm
 * Created by danqing on 2020/4/19 1:10
 */
public class RoleIds {
    private long[] roleIds;

    public long[] getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(long[] roleIds) {
        this.roleIds = roleIds;
    }

    @Override
    public String toString() {
        return "RoleIds{" +
                "roleIds=" + Arrays.toString(roleIds) +
                '}';
    }
}
