package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.dao.BaseDao;
import com.atguigu.dao.UserFollowDao;
import com.atguigu.entity.UserFollow;
import com.atguigu.service.DictService;
import com.atguigu.service.UserFollowService;
import com.atguigu.vo.UserFollowVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Service(interfaceClass = UserFollowService.class)
@Transactional
public class UserFollowServiceImpl extends BaseServiceImpl<UserFollow> implements UserFollowService {

  @Autowired
  private UserFollowDao userFollowDao;
  @Reference
  private DictService dictService;
  @Override
  protected BaseDao<UserFollow> getEntityDao() {
    return userFollowDao;
  }

  @Override
  public void follow(Long id, Long houseId) {
    //创建UserFollow对象
    UserFollow userFollow = new UserFollow();
    userFollow.setUserId(id);
    userFollow.setHouseId(houseId);
    //调用UserFollowDao中的添加方法
    userFollowDao.insert(userFollow);
  }

  @Override
  public Boolean isFollowed(Long userId, Long houseId) {
    //调用UserFollowDao中是否关注该房源的方法
    Integer count = userFollowDao.getCountByUserIdAndHouseId(userId,houseId);
    if (count > 0){
      //已经关注了该房源
      return true;
    }else {
      return false;
    }
  }

  @Override
  public PageInfo<UserFollowVo> findPagelist(Integer pageNum, Integer pageSize, Long userId) {
    //开启翻页
    PageHelper.startPage(pageNum,pageSize);
    //调用UserFollowDao中分页的方法
    Page<UserFollowVo> page = userFollowDao.findPagelist(userId);
    //遍历page
    for (UserFollowVo userFollowVo : page) {
      //获取房屋的户型
      String houseTypeName = dictService.getNameById(userFollowVo.getHouseTypeId());
      //获取楼层
      String floorName = dictService.getNameById(userFollowVo.getFloorId());
      //获取朝向
      String directionName = dictService.getNameById(userFollowVo.getDirectionId());
      userFollowVo.setHouseTypeName(houseTypeName);
      userFollowVo.setFloorName(floorName);
      userFollowVo.setDirectionName(directionName);
    }
    return new PageInfo<>(page,5);
  }

  @Override
  public void cancelFollow(Long id) {
    //调用UserFollowDao中删除的方法
    userFollowDao.delete(id);
  }

}
