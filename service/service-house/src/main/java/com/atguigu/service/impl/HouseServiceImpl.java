package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.dao.BaseDao;
import com.atguigu.dao.DictDao;
import com.atguigu.dao.HouseDao;
import com.atguigu.entity.House;
import com.atguigu.service.HouseService;
import com.atguigu.vo.HouseQueryVo;
import com.atguigu.vo.HouseVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import java.io.Serializable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Service(interfaceClass = HouseService.class)
@Transactional
public class HouseServiceImpl extends BaseServiceImpl<House> implements HouseService{

  @Autowired
  private HouseDao houseDao;
  @Autowired
  private DictDao dictDao;
  @Override
  protected BaseDao<House> getEntityDao() {
    return houseDao;
  }

  @Override
  public void publish(Long houseId, Integer status) {
    House house = new House();
    house.setId(houseId);
    house.setStatus(status);
    houseDao.update(house);
  }

  @Override
  public PageInfo<HouseVo> findPageList(Integer pageNum, Integer pageSize, HouseQueryVo houseQueryVo) {
    PageHelper.startPage(pageNum,pageSize);
    //调用HouseDao中前端分页及带条件执行的方法
    Page<HouseVo> page = houseDao.findPageList(houseQueryVo);
    for (HouseVo houseVo : page) {
      //获取房屋的类型
      String houseTypeName = dictDao.getNameById(houseVo.getHouseTypeId());
      //获取楼层
      String floorName = dictDao.getNameById(houseVo.getFloorId());
      //获取房屋的朝向
      String directionName = dictDao.getNameById(houseVo.getDirectionId());
      houseVo.setHouseTypeName(houseTypeName);
      houseVo.setFloorName(floorName);
      houseVo.setDirectionName(directionName);
    }
    return new PageInfo<>(page,5);
  }

  //调用HouseService中前端分页及带条件查询的方法

  //重写该方法是为了展示房源中户型、楼层、朝向
  @Override
  public House getById(Serializable id) {
    House house = houseDao.getById(id);
    //获取户型 
    String houseTypeName = dictDao.getNameById(house.getHouseTypeId());
    //获取楼层
    String floorName = dictDao.getNameById(house.getFloorId());
    //获取朝向
    String directionName = dictDao.getNameById(house.getDirectionId());
    //获取建筑结构
    String buildStructureName = dictDao.getNameById(house.getBuildStructureId());
    //获取装修情况
    String decorationName = dictDao.getNameById(house.getDecorationId());
    //获取房屋用途
    String houseUseName = dictDao.getNameById(house.getHouseUseId());
    //设置
    house.setHouseTypeName(houseTypeName);
    house.setFloorName(floorName);
    house.setDirectionName(directionName);
    house.setBuildStructureName(buildStructureName);
    house.setDecorationName(decorationName);
    house.setHouseUseName(houseUseName);
    return house;
  }
}
