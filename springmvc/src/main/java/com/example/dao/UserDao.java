package com.example.dao;

import com.example.entity.User;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户数据访问层
 * 功能：模拟数据库CRUD
 */
@Repository
public class UserDao {
    private static final List<User> userList = new ArrayList<>();

    // 模拟数据
    static {
        User u1 = new User(1, "redirectUser", "123456", 21, "redirect@qq.com");
        u1.setId(1);
        u1.setUsername("admin");
        u1.setPassword("123456");
        u1.setAge(20);
        u1.setEmail("admin@qq.com");
        userList.add(u1);
    }

    /**
     * 根据ID查询用户
     * @param id 用户ID
     * @return 用户对象
     */
    public User getUserById(Integer id) {
        for (User user : userList) {
            if (user.getId().equals(id)) {
                return user;
            }
        }
        return null;
    }

    /**
     * 根据用户名密码查询
     * @param username 用户名
     * @param password 密码
     * @return 用户对象
     */
    public User login(String username, String password) {
        for (User user : userList) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }

    /**
     * 查询所有用户
     * @return 用户列表
     */
    public List<User> findAll() {
        return new ArrayList<>(userList);
    }

    /**
     * 添加用户
     * @param user 用户对象
     */
    public void add(User user) {
        user.setId(userList.size() + 1);
        userList.add(user);
    }

    /**
     * 根据ID删除用户
     * @param id 用户ID
     */
    public void deleteById(Integer id) {
        userList.removeIf(u -> u.getId().equals(id));
    }
}