package com.example.csmallpassport.filter;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        //根据业内惯用的做法 客户端应该将jwt保存在请求头(Request Header)中的名为Authorization的属性名
        String jwt = request.getHeader("Authorization");
        log.debug("尝试接收客户端携带的JWT数据，JWT：{}",jwt);

        filterChain.doFilter(request, response);
    }
}
