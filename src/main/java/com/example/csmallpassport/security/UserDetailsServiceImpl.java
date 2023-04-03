package com.example.csmallpassport.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        log.debug("Spring Security 自动调用了loadUserByUsername()方法 参数：{}", s);

        //假设正确的用户名是root 匹配的密码是1234
        if (!"root".equals(s)){
            log.warn("用户名【{}】错误，将不会返回有效的UserDetails(用户详情)", s);
            return null;
        }

        UserDetails userDetails = User.builder()
                .username("root") //存入用户名
                .password("1234") //存入密码
                .disabled(false) //存入启用，禁用状态
                .accountLocked(false) //存入账号是否锁定的状态
                .credentialsExpired(false) //存入凭证是否过期的状态
                .accountExpired(false) //存入账号是否过期的状态
                .authorities("这是一个临时的山寨权限，暂时没什么用")
                .build();
        log.debug("即将向Spring Security 返回UserDetails类型对象， 返回结果：{}", userDetails);
        return userDetails;

    }
}
