package com.example.csmallpassport.security;

import com.example.csmallpassport.mapper.AdminMapper;
import com.example.csmallpassport.pojo.vo.AdminLoginInfoVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private AdminMapper mapper;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        log.debug("Spring Security 自动调用了loadUserByUsername()方法 参数：{}", s);

        AdminLoginInfoVO loginInfo = mapper.getLoginInfoByUsername(s);
        log.debug("根据用户名【{}】查询登录信息，结果：{}", s, loginInfo);
        //假设正确的用户名是root 匹配的密码是1234
        if (loginInfo==null){
            String message = "用户名不存在， 将无法返回有效的UserDetails对象，则返回null";
            log.warn(message);
            return null;
        }

        log.debug("开始创建返回给Spring Security的UserDetails对象……");
        UserDetails userDetails = User.builder() // 构建者模式
                .username(loginInfo.getUsername()) //存入用户名
                .password(loginInfo.getPassword()) //存入密码
                .disabled(loginInfo.getEnable() != 1) //存入启用，禁用状态
                .accountLocked(false) //存入账号是否锁定的状态
                .credentialsExpired(false) //存入凭证是否过期的状态
                .accountExpired(false) //存入账号是否过期的状态
                .authorities("这是一个临时的山寨权限，暂时没什么用")// 存入权限列表
                .build();// 执行构建，得到UserDetails类型的对象
        log.debug("即将向Spring Security 返回UserDetails类型对象， 返回结果：{}", userDetails);
        return userDetails;

    }
}
