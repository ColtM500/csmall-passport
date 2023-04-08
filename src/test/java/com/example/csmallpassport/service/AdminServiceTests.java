package com.example.csmallpassport.service;

import com.example.csmallpassport.pojo.dto.AdminAddNewDTO;
import com.example.csmallpassport.pojo.dto.AdminLoginDTO;
import com.example.csmallpassport.pojo.vo.AdminListItemVO;
import com.sun.corba.se.spi.ior.ObjectKey;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@Slf4j
@SpringBootTest
public class AdminServiceTests {
    @Autowired
    IAdminService service;

    @Test
    void login(){
        AdminLoginDTO adminLoginInfoDTO = new AdminLoginDTO();
        adminLoginInfoDTO.setUsername("liucangsong");
        adminLoginInfoDTO.setPassword("123456");

        try {
            String jwt = service.login(adminLoginInfoDTO);
            log.debug("登录成功，JWT：{}", jwt);
        } catch (Throwable e) {
            // 由于不确定Spring Security会抛出什么类型的异常
            // 所以，捕获的是Throwable
            // 并且，在处理时，应该打印信息，以了解什么情况下会出现哪种异常
            e.printStackTrace();
        }
    }

    @Test
    void addNew(){
        AdminAddNewDTO adminAddNewDTO = new AdminAddNewDTO();
        adminAddNewDTO.setUsername("w");
        adminAddNewDTO.setPassword("w");
        adminAddNewDTO.setPhone("1846w");
        adminAddNewDTO.setEmail("1w846@qq.com");

        service.addNew(adminAddNewDTO);
        log.debug("添加数据完成!");
    }

    @Test
    void setEnable(){
        Long id = 1L;

        service.setEnable(id);
        log.debug("启用状态完成!");
    }

    @Test
    void setDisable(){
        Long id = 1L;

        service.setDisable(id);
        log.debug("禁用状态完成!");
    }

    @Test
    void getStandardById(){
        Long id = 1L;
        service.getStandardById(id);
        log.debug("根据id：{}查询完成",id);
    }

    @Test
    void list(){
        List<?> list = service.list();
        log.debug("查询管理员");
        for (Object item : list){
            log.debug("{}", item);
        }
    }

    @Test
    void delete(){
        Long id = 1L;

        try {
            service.delete(id);
            log.debug("删除管理员完成!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
