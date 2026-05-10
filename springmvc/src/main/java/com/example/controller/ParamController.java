package com.example.controller;

import com.example.entity.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

/**
 * 参数绑定控制器
 * 功能：演示各种参数接收方式
 */
@Controller
@RequestMapping("/param")
public class ParamController {

    /**
     * 基本类型参数
     */
    @GetMapping("/base")
    @ResponseBody
    public String base(@RequestParam String name, @RequestParam int age) {
        return "name=" + name + ", age=" + age;
    }

    /**
     * 数组参数
     */
    @GetMapping("/array")
    @ResponseBody
    public String array(@RequestParam String[] hobby) {
        return "hobby=" + Arrays.toString(hobby);
    }

    /**
     * 对象参数
     */
    @PostMapping("/obj")
    @ResponseBody
    public String obj(User user) {
        return "user=" + user.getUsername() + ", age=" + user.getAge();
    }

    /**
     * 路径参数
     */
    @GetMapping("/path/{id}/{name}")
    @ResponseBody
    public String path(@PathVariable Integer id, @PathVariable String name) {
        return "id=" + id + ", name=" + name;
    }

    /**
     * ajax json参数
     */
    @PostMapping("/json")
    @ResponseBody
    public User json(@RequestBody User user) {
        return user;
    }

    /**
     * request原生获取
     */
    @GetMapping("/request")
    @ResponseBody
    public String request(HttpServletRequest request) {
        String name = request.getParameter("name");
        return "request获取name：" + name;
    }
}