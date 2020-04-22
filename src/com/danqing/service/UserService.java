package com.danqing.service;

import com.danqing.pojo.User;

import java.util.List;

public interface UserService {
    public String getPassword(String name);

    public User getByName(String name);

    public User getByEmail(String email);

    public List<User> list();

    public void add(User user);

    public void delete(Long id);

    public User get(Long id);

    public void update(User user);

    public int getTotal();
}