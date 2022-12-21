package com.atguigu.dao;

import com.atguigu.entity.Admin;
import java.util.List;

public interface AdminDao extends BaseDao<Admin> {

  List<Admin> findAll();
  Integer insert(Admin admin);

  Admin getAdminByUsername(String username);
}

