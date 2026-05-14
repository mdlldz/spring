package com.example.entity;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * 用户实体类
 * 覆盖知识点：数据绑定、JSR303参数校验
 */
public class User {
    private Integer id; // 用户ID
    @NotBlank(message = "用户名不能为空")
    @Size(min = 3, max = 20, message = "用户名长度需在3-20之间")
    private String username; // 用户名
    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 16, message = "密码长度需在6-16之间")
    private String password; // 密码
    @Email(message = "邮箱格式不正确")
    private String email; // 邮箱
    private Integer age; // 年龄
    private Date createTime; // 创建时间

    // 无参构造器（SpringMVC数据绑定必须）
    public User(int i, String redirectUser, String number, int i1, String mail) {}

    // 全参构造器
    public User(Integer id, String username, String password, String email, Integer age, Date createTime) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.age = age;
        this.createTime = createTime;
    }

    // Getter和Setter方法
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", age=" + age +
                ", createTime=" + createTime +
                '}';
    }
}