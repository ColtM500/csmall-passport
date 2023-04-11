package com.example.csmallpassport.controller;

import com.example.csmallpassport.pojo.dto.AdminAddNewDTO;
import com.example.csmallpassport.pojo.dto.AdminLoginDTO;
import com.example.csmallpassport.pojo.vo.AdminListItemVO;
import com.example.csmallpassport.security.LoginPrincipal;
import com.example.csmallpassport.service.IAdminService;
import com.example.csmallpassport.web.JsonResult;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.ApiImplicitParam;
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

    public AdminController() {
        log.debug("创建控制器类对象：AdminController");
    }

    // http://localhost:6090/admins/login
    @ApiOperation("管理员登录")
    @ApiOperationSupport(order = 10)
    @PostMapping("/login")
    public JsonResult login(AdminLoginDTO adminLoginDTO) {
        log.debug("开始处理【管理员登录】的请求，参数：{}", adminLoginDTO);
        String jwt = service.login(adminLoginDTO);
        return JsonResult.ok(jwt);
    }

    // http://localhost:6090/admins/add-new
    @ApiOperation("添加管理员")
    @ApiOperationSupport(order = 100)
    @PreAuthorize("hasAuthority('/ams/admin/add-new')")
    @PostMapping("/add-new")
    public JsonResult addNew(AdminAddNewDTO adminAddNewDTO) {
        log.debug("开始处理【添加管理员】的请求，参数：{}", adminAddNewDTO);
        service.addNew(adminAddNewDTO);
        return JsonResult.ok();
    }

    // http://localhost:6090/admins/9527/delete
    @ApiOperation("根据ID删除管理员")
    @ApiOperationSupport(order = 200)
    @ApiImplicitParam(name = "id", value = "管理员ID", required = true, dataType = "long")
    @PreAuthorize("hasAuthority('/ams/admin/delete')")
    @PostMapping("/{id:[0-9]+}/delete")
    public JsonResult delete(@PathVariable Long id) {
        log.debug("开始处理【根据ID删除管理员】的请求，参数：{}", id);
        service.delete(id);
        return JsonResult.ok();
    }

    // http://localhost:6090/admins/9527/enable
    @ApiOperation("启用管理员")
    @ApiOperationSupport(order = 310)
    @ApiImplicitParam(name = "id", value = "管理员ID", required = true, dataType = "long")
    @PreAuthorize("hasAuthority('/ams/admin/update')")
    @PostMapping("/{id:[0-9]+}/enable")
    public JsonResult setEnable(@PathVariable Long id) {
        log.debug("开始处理【启用管理员】的请求，参数：{}", id);
        service.setEnable(id);
        return JsonResult.ok();
    }

    // http://localhost:6090/admins/9527/disable
    @ApiOperation("禁用管理员")
    @ApiOperationSupport(order = 311)
    @ApiImplicitParam(name = "id", value = "管理员ID", required = true, dataType = "long")
    @PreAuthorize("hasAuthority('/ams/admin/update')")
    @PostMapping("/{id:[0-9]+}/disable")
    public JsonResult setDisable(@PathVariable Long id) {
        log.debug("开始处理【禁用管理员】的请求，参数：{}", id);
        service.setDisable(id);
        return JsonResult.ok();
    }

    // http://localhost:6090/admins
    @ApiOperation("查询管理员列表")
    @ApiOperationSupport(order = 420)
    @PreAuthorize("hasAuthority('/ams/admin/read')")
    @GetMapping("")
    public JsonResult list(
            @ApiIgnore @AuthenticationPrincipal LoginPrincipal loginPrincipal) {
        log.debug("开始处理【查询管理员列表】的请求，参数：无");
        // log.debug("当事人信息：{}", loginPrincipal);
        // log.debug("当事人信息中的ID：{}", loginPrincipal.getId());
        // log.debug("当事人信息中的用户名：{}", loginPrincipal.getUsername());
        List<AdminListItemVO> list = service.list();
        return JsonResult.ok(list);
    }
}
