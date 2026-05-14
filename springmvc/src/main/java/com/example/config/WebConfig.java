package com.example.config;

import com.example.interceptor.LoginInterceptor;
import com.example.resolver.CustomViewResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import java.util.ArrayList;
import java.util.List;

/**
 * SpringMVC核心配置类
 * 扩展：注册自定义视图解析器
 */
@Configuration
@ComponentScan("com.example")
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    /**
     * 内置JSP视图解析器
     */
    @Bean
    public InternalResourceViewResolver jspViewResolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("/WEB-INF/views/");
        resolver.setSuffix(".jsp");
        resolver.setOrder(2); // 优先级低于自定义视图解析器
        return resolver;
    }

    /**
     * 自定义视图解析器
     */
    @Bean
    public CustomViewResolver customViewResolver() {
        return new CustomViewResolver();
    }

    /**
     * 配置视图解析器链
     */
    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        // 注册自定义视图解析器
        registry.viewResolver(customViewResolver());
        // 注册JSP视图解析器
        registry.jsp("/WEB-INF/views/", ".jsp");
    }

    /**
     * 文件上传解析器
     */
    @Bean
    public CommonsMultipartResolver multipartResolver() {
        CommonsMultipartResolver resolver = new CommonsMultipartResolver();
        resolver.setDefaultEncoding("UTF-8");
        resolver.setMaxUploadSize(10 * 1024 * 1024);
        return resolver;
    }

    /**
     * 注册拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/user/login", "/user/doLogin", "/static/**", "/api/**");
    }

    /**
     * 静态资源放行
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("/static/");
    }
}