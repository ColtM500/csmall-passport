package com.example.csmallpassport.service.impl;

import com.example.csmallpassport.pojo.dto.AdminLoginDTO;
import com.example.csmallpassport.service.IAdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AdminServiceImpl implements IAdminService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public void login(AdminLoginDTO adminLoginDTO) {
        log.debug("开始处理【管理员登录】的业务，参数：{}",adminLoginDTO);

        //创建认证信息对象
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                adminLoginDTO.getUsername(), adminLoginDTO.getPassword()
        );
        //调用认证管理器进行认证
        authenticationManager.authenticate(authentication);
        //如果没有出异常 则表示验证登录成功
        log.debug("验证登录成功!");

    }
}
