package com.example.csmallpassport.pojo.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class RoleItemListVO implements Serializable {

    private Long id;

    private String name;

    private String description;

    private Integer sort;
}
