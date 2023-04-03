package com.example.csmallpassport.mapper;

import com.example.csmallpassport.pojo.entity.Admin;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@SpringBootTest
public class AdminMapperTests {

    @Autowired
    AdminMapper mapper;

    @Test
    void insert() {
        Admin admin = new Admin();
        admin.setUsername("汪诗意");
        admin.setPassword("123456");
        admin.setPhone("10086");
        admin.setEmail("10086@gmail.com");

        log.debug("插入数据之前，参数：{}", admin);
        int rows = mapper.insert(admin);
        log.debug("插入数据完成，受影响的行数：{}", rows);
        log.debug("插入数据之后，参数：{}", admin);
    }

    @Test
    void insertBatch() {
        List<Admin> admins = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            Admin admin = new Admin();
            admin.setUsername("test-admin-" + i);
            admins.add(admin);
        }

        int rows = mapper.insertBatch(admins);
        log.debug("批量插入完成，受影响的行数：{}", rows);
    }

    @Test
    void deleteById() {
        Long id = 6L;
        int rows = mapper.deleteById(id);
        log.debug("删除完成，受影响的行数：{}", rows);
    }

    @Test
    void deleteByIds() {
        Long[] ids = {7L, 9L, 11L};
        int rows = mapper.deleteByIds(ids);
        log.debug("批量删除完成，受影响的行数：{}", rows);
    }

    @Test
    void update() {
        Admin admin = new Admin();
        admin.setId(8L);
        admin.setNickname("新-测试数据001");

        int rows = mapper.update(admin);
        log.debug("更新完成，受影响的行数：{}", rows);
    }

    @Test
    void count() {
        int count = mapper.count();
        log.debug("统计完成，表中的数据的数量：{}", count);
    }

    @Test
    void countByUsername() {
        String username = "wangkejing";
        int count = mapper.countByUserName(username);
        log.debug("根据用户名【{}】统计管理员账号的数量：{}", username, count);
    }

    @Test
    void countByPhone() {
        String phone = "13900139001";
        int count = mapper.countByPhone(phone);
        System.out.println(phone+","+ count);
    }

    @Test
    void countByEmail() {
        String email = "wangkejing@qq.com";
        int count = mapper.countByEmail(email);
        log.debug("根据电子邮箱【{}】统计管理员账号的数量：{}", email, count);
    }

    @Test
    void getStandardById() {
        Long id = 1L;
        Object queryResult = mapper.getStandardById(id);
        log.debug("根据id【{}】查询数据详情完成，查询结果：{}", id, queryResult);
    }

    @Test
    void list() {
        List<?> list = mapper.list();
        log.debug("查询列表完成，列表中的数据的数量：{}", list.size());
        for (Object item : list) {
            System.out.println(item);
        }
    }

    @Test
    void getLoginInfoByUsername(){
        String username = "root";
        Object queryResult = mapper.getLoginInfoByUsername(username);
//        log.debug("根据username【{}】查询数据详情完成，查询结果：{}", username, queryResult);
        System.out.println("根据username【{"+username+"}】查询数据详情完成，查询结果：{"+queryResult+"}");
    }
}
