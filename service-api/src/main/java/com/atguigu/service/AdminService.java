package com.atguigu.service;

import com.atguigu.entity.Admin;
import java.util.List;

public interface AdminService extends BaseService<Admin> {

  List<Admin> findAll();

  Integer insert(Admin admin);

  Admin getAdminByUsername(String username);
}
