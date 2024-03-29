package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.dao.BaseDao;
import com.atguigu.dao.UserInfoDao;
import com.atguigu.entity.UserInfo;
import com.atguigu.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Service(interfaceClass = UserInfoService.class)
@Transactional
public class UserInfoServiceImpl extends BaseServiceImpl<UserInfo> implements UserInfoService{
  @Autowired
  private UserInfoDao userInfoDao;
  @Override
  protected BaseDao<UserInfo> getEntityDao() {
    return userInfoDao;
  }

  @Override
  public UserInfo getUserInfoByPhone(String phone) {
    UserInfo userInfo = userInfoDao.getUserInfoByPhone(phone);
    return userInfo;
  }
}
