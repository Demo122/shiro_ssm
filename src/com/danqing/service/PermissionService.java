package com.danqing.service;

import com.danqing.pojo.Permission;
import com.danqing.pojo.Role;

import java.util.List;
import java.util.Set;

public interface PermissionService {
    public Set<String> listPermissions(String userName);

    public List<Permission> list();

    public void add(Permission permission);

    public void delete(Long id);

    public Permission get(Long id);

    public Permission get(String permissionName);

    public Permission getByUrl(String url);

    public void update(Permission permission);

    public List<Permission> list(Role role);

    public List<Permission> selectByCategory(String category);

    public int getTotalSelectByCategory(String category);

    public List<Permission> selectByMenu(Boolean menu);

    public int getTotalSelectByMenu(String menu);

    public boolean needInterceptor(String requestURI);

    public Set<String> listPermissionURLs(String userName);

    public int getTotal();
}