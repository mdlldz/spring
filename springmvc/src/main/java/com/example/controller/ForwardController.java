package com.example.controller;

import com.example.entity.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 请求转发与重定向控制器
 * 覆盖知识点：请求转发、重定向、带数据重定向、原生ServletAPI实现
 */
@Controller
@RequestMapping("/forward")
public class ForwardController {

    /**
     * 方式1：SpringMVC请求转发（forward:前缀）
     */
    @GetMapping("/springForward")
    public String springForward(Model model) {
        model.addAttribute("msg", "SpringMVC请求转发的消息");
        // 转发到/forward/target
        return "forward:/forward/target";
    }

    /**
     * 方式2：原生ServletAPI请求转发
     */
    @GetMapping("/servletForward")
    public void servletForward(HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setAttribute("msg", "原生Servlet请求转发的消息");
        request.getRequestDispatcher("/WEB-INF/views/forward/target.jsp").forward(request, response);
    }

    /**
     * 方式1：SpringMVC重定向（redirect:前缀）
     */
    @GetMapping("/springRedirect")
    public String springRedirect() {
        // 重定向到/forward/redirectTarget
        return "redirect:/forward/redirectTarget";
    }

    /**
     * 方式2：原生ServletAPI重定向
     */
    @GetMapping("/servletRedirect")
    public void servletRedirect(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendRedirect(request.getContextPath() + "/forward/redirectTarget");
    }

    /**
     * 带数据重定向：使用RedirectAttributes传递flash属性
     */
    @GetMapping("/redirectWithData")
    public String redirectWithData(RedirectAttributes attributes) {
        // flash属性会被放到session中，重定向后取出并删除
        attributes.addFlashAttribute("flashMsg", "重定向传递的临时消息");
        attributes.addFlashAttribute("user", new User(1, "redirectUser", "123456", 21, "redirect@qq.com"));
        return "redirect:/forward/redirectTarget";
    }

    /**
     * 转发/重定向目标页面
     */
    @GetMapping("/target")
    public String targetPage() {
        return "forward/target";
    }

    @GetMapping("/redirectTarget")
    public String redirectTargetPage() {
        return "forward/redirectTarget";
    }
}