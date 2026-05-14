package com.example.controller;

import com.example.entity.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.List;

/**
 * 参数绑定与模型数据传递控制器
 * 覆盖知识点：Model、ModelAndView、@ModelAttribute、Session域数据传递
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
     * 原生Request获取参数
     */
    @GetMapping("/request")
    @ResponseBody
    public String request(HttpServletRequest request) {
        String name = request.getParameter("name");
        return "request获取name：" + name;
    }

    /**
     * Model数据传递（Request域）
     */
    @GetMapping("/model")
    public String modelData(Model model) {
        model.addAttribute("msg", "这是Request域中的消息");
        model.addAttribute("user", new User(1, "modelUser", "123456", 20, "model@qq.com"));
        return "param/model";
    }

    /**
     * ModelAndView数据传递
     */
    @GetMapping("/modelAndView")
    public ModelAndView modelAndView() {
        ModelAndView mav = new ModelAndView();
        mav.addObject("msg", "这是ModelAndView传递的消息");
        mav.addObject("list", Arrays.asList("Java", "Spring", "SpringMVC"));
        mav.setViewName("param/modelAndView");
        return mav;
    }

    /**
     * Session域数据传递
     */
    @GetMapping("/session")
    public String sessionData(HttpSession session) {
        session.setAttribute("sessionMsg", "这是Session域中的消息");
        session.setAttribute("loginUser", new User(1, "sessionUser", "123456", 22, "session@qq.com"));
        return "param/session";
    }

    /**
     * @ModelAttribute注解：提前向模型中添加数据
     */
    @ModelAttribute
    public void addCommonData(Model model) {
        model.addAttribute("systemName", "SpringMVC学习系统");
    }

    /**
     * @ModelAttribute绑定表单数据并传递到模型
     */
    @PostMapping("/modelAttribute")
    public String modelAttribute(@ModelAttribute("user") User user) {
        return "param/modelAttribute";
    }
}