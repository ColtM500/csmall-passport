package com.example.csmallpassport.mapper;

import com.example.csmallpassport.pojo.entity.AdminRole;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@SpringBootTest
public class AdminRoleMapperTests {
    @Autowired
    AdminRoleMapper mapper;

    @Test
    void insertBatch() {
        List<AdminRole> adminRoles = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            AdminRole adminRole = new AdminRole();
            adminRole.setAdminId(10L+i);
            adminRole.setRoleId(10L);
            adminRoles.add(adminRole);
        }

        int rows = mapper.insertBatch(adminRoles);
        System.out.println(rows);
    }

    @Test
    void deleteByAdminId() {
        Long adminId = 6L;
        int rows = mapper.deleteById(adminId);
        log.debug("删除完成，受影响的行数：{}", rows);
    }
}
