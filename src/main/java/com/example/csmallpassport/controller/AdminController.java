package com.example.csmallpassport.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/admins")
public class AdminController {

    @PostMapping("/add-new")
    public String addNew(){
        return "接收到【添加管理员】的请求， 但是， 服务器端尚未实现此功能!";
    }


    @GetMapping("/delete")
    public String delete(){
        return "接收到【删除管理员】的请求， 但是， 服务器端尚未实现此功能!";
    }

    @PostMapping("/update")
    public String update(){
        return "接收到【修改管理员】的请求， 但是， 服务器端尚未实现此功能!";
    }

    @GetMapping("/{id:[0-9]+}")
    public String getStandardById(@PathVariable Long id){
        return "接收到【查询管理员ID= " + id + "】的请求， 但是， 服务器端尚未实现此功能!";
    }

    @GetMapping("")
    public String list(){
        return "接收到【添加管理员列表】的请求， 但是， 服务器端尚未实现此功能!";
    }
}
