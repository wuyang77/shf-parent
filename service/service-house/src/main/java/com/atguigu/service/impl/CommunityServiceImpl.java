package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.dao.BaseDao;
import com.atguigu.dao.CommunityDao;
import com.atguigu.dao.DictDao;
import com.atguigu.entity.Community;
import com.atguigu.service.CommunityService;
import com.atguigu.util.CastUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import org.springframework.transaction.annotation.Transactional;

@Service(interfaceClass = CommunityService.class)
@Transactional
public class CommunityServiceImpl extends BaseServiceImpl<Community> implements CommunityService {

  @Autowired
  private CommunityDao communityDao;

  @Autowired
  private DictDao dictDao;

  @Override
  protected BaseDao<Community> getEntityDao() {
    return communityDao;
  }
  //重写分页的方法，目的是为了给小区中的区域和板块赋值
  @Override
  public PageInfo<Community> findPage(Map<String, Object> filters) {
    //当前页数
    int pageNum = CastUtil.castInt(filters.get("pageNum"), 1);
    //每页显示的记录条数
    int pageSize = CastUtil.castInt(filters.get("pageSize"), 10);

    PageHelper.startPage(pageNum, pageSize);
    Page<Community> page = communityDao.findPage(filters);
    List<Community> list = page.getResult();
    for(Community community : list) {
      String areaName = dictDao.getNameById(community.getAreaId());
      String plateName = dictDao.getNameById(community.getPlateId());
      community.setAreaName(areaName);
      community.setPlateName(plateName);
    }
    return new PageInfo<Community>(page, 10);

  }

  @Override
  public List<Community> findAll() {
    return communityDao.findAll();
  }

  @Override
  public Community getById(Serializable id) {
    //调用CommunityDao中根据id获取小区的方法
    Community community = communityDao.getById(id);
    String areaName = dictDao.getNameById(community.getAreaId());
    String plateName = dictDao.getNameById(community.getPlateId());
    community.setAreaName(areaName);
    community.setPlateName(plateName);
    return community;
  }

}