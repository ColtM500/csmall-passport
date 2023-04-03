package com.example.csmallpassport.mapper;


import com.example.csmallpassport.pojo.entity.AdminRole;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminRoleMapper {

    int insertBatch(List<AdminRole> adminRoleList);

    int deleteById(Long id);
}
