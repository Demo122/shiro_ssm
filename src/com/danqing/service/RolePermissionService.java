package com.danqing.service;

import com.danqing.pojo.Role;
import com.danqing.pojo.RolePermission;

public interface RolePermissionService {
    public void setPermissions(Role role, long[] permissionIds);

    public void deleteByRole(long roleId);

    public void deleteByPermission(long permissionId);

    public void addPermission(RolePermission rolePermission);
}