package com.atguigu.dao;

import com.atguigu.entity.HouseBroker;
import com.github.pagehelper.Page;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface HouseBrokerDao extends BaseDao<HouseBroker>{
  //根据房源id查询该房源的经济人
  List<HouseBroker> getHouseBrokerByHouseId(Long houseId);


}
