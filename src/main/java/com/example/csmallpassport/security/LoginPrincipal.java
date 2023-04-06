package com.example.csmallpassport.security;

import lombok.Data;

import java.io.Serializable;

@Data
public class LoginPrincipal implements Serializable {

    /**
     * 当事人ID
     */
    private Long id;
    /**
     * 当事人用户名
     */
    private String username;
}
