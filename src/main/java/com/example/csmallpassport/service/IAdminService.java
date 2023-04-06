package com.example.csmallpassport.service;

import com.example.csmallpassport.pojo.dto.AdminLoginDTO;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface IAdminService {

    /**
     * 验证管理员登录
     * @param adminLoginDTO 管理员的登录信息 至少封装用户名与密码原文
     * @return 验证登录通过后的JWT
     */
    String login(AdminLoginDTO adminLoginDTO);
}

