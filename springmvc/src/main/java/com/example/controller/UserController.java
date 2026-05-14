package com.example.controller;

import com.example.entity.Result;
import com.example.entity.User;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

/**
 * 用户控制器
 * 请求映射、参数绑定、转发重定向、session、model、校验
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 跳转到登录页
     */
    @GetMapping("/login")
    public String toLogin() {
        return "user/login";
    }

    /**
     * 登录提交
     */
    @PostMapping("/doLogin")
    public String doLogin(
            @RequestParam String username,
            @RequestParam String password,
            HttpSession session,
            RedirectAttributes attributes) {

        User user = userService.login(username, password);
        if (user == null) {
            attributes.addFlashAttribute("msg", "用户名或密码错误");
            return "redirect:/user/login";
        }

        session.setAttribute("loginUser", user);
        return "redirect:/user/list";
    }

    /**
     * 用户列表
     */
    @GetMapping("/list")
    public String list(Model model) {
        List<User> userList = userService.findAll();
        model.addAttribute("userList", userList);
        return "user/list";
    }

    /**
     * 跳转到添加页
     */
    @GetMapping("/add")
    public String toAdd(Model model) {
        model.addAttribute("user", new User(1, "redirectUser", "123456", 21, "redirect@qq.com"));
        return "user/add";
    }

    /**
     * 添加提交（JSR303校验）
     */
    @PostMapping("/doAdd")
    public String doAdd(@Valid User user, BindingResult result) {
        if (result.hasErrors()) {
            return "user/add";
        }
        userService.add(user);
        return "redirect:/user/list";
    }

    /**
     * 删除用户
     */
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id) {
        userService.deleteById(id);
        return "redirect:/user/list";
    }

    /**
     * 登出
     */
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/user/login";
    }

    /**
     * JSON格式返回所有用户
     */
    @GetMapping("/jsonList")
    @ResponseBody
    public Result<List<User>> jsonList() {
        List<User> list = userService.findAll();
        return Result.success(list);
    }
}