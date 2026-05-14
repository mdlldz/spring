package com.example.controller;

import com.example.entity.User;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;

/**
 * 自定义视图控制器
 * 功能：演示自定义视图解析器的使用，导出Excel文件
 */
@Controller
@RequestMapping("/view")
public class ViewController {

    @Autowired
    private UserService userService;

    /**
     * 导出用户数据为Excel
     * @param model 模型对象
     * @return 自定义视图名
     */
    @GetMapping("/exportExcel")
    public String exportExcel(Model model) {
        List<User> userList = userService.findAll();
        model.addAttribute("userList", userList);
        // 返回自定义视图名，由CustomViewResolver解析
        return "excelView";
    }
}