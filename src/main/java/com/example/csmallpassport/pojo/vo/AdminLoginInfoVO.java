package com.example.csmallpassport.pojo.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 管理员的vo登陆类
 */
@Data
public class AdminLoginInfoVO implements Serializable {

    /**
     * 数据ID
     */
    private Long id;
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码（密文）
     */
    private String password;
    /**
     * 是否启用 1=启用 0=禁用
     */
    private Integer enable;
    /**
     * 权限列表
     */
    private List<String> permissions;
}
