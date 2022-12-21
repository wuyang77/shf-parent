package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.entity.Admin;
import com.atguigu.service.AdminService;
import com.atguigu.service.RoleService;
import com.atguigu.util.QiniuUtils;
import com.github.pagehelper.PageInfo;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/admin")
@SuppressWarnings({"unchecked", "rawtypes"})
public class AdminController extends BaseController{

  @Reference
  private AdminService adminService;
  @Reference
  private RoleService roleService;
  //注入密码加密器
  @Autowired
  private PasswordEncoder passwordEncoder;
  private final static String LIST_ACTION = "redirect:/admin";

  private final static String PAGE_INDEX = "admin/index";
  private final static String PAGE_CREATE = "admin/create";
  private final static String PAGE_EDIT = "admin/edit";
  private final static String PAGE_SUCCESS = "common/successPage";

  /**
   * 列表
   */
  @RequestMapping
  public String findPage(ModelMap model, HttpServletRequest request){
    //获取请求参数
    Map<String, Object> filters = getFilters(request);
    //调用AdminService中分页的方法
    PageInfo<Admin> page = adminService.findPage(filters);

    model.addAttribute("filters",filters);
    model.addAttribute("page",page);

    return PAGE_INDEX;
  }
  /**
   * 进入新增页面
   */
  @GetMapping("/create")
  public String create() {
    return PAGE_CREATE;
  }

  /**
   * 保存新增
   */
  @PostMapping("/save")
  public String save(Admin admin) {
    //对Admin对象中的密码进行加密
    admin.setPassword(passwordEncoder.encode(admin.getPassword()));
    adminService.insert(admin);
    return PAGE_SUCCESS;
  }

  /**
   * 进入编辑页面
   */
  @GetMapping("/edit/{id}")
  public String edit(ModelMap model,@PathVariable Long id) {
    Admin admin = adminService.getById(id);
    model.addAttribute("admin",admin);
    return PAGE_EDIT;
  }

  /**
   * 保存更新
   */
  @PostMapping(value="/update")
  public String update(Admin admin) {

    adminService.update(admin);

    return PAGE_SUCCESS;
  }

  /**
   * 删除
   */
  @GetMapping("/delete/{id}")
  public String delete(@PathVariable Long id) {
    adminService.delete(id);
    return LIST_ACTION;
  }

  @RequestMapping("/uploadShow/{id}")
  public String goUploadPage(@PathVariable Long id,Map map) {
    map.put("id",id);
    return "admin/upload";
  }


  //上传头像
  @RequestMapping ("/upload/{id}")
  public String uploadPage(@PathVariable Long id,@RequestParam(value="file") MultipartFile file){
    try {
      Admin admin = adminService.getById(id);
      String fileName = UUID.randomUUID().toString();
      byte[] bytes = file.getBytes();
      QiniuUtils.upload2Qiniu(bytes,fileName);
      String url = "http://rn1s9e76a.hd-bkt.clouddn.com/"+fileName;
      admin.setHeadUrl(url);
      adminService.update(admin);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return PAGE_SUCCESS;
  }

  //分配角色
  @RequestMapping("/assignShow/{adminId}")
  public String goAssignShowPage(@PathVariable("adminId")Long adminId,ModelMap modelMap) {
    //将用户的id放到request域中
    modelMap.addAttribute("adminId",adminId);
    //调用RoleService中根据用户id查询用户角色的方法
    Map<String, Object> rolesByAdminId = roleService.findRolesByAdminId(adminId);
    //将Map放到request域中
    modelMap.addAllAttributes(rolesByAdminId);
    return "admin/assignShow";
  }

  //分配角色
  @RequestMapping("/assignRole")
  public String assignRole(Long adminId, Long[] roleIds){
    //调用RoleService中分配角色的方法
    roleService.assignRole(adminId,roleIds);
    return "common/successPage";
  }

}
