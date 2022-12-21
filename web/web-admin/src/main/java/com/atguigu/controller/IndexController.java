package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.entity.Admin;
import com.atguigu.entity.Permission;
import com.atguigu.service.AdminService;
import com.atguigu.service.PermissionService;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpSession;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {
  @Reference
  private AdminService adminService;
  @Reference
  private PermissionService permissionService;
  //去首页
//  @RequestMapping("/")
//  public String index(){
//    return "frame/index";
//  }
  @RequestMapping("/")
  public String index(Map map){
    //设置默认用户的id
//    Long userId = 1L;
    //调用AdminService中查询的方法
//    Admin admin = adminService.getById(userId);

    //通过SpringSecurity获取User对象
    User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    //根据用户名获取Admin对象的方法
    Admin admin = adminService.getAdminByUsername(user.getUsername());
    //调用PermissionService,根据用户的id获取用户权限菜单的方法
    List<Permission> permissionList = permissionService.getMenuPermissionByAdminId(admin.getId());
    //将用户的权限菜单放到request域中
    map.put("admin", admin);
    map.put("permissionList", permissionList);
    return "frame/index";
  }



  //去主页面
  @RequestMapping("/main")
  public String main(){
    return "frame/main";
  }

  //去登陆页面
  @RequestMapping("/login")
  public String login(){
    return "frame/login";
  }
  //去没有权限的提示页面
  @RequestMapping("/auth")
  public String auth(){
    return "frame/auth";
  }

  //登出
  @RequestMapping("/logout")
  public String logout(HttpSession session){
    //让session失效
    session.invalidate();
    //重定向到登录的请求
    return "redirect:/login";
  }
}
