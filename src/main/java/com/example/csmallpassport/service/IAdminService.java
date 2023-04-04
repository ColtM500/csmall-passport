package com.example.csmallpassport.service;

import com.example.csmallpassport.pojo.dto.AdminLoginDTO;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface IAdminService {
    void login(AdminLoginDTO adminLoginDTO);
}

