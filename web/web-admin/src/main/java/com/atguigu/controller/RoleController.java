package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.entity.Role;
import com.atguigu.service.PermissionService;
import com.atguigu.service.RoleService;
import com.github.pagehelper.PageInfo;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/role")
public class RoleController extends BaseController{

  public static final String SUCCESS_PAGE = "common/successPage";
//  @Autowired
  @Reference
  private RoleService roleService;

  @Reference
  private PermissionService permissionService;

//  @RequestMapping
//  public String index(Map map){
//    //调用RoleService获取所有角色的方法
//    List<Role> roleList = roleService.findAll();
//    //将所有的角色放到request域中
//    map.put("list",roleList);
//    //显示渲染的页面
//    return "role/index";
//  }

  @PreAuthorize("hasAuthority('role.show')")
  //分页及带条件查询的方法
  @RequestMapping
  public String index(Map map,HttpServletRequest request){
    //获取请求参数
    Map<String, Object> filters = getFilters(request);
    //将Filters放到Request域
    map.put("filters",filters);
    //调用RoleSevice中分页及带条件查询的方法
    PageInfo<Role> page = roleService.findPage(filters);
    //将PageInfo对象放到Request域里
    request.setAttribute("page",page);
    return "role/index";
  }
  //去添加角色的页面
  @PreAuthorize("hasAuthority('role.create')")
  @RequestMapping("/create")
  public String goAddpage(){
    return "role/create";
  }

  //添加角色
  @PreAuthorize("hasAuthority('role.create')")
  @RequestMapping("/save")
  public String save(Role role){
    //调用RoleSevice中添加的方法
    roleService.insert(role);
    //重定向到查询所有角色的方法
//    return "redirect:/role";
    //去common下的success
    return SUCCESS_PAGE;
  }
  //删除角色
  @PreAuthorize("hasAuthority('role.delete')")
  @RequestMapping("/delete/{roleId}")
  public String delete(@PathVariable("roleId") Long roleId){
    //调用RoleService中删除的方法
    roleService.delete(roleId);
    //重定向到查询所有角色的方法
    return "redirect:/role";
  }
  //修改角色
  @PreAuthorize("hasAuthority('role.edit')")
  @RequestMapping("/edit/{roleId}")
  public String goEditPage(@PathVariable("roleId") Long roleId,Map map){
    //调用roleService中根据id查询的方法
    Role role = roleService.getById(roleId);
    //将查询的Role放到request域中
    map.put("role",role);
    //去修改的页面
    return "role/edit";
  }

  //更新角色
  @PreAuthorize("hasAuthority('role.edit')")
  @RequestMapping("/update")
  public String update(Role role){
    //调用roleService中更新的方法
    roleService.update(role);
    return SUCCESS_PAGE;
  }

  //去分配权限的页面
  @PreAuthorize("hasAuthority('role.assign')")
  @RequestMapping("/assignShow/{roleId}")
  public String goAssignShowPage(@PathVariable("roleId")Long roleId, Map map){
    //将角色id放到request域中
    map.put("roleId",roleId);
    //调用PermissionService中根据角色id获取权限的方法
    List<Map<String,Object>> zNodes = permissionService.findPermissionByRoleId(roleId);
    //将zNodes放到request域中
    map.put("zNodes",zNodes);
    return "role/assignShow";
  }

  //分配权限
  @PreAuthorize("hasAuthority('role.assign')")
  @RequestMapping("/assignPermission")
  public String assignPermission(@RequestParam("roleId") Long roleId,@RequestParam("permissionIds") Long[] permissionIds){
    //调用PermissionService中分配权限的方法
    permissionService.assignPermission(roleId,permissionIds);
    return "common/successPage";
  }


}
