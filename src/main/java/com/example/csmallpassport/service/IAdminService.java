package com.example.csmallpassport.service;

import com.example.csmallpassport.pojo.dto.AdminAddNewDTO;
import com.example.csmallpassport.pojo.dto.AdminLoginDTO;
import com.example.csmallpassport.pojo.entity.Admin;
import com.example.csmallpassport.pojo.vo.AdminListItemVO;
import com.example.csmallpassport.pojo.vo.AdminStandardVO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface IAdminService {

    String[] ENABLE_TEXT = {"禁用","启用"};
    void setEnable(Long id);
    void setDisable(Long id);

    /**
     * 验证管理员登录
     * @param adminLoginDTO 管理员的登录信息 至少封装用户名与密码原文
     * @return 验证登录通过后的JWT
     */
    String login(AdminLoginDTO adminLoginDTO);

    void addNew(AdminAddNewDTO adminAddNewDTO);

    void delete(Long id);

    AdminStandardVO getStandardById(Long id);

    List<AdminListItemVO> list();

}

