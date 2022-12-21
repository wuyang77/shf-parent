package com.atguigu.service;

import com.atguigu.entity.HouseUser;
import java.util.List;

public interface HouseUserService extends BaseService<HouseUser>{
  //根据房源Id查询房东信息
  List<HouseUser> getHouseUserByHouseId(Long houseId);
}
