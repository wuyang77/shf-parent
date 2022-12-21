package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.dao.AdminDao;
import com.atguigu.dao.BaseDao;
import com.atguigu.entity.Admin;
import com.atguigu.service.AdminService;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Service(interfaceClass = AdminService.class)
@Transactional
public class AdminServiceImpl extends BaseServiceImpl<Admin> implements AdminService {
//  @Autowired
  @Autowired
  private AdminDao adminDao;
  @Override
  protected BaseDao<Admin> getEntityDao() {
    return this.adminDao;
  }

  @Override
  public List<Admin> findAll() {
    return adminDao.findAll();
  }

  @Override
  public Integer insert(Admin admin) {
    return adminDao.insert(admin);
  }

  @Override
  public Admin getAdminByUsername(String username) {
    return adminDao.getAdminByUsername(username);
  }
}
