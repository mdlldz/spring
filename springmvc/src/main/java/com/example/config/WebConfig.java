package com.example.config;

import com.example.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

/**
 * SpringMVC核心配置类
 * 覆盖知识点：SpringMVC配置、视图解析器、文件上传、拦截器、静态资源映射
 */
@Configuration
@ComponentScan(basePackages = "com.example")
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    /**
     * 视图解析器配置
     * 功能：拼接视图路径，将逻辑视图名转换为物理视图路径
     */
    @Bean
    public InternalResourceViewResolver viewResolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("/WEB-INF/views/");
        resolver.setSuffix(".jsp");
        resolver.setViewClass(org.springframework.web.servlet.view.JstlView.class);
        return resolver;
    }

    /**
     * 文件上传解析器配置
     * 功能：解析multipart/form-data类型的文件上传请求
     */
    @Bean
    public CommonsMultipartResolver multipartResolver() {
        CommonsMultipartResolver resolver = new CommonsMultipartResolver();
        resolver.setMaxUploadSize(10 * 1024 * 1024); // 最大上传文件10MB
        resolver.setDefaultEncoding("UTF-8");
        return resolver;
    }

    /**
     * 拦截器注册
     * 功能：注册登录拦截器，对需要登录的请求进行权限校验
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor())
                .addPathPatterns("/**") // 拦截所有请求
                .excludePathPatterns(
                        "/user/login",
                        "/user/register",
                        "/static/**",
                        "/error"
                ); // 放行登录、注册、静态资源和错误页面
    }

    /**
     * 静态资源映射
     * 功能：放行css、js、图片等静态资源请求
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**")
                .addResourceLocations("/static/");
    }

    /**
     * 配置默认Servlet处理静态资源
     */
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }
}