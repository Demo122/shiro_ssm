package com.danqing.service.impl;

import com.danqing.mapper.UserMapper;
import com.danqing.pojo.User;
import com.danqing.pojo.UserExample;
import com.danqing.service.UserRoleService;
import com.danqing.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper userMapper;
    @Autowired
    UserRoleService userRoleService;

    @Override
    public String getPassword(String name) {
        User user = getByName(name);
        if (null == user)
            return null;
        return user.getPassword();
    }

    @Override
    public User getByName(String name) {
        UserExample example = new UserExample();
        example.createCriteria().andNameEqualTo(name);
        List<User> users = userMapper.selectByExample(example);
        if (users.isEmpty())
            return null;
        return users.get(0);
    }

    @Override
    public void add(User u) {
        userMapper.insert(u);
    }

    @Override
    public void delete(Long id) {
        userMapper.deleteByPrimaryKey(id);
        userRoleService.deleteByUser(id);
    }

    @Override
    public void update(User u) {
        userMapper.updateByPrimaryKeySelective(u);
    }

    @Override
    public User get(Long id) {
        return userMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<User> list() {
        UserExample example = new UserExample();
        example.setOrderByClause("id desc");
        return userMapper.selectByExample(example);

    }


    @Override
    public int getTotal() {
        return userMapper.getTotal();
    }
}