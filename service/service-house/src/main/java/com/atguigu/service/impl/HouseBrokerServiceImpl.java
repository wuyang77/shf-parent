package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.dao.BaseDao;
import com.atguigu.dao.HouseBrokerDao;
import com.atguigu.entity.HouseBroker;
import com.atguigu.service.HouseBrokerService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Service(interfaceClass = HouseBrokerService.class)
@Transactional
public class HouseBrokerServiceImpl extends BaseServiceImpl<HouseBroker> implements HouseBrokerService {

  @Autowired
  private HouseBrokerDao houseBrokerDao;
  @Override
  protected BaseDao<HouseBroker> getEntityDao() {
    return houseBrokerDao;
  }

  @Override
  public List<HouseBroker> getHouseBrokerByHouseId(Long houseId) {
    return houseBrokerDao.getHouseBrokerByHouseId(houseId);
  }

}
