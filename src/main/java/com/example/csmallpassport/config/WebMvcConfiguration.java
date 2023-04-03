package com.example.csmallpassport.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Spring MVC配置类
 *
 * @author java@tedu.cn
 * @version 0.0.1
 */
@Slf4j
@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {
    public WebMvcConfiguration() {
        log.debug("创建配置类对象：WebMvcConfiguration");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 所有请求路径
                .allowedOriginPatterns("*") // 所有来源
                .allowedHeaders("*") // 所有请求头
                .allowedMethods("*") // 所有请求方式
                .allowCredentials(true) // 允许凭证
                .maxAge(3600); // 有效期
    }
}
