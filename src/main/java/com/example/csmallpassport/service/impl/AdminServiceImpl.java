package com.example.csmallpassport.service.impl;

import com.alibaba.fastjson.JSON;
import com.example.csmallpassport.pojo.dto.AdminLoginDTO;
import com.example.csmallpassport.security.AdminDetails;
import com.example.csmallpassport.service.IAdminService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class AdminServiceImpl implements IAdminService {

    @Value("${csmall.jwt.secret-key}")
    private String secretKey;

    @Value("${csmall.jwt.duration-in-minute}")
    private Long durationInMinute;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public String login(AdminLoginDTO adminLoginDTO) {
        log.debug("开始处理【管理员登录】的业务，参数：{}",adminLoginDTO);

        //创建认证信息对象
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                adminLoginDTO.getUsername(), adminLoginDTO.getPassword()
        );
        //调用认证管理器进行认证
        Authentication authenticationResult = authenticationManager.authenticate(authentication);
        log.debug("验证登录成功!返回的Authentication为{}",authenticationResult);
//        //如果没有出异常 则表示验证登录成功 需要将认证信息存入到Security上下文
//        SecurityContext securityContext = SecurityContextHolder.getContext();
//        securityContext.setAuthentication(authenticationResult);

        //处理验证登录成功后的结果中的当事人
        Object principal = authenticationResult.getPrincipal();
        log.debug("获取验证登录成功后的结果中的当事人:{}",principal);
        AdminDetails adminDetails = (AdminDetails) principal;

        //需要写入JWT中的数据
        Map<String, Object> claims = new HashMap<>();
        claims.put("id",adminDetails.getId());
        claims.put("username",adminDetails.getUsername());
        String authoritiesJsonString = JSON.toJSONString(adminDetails.getAuthorities());
        claims.put("authoritiesJsonString", authoritiesJsonString);
        log.debug("即将生成JWT数据，包含的账号信息是:{}",claims);

        //生成JWT 并返回JWT
        Date exp = new Date(System.currentTimeMillis()+durationInMinute*60*1000);
        String jwt = Jwts.builder()
                .setHeaderParam("alg", "HS256")
                .setHeaderParam("typ", "JWT")
                .setClaims(claims)
                .setExpiration(exp)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
        log.debug("生成了JWT数据，并将返回此JWT数据：{}", jwt);
        return jwt;
    }
}
