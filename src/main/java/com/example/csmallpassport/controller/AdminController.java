package com.example.csmallpassport.controller;

import com.example.csmallpassport.pojo.dto.AdminAddNewDTO;
import com.example.csmallpassport.pojo.dto.AdminLoginDTO;
import com.example.csmallpassport.security.LoginPrincipal;
import com.example.csmallpassport.service.IAdminService;
import com.example.csmallpassport.web.JsonResult;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@Slf4j
@RestController
@RequestMapping("/admins")
public class AdminController {
    @Autowired
    private IAdminService service;

    @PostMapping("/login")
    @ApiOperation("管理员登录")
    public JsonResult login(AdminLoginDTO adminLoginDTO) {
        log.debug("开始处理【管理员登录】的请求，参数：{}", adminLoginDTO);
        String jwt = service.login(adminLoginDTO);
        return JsonResult.ok(jwt);
    }

    @PostMapping("/add-new")
    @ApiOperation("添加管理员")
    public JsonResult addNew(AdminAddNewDTO adminAddNewDTO){
        log.debug("开始处理添加管理员请求!");
        service.addNew(adminAddNewDTO);
        return JsonResult.ok();
    }

    @PostMapping("/{id:[0-9]+}/enable")
    @ApiOperation("启用状态")
    public JsonResult setEnable(@PathVariable Long id){
        service.setEnable(id);
        return JsonResult.ok();
    }

    @PostMapping("/{id:[0-9]+}/disable")
    @ApiOperation("禁用状态")
    public JsonResult setDisable(@PathVariable Long id){
        service.setDisable(id);
        return JsonResult.ok();
    }

    @GetMapping("/delete")
    public String delete(){
        return "接收到【删除管理员】的请求， 但是， 服务器端尚未实现此功能!";
    }

    @PostMapping("/update")
    @PreAuthorize("hasAuthority('/ams/admin/update')")
    public String update(){
        return "接收到【修改管理员】的请求， 但是， 服务器端尚未实现此功能!";
    }

    @GetMapping("/{id:[0-9]+}")
    public String getStandardById(@PathVariable Long id){
        return "接收到【查询管理员ID= " + id + "】的请求， 但是， 服务器端尚未实现此功能!";
    }

    @GetMapping("")
    @PreAuthorize("hasAuthority('/ams/admin/read')")
    public String list(@AuthenticationPrincipal @ApiIgnore LoginPrincipal loginPrincipal){
        log.debug("当事人的用户名:{}",loginPrincipal.getUsername());

        return "接收到【添加管理员列表】的请求,但是,服务器端尚未实现此功能!";
    }
}
