package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.entity.UserFollow;
import com.atguigu.entity.UserInfo;
import com.atguigu.result.Result;
import com.atguigu.service.UserFollowService;
import com.atguigu.vo.UserFollowVo;
import com.github.pagehelper.PageInfo;
import javax.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/userFollow")
public class UserFollowController {
  @Reference
  private UserFollowService userFollowService;
  //关注房源
  @RequestMapping("/auth/follow/{houseId}")
  public Result follow(@PathVariable("houseId")Long houseId, HttpSession session){
    //获取UserInfo对象
    UserInfo userInfo = (UserInfo) session.getAttribute("user");
    userFollowService.follow(userInfo.getId(),houseId);
    return Result.ok();
  }

  //查询我的关注
  @RequestMapping("/auth/list/{pageNum}/{pageSize}")
  public Result myFollowed(@PathVariable("pageNum")Integer pageNum,@PathVariable("pageSize")Integer pageSize,HttpSession session){
    //获取UserInfo对象
    UserInfo userInfo = (UserInfo) session.getAttribute("user");
    //调用FollowService中分页查询的方法
    PageInfo<UserFollowVo> pageInfo = userFollowService.findPagelist(pageNum,pageSize,userInfo.getId());
    return Result.ok(pageInfo);
  }
  //取消关注
  @RequestMapping("/auth/cancelFollow/{id}")
  public Result cancellFollowed(@PathVariable("id")Long id){
    //调用UserInfoService取消关注的方法
    userFollowService.cancelFollow(id);
    return Result.ok();
  }
}
