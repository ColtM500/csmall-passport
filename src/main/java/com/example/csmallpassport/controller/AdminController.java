package com.example.csmallpassport.controller;

import com.example.csmallpassport.pojo.dto.AdminAddNewDTO;
import com.example.csmallpassport.pojo.dto.AdminLoginDTO;
import com.example.csmallpassport.pojo.vo.AdminListItemVO;
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

import java.util.List;

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
    @ApiOperation("删除管理员")
    public JsonResult delete(@PathVariable Long id){
        service.delete(id);
        return JsonResult.ok();
    }

    @PostMapping("/update")
    @PreAuthorize("hasAuthority('/ams/admin/update')")
    public String update(){
        return "接收到【修改管理员】的请求， 但是， 服务器端尚未实现此功能!";
    }

    @GetMapping("/{id:[0-9]+}")
    @ApiOperation("根据id查询管理员信息")
    public JsonResult getStandardById(@PathVariable Long id){
        service.getStandardById(id);
        return JsonResult.ok();
    }

    @GetMapping("")
    @ApiOperation("查询管理员列表")
    @PreAuthorize("hasAuthority('/ams/admin/read')")
    public JsonResult list(@AuthenticationPrincipal @ApiIgnore LoginPrincipal loginPrincipal){
        log.debug("开始处理【查询管理员列表】的请求，参数：无");
        log.debug("当事人信息：{}", loginPrincipal);
        log.debug("当事人信息中的ID：{}", loginPrincipal.getId());
        log.debug("当事人信息中的用户名：{}", loginPrincipal.getUsername());
        List<AdminListItemVO> list = service.list();
        return JsonResult.ok(list);
    }
}
