package com.example.config;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

/**
 * 项目初始化类（替代web.xml）
 * 注册SpringMVC前端控制器
 */
public class WebInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    // 加载Spring配置
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[0];
    }

    // 加载SpringMVC配置
    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[]{WebConfig.class};
    }

    // 映射路径
    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }
}