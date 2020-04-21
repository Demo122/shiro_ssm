package com.danqing.mapper;

import com.danqing.pojo.Permission;
import com.danqing.pojo.PermissionExample;
import java.util.List;

public interface PermissionMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Permission record);

    int insertSelective(Permission record);

    List<Permission> selectByExample(PermissionExample example);

    Permission selectByPrimaryKey(Long id);

    Permission selectByName(String name);

    int updateByPrimaryKeySelective(Permission record);

    int updateByPrimaryKey(Permission record);

    int getTotal();

    Permission getByUrl(String url);
}