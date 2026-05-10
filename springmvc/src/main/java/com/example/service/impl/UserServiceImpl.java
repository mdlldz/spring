package com.example.service.impl;

import com.example.dao.UserDao;
import com.example.entity.User;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户业务实现类
 * 功能：处理业务逻辑、调用DAO
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    /**
     * 根据用户ID查询用户
     * @param id 用户ID
     * @return 用户对象
     */
    @Override
    public User getUserById(Integer id) {
        return userDao.getUserById(id);
    }

    /**
     * 登录业务
     * @param username 用户名
     * @param password 密码
     * @return 登录成功的用户对象
     */
    @Override
    public User login(String username, String password) {
        if (username == null || password == null || username.trim().isEmpty() || password.trim().isEmpty()) {
            throw new RuntimeException("用户名或密码不能为空");
        }
        return userDao.login(username, password);
    }

    /**
     * 查询所有用户
     * @return 用户列表
     */
    @Override
    public List<User> findAll() {
        return userDao.findAll();
    }

    /**
     * 添加用户
     * @param user 待添加的用户对象
     */
    @Override
    public void add(User user) {
        userDao.add(user);
    }

    /**
     * 根据ID删除用户
     * @param id 用户ID
     */
    @Override
    public void deleteById(Integer id) {
        userDao.deleteById(id);
    }
}