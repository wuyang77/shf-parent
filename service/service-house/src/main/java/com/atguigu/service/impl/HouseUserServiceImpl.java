package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.dao.BaseDao;
import com.atguigu.dao.HouseUserDao;
import com.atguigu.entity.HouseUser;
import com.atguigu.service.HouseUserService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Service(interfaceClass = HouseUserService.class)
@Transactional
public class HouseUserServiceImpl extends BaseServiceImpl<HouseUser> implements HouseUserService {

  @Autowired
  private HouseUserDao houseUserDao;

  @Override
  protected BaseDao<HouseUser> getEntityDao() {
    return houseUserDao;
  }

  @Override
  public List<HouseUser> getHouseUserByHouseId(Long houseId) {
    return houseUserDao.getHouseUserByHouseId(houseId);
  }
}
