package com.example.csmallpassport.mapper;

import com.example.csmallpassport.pojo.entity.Admin;
import com.example.csmallpassport.pojo.vo.AdminListItemVO;
import com.example.csmallpassport.pojo.vo.AdminStandardVO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 处理管理员数据的Mapper接口
 */
@Repository
public interface AdminMapper {

    int insert(Admin admin);

    int insertBatch(List<Admin> admins);

    int deleteById(Long id);

    int deleteByIds(Long[] ids);

    int update(Admin admin);

    int count();

    int countByUserName(String username);

    int countByPhone(String phone);

    int countByEmail(String email);

    AdminStandardVO getStandardById(Long id);

    List<AdminListItemVO> list();
}
