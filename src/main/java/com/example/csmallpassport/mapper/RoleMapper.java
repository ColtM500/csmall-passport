package com.example.csmallpassport.mapper;

import com.example.csmallpassport.pojo.entity.Role;
import com.example.csmallpassport.pojo.vo.RoleItemListVO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleMapper {

    List<RoleItemListVO> list();
}
