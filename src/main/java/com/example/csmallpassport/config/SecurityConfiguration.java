package com.example.csmallpassport.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Slf4j
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        String[] urls = {
                "/doc.html",
                "/favicon.ico",
                "/**/*.css",
                "/**/*.js",
                "/swagger-resources",
                "/v2/api-docs",
                "/admins/*"
        };

        //禁用“防止伪造的跨域攻击”这种防御机制
        http.csrf().disable();

        //配置url的授权访问
        //注意 配置时 各请求的授权访问遵循 ”第一匹配原则“ 即根据代码从上至下 以第一次匹配到的规则为准
        //所以 在配置时 必须将更加精准的配置写在前面 覆盖范围更大的匹配配置写在后面
        http.authorizeRequests() //配置url的授权访问
                .mvcMatchers(urls) //匹配某些请求
                .permitAll() //直接许可： 不需要认证就可以访问
                .anyRequest() //匹配任何请求
                .authenticated() //以上匹配的请求必须是”已经通过认证的“
        ;
        //调用formLogin表示启用登录和登出页面 如果未调用此方法 则没有登录和登出页面
//        http.formLogin();

//        super.configure(http); //不要调用父类的同款方法
    }
}
