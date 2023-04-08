package com.example.csmallpassport.filter;


import com.alibaba.fastjson.JSON;
import com.example.csmallpassport.security.LoginPrincipal;
import com.example.csmallpassport.web.JsonResult;
import com.example.csmallpassport.web.ServiceCode;
import com.sun.corba.se.spi.ior.ObjectKey;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * <p>处理JWT的过滤器类</p>
 *
 * <p>此过滤器类的主要职责：</p>
 * <ul>
 *     <li>尝试接收客户端携带的JWT</li>
 *     <li>尝试解析接收到的JWT</li>
 *     <li>将解析成功后得到的结果创建为Authentication并存入到SecurityContext中</li>
 * </ul>
 *
 * @author java@tedu.cn
 * @version 0.0.1
 */
@Slf4j
@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    public static final int JWT_MIN_LENGTH = 113;

    @Value("${csmall.jwt.secret-key}")
    private String secretKey;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        // 根据业内惯用的做法，客户端应该将JWT保存在请求头（Request Header）中的名为Authorization的属性名
        String jwt = request.getHeader("Authorization");
        log.debug("尝试接收客户端携带的JWT数据，JWT：{}", jwt);

        // 判断客户端是否携带了基本有效的JWT
        if (!StringUtils.hasText(jwt) || jwt.length() < JWT_MIN_LENGTH) {
            log.warn("JWT数据为空 直接放行");
            // 客户端没有携带有铲的JWT，则“放行”，交由后续的组件继续处理
            filterChain.doFilter(request, response);
            // 【重要】终止当前方法的执行，不执行接下来的代码
            return;
        }

        // TODO：2.解析JWT时可能出现异常，需要处理
        /**
         * 客户端携带了基本有效的JWT，则尝试解析JWT
         * 这段代码是从 JWT 中解析出管理员的 ID 和用户名，并打印出来。
         * 首先，定义了一个密钥 secretKey，用于验证 JWT 的签名。
         * 然后，使用 Jwts.parser() 方法创建一个 JWT 解析器，并调用 setSigningKey 方法将密钥传递给解析器。
         * 接着，调用 parseClaimsJws 方法解析 JWT，并获取 JWT 的主体部分。
         * 其次，使用 get 方法从主体部分中获取管理员 ID 和用户名，并打印出来。
         * 最后，调用 filterChain.doFilter 方法放行请求。
         */
//        String secretKey = "kU4jrFA3iuI5jn25u743kfDs7a8pFEwS54hm";
        Claims claims = null;
        response.setContentType("application/json; charset=utf-8");
        try {
            claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(jwt)
                    .getBody();
        } catch (ExpiredJwtException e){
            //将错误信息保存在一个字符串变量message中
            String message = "您的登录信息已过期，请重新登录！";
            log.warn(message);
            //构建Json结果 使用JsonResult.fail()构建一个JSON格式错误结果
            JsonResult jsonResult = JsonResult.fail(ServiceCode.ERR_JWT_EXPIRED, message);
            //使用JSON.toJSONString() 方法将JSON结果转换为字符串格式
            String jsonResultString = JSON.toJSONString(jsonResult);
            //将字符串结果
            PrintWriter writer = response.getWriter();
            writer.println(jsonResultString);
            writer.close();
            return;
        } catch (SignatureException e){
            String message = "非法访问！";
            log.warn(message);
            JsonResult jsonResult = JsonResult.fail(ServiceCode.ERR_JWT_SIGNATURE, message);
            String jsonResultString = JSON.toJSONString(jsonResult);
            PrintWriter writer = response.getWriter();
            writer.println(jsonResultString);
            writer.close();
            return;
        } catch (MalformedJwtException e){
            String message = "非法访问！";
            log.warn(message);
            JsonResult jsonResult = JsonResult.fail(ServiceCode.ERR_JWT_MALFORMED, message);
            String jsonResultString = JSON.toJSONString(jsonResult);
            PrintWriter writer = response.getWriter();
            writer.println(jsonResultString);
            writer.close();
            return;
        } catch (Throwable e) {
            String message = "服务器忙，请稍后再次尝试！（开发过程中，如果看到此提示，请检查控制台的信息，并在JWT过滤器补充处理此异常）";
            log.warn(message);
            JsonResult jsonResult = JsonResult.fail(ServiceCode.ERR_UNKNOWN, message);
            String jsonResultString = JSON.toJSONString(jsonResult);
            PrintWriter writer = response.getWriter();
            writer.println(jsonResultString);
            writer.close();
        }

        log.info("claims输出为"+claims);

        Long id = claims.get("id", Long.class);
        String username = claims.get("username", String.class);
        String authoritiesJsonString = claims.get("authoritiesJsonString",String.class);
        log.debug("从JWT中解析得到的管理员ID：{}", id);
        log.debug("从JWT中解析得到的管理员用户名：{}", username);
        log.debug("从JWT中解析得到管理员权限列表的JSON：{}",authoritiesJsonString);

        /**
         * 基于解析JWT的结果创建为Authentication对象
         *
         * 这段代码的主要作用是将解析JWT得到的用户信息创建为一个Authentication对象，
         * 并将该对象存放到SecurityContext中，用于后续的鉴权操作。
         */
        LoginPrincipal loginPrincipal = new LoginPrincipal();
        loginPrincipal.setId(id);
        loginPrincipal.setUsername(username);

        Object principal = loginPrincipal;// 当事人：暂时使用用户名
        ObjectKey credentials = null;// 凭证：应该为null
        //创建了一个SimpleGrantedAuthority对象，并将其添加到authorities集合中，
        // 表示当前用户拥有的权限。
        List<SimpleGrantedAuthority> authorities = JSON.parseArray(authoritiesJsonString,SimpleGrantedAuthority.class);
        //这个token对象表示当前用户已经通过认证，并且拥有指定的凭证和权限。
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                principal, credentials, authorities
        );

        // 将Authentication存入到SecurityContext中
        // SecurityContext是Spring Security中的一个核心概念，用于存储当前用户的认证信息，以便后续的鉴权操作。
        log.debug("向SecurityContext中存入Authentication：{}", authentication);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);

        // 目前的测试结果表现为：
        // 1. 携带有效的JWT，可以访问任何请求（需要删除各处理请求的方法上的获取当事人、检查权限的代码）
        // 2. 成功的处理了某个请求后，在接下来的一段时间里，不携带JWT也可以请求成功
        // 3. 如果重启服务器后，第1次发起的请求就没有携带JWT，会响应403

        // 过滤器链继续执行，相当于“放行”
        log.debug("已经向上下文中 存入当事人信息 放行 ");
        filterChain.doFilter(request, response);
    }
}
