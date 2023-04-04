package com.example.csmallpassport.service.impl;

import com.example.csmallpassport.pojo.dto.AdminLoginDTO;
import com.example.csmallpassport.service.IAdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AdminServiceImpl implements IAdminService {
    @Override
    public void login(AdminLoginDTO adminLoginDTO) {
        log.debug("开始处理【管理员登录】的业务，参数：{}",adminLoginDTO);
    }
}
