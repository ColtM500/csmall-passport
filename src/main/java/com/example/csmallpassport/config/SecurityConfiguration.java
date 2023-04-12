package com.example.csmallpassport.config;

import com.alibaba.fastjson.JSON;
import com.example.csmallpassport.filter.JwtAuthorizationFilter;
import com.example.csmallpassport.web.JsonResult;
import com.example.csmallpassport.web.ServiceCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)//新增 基于方法的权限检查
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtAuthorizationFilter jwtAuthorizationFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        // return NoOpPasswordEncoder.getInstance();
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 处理“当客户端提交请求时没有携带JWT，请求的目标却是需要通过认证的资源”的问题
        http.exceptionHandling().authenticationEntryPoint(new AuthenticationEntryPoint() {
            @Override
            public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
                response.setContentType("application/json; charset=utf-8;");
                String message = "未检测到登录信息，请登录！（在开发阶段，看到此提示时，请检查客户端是否携带了有效的JWT数据）";
                log.warn(message);
                JsonResult jsonResult = JsonResult.fail(ServiceCode.ERR_UNAUTHORIZED, message);
                String jsonResultString = JSON.toJSONString(jsonResult);
                PrintWriter writer = response.getWriter();
                writer.println(jsonResultString);
                writer.close();
            }
        });

        // 白名单
        // 所有路径必须使用 / 作为第1个字符
        // 使用1个星号作为通配符时，表示通配此层级的任意资源，例如：/admins/*，可以匹配 /admins/delete、/admins/add-new
        // 但是，不可以匹配多层级，例如：/admins/* 不可匹配 /admins/9527/delete
        // 使用2个连续的星号，表示通配若干层级的任意资源，例如：/admins/* 可以匹配 /admins/delete、/admins/9527/delete
        String[] urls = {
                "/doc.html",
                "/favicon.ico",
                "/**/*.css",
                "/**/*.js",
                "/swagger-resources",
                "/v2/api-docs",
                "admins/login",
        };

        // 此方法会配置Spring Security框架自带的CorsFilter，此过滤器会对OPTIONS请求放行
        http.cors();

        // 禁用“防止伪造的跨域攻击”这种防御机制
        http.csrf().disable();

        // 配置URL的授权访问
        // 注意：配置时，各请求的授权访问遵循“第一匹配原则”，即根据代码从上至下，以第1次匹配到的规则为准
        // 所以，在配置时，必须将更加精准的配置写在前面，覆盖范围更大的匹配的配置写在后面
        http.authorizeRequests() // 配置URL的授权访问
                .mvcMatchers(urls) // 匹配某些请求
                .permitAll() // 直接许可，即：不需要通过认证就可以访问
                .anyRequest() // 匹配任何请求
                .authenticated() // 以上匹配到的请求必须是“已经通过认证的”
        ;

        // 将自定义的JWT过滤器添加在Spring Security的UsernamePasswordAuthenticationFilter之前
        http.addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);

        // 将Session策略设置为“从不使用”：STATELESS=无状态，即从不使用Session，NEVER=从不主动创建Session
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // 调用formLogin()表示启用登录和登出页面，如果未调用此方法，则没有登录和登出页面
        // http.formLogin();
        // super.configure(http); // 不要调用父类的同款方法
    }
}
