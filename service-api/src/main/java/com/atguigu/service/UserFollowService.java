package com.atguigu.service;

import com.atguigu.entity.UserFollow;
import com.atguigu.vo.UserFollowVo;
import com.github.pagehelper.PageInfo;

public interface UserFollowService extends BaseService<UserFollow>{
  //关注房源
  void follow(Long id, Long houseId);
  //查看是否关注了该房源
  Boolean isFollowed(Long userId, Long houseId);
  //分页查询我关注的房源
  PageInfo<UserFollowVo> findPagelist(Integer pageNum, Integer pageSize, Long userId);
  //取消关注房源
  void cancelFollow(Long id);
}
