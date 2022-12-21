package com.atguigu.configuration;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.entity.Admin;
import com.atguigu.service.AdminService;
import com.atguigu.service.PermissionService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class MyUserDetailsService implements UserDetailsService {
  @Reference
  private AdminService adminService;
  @Reference
  private PermissionService permissionService;
  //登陆时，SpringSecurity会自动调用该方法，并将用户名传入到该方法中
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    //调用AdminService中根据用户名查询Admin对象的方法
    Admin admin = adminService.getAdminByUsername(username);
    if (admin == null) {
      throw new UsernameNotFoundException("用户不存在！");
    }
    //调用PermissionService中获取当前用户权限的方法
    List<String> permissionCodes = permissionService.getPermissionCodesByAdmin(admin.getId());
    //创建一个List
    List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
    //遍历得到每一个权限码
    for (String permissionCode : permissionCodes) {
      if (!StringUtils.isEmpty(permissionCode)) {
        //创建GrantedAuthority对象
        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(permissionCode);
        //将对象放到权限集合中
        grantedAuthorities.add(simpleGrantedAuthority);
      }
    }
    /*
     * 给用户授权
     *   权限有两种标识方式“
     *      1.通过角色的方式表示，例如：ROLE_ADMIN
     *      2.直接设置权限,例如：Delete、Query、Update
     */

//    return new User(username,admin.getPassword(), AuthorityUtils.commaSeparatedStringToAuthorityList("Select,Delete"));
    return new User(username,admin.getPassword(),grantedAuthorities);
  }
}
