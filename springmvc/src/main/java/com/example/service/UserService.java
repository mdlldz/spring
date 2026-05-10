package com.example.service;

import com.example.entity.User;
import java.util.List;

/**
 * 用户业务接口
 */
public interface UserService {
    /**
     * 根据用户ID查询用户
     * @param id 用户ID
     * @return 用户对象
     */
    User getUserById(Integer id);

    /**
     * 用户登录
     * @param username 用户名
     * @param password 密码
     * @return 登录成功的用户对象
     */
    User login(String username, String password);

    /**
     * 查询所有用户
     * @return 用户列表
     */
    List<User> findAll();

    /**
     * 添加用户
     * @param user 用户对象
     */
    void add(User user);

    /**
     * 根据ID删除用户
     * @param id 用户ID
     */
    void deleteById(Integer id);
}