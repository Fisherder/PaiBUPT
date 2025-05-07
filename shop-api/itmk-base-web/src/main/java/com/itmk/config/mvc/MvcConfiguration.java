// 更新src/main/java/com/itmk/config/mvc/MvcConfiguration.java
package com.itmk.config.mvc;

import com.itmk.web.interceptor.AuthenticationInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfiguration implements WebMvcConfigurer {
    @Value("${web.load-path}")
    private String loadPath;

    @Bean
    public AuthenticationInterceptor authenticationInterceptor() {
        return new AuthenticationInterceptor();
    }

    /**
     * 跨域配置
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedMethods("*")
                .allowedHeaders("*")
                .maxAge(3600)
                .allowCredentials(true);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/images/**")
                .addResourceLocations(loadPath);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authenticationInterceptor())
                .addPathPatterns("/api/**") // 应用于所有API端点
                .excludePathPatterns(     // 排除公共端点
                        "/api/wxUser/register",
                        "/api/wxUser/login",
                        "/api/wxUser/forget",
                        "/api/category/getCateList",
                        "/api/category/getSelectList",
                        "/api/banner/getIndexBanner",
                        "/api/goods/getIndexList",
                        "/api/goods/getUsedList",
                        "/api/goods/getBuyList"
                );
    }
}