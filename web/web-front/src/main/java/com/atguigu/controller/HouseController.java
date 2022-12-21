package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.entity.Community;
import com.atguigu.entity.House;
import com.atguigu.entity.HouseBroker;
import com.atguigu.entity.HouseImage;
import com.atguigu.entity.UserInfo;
import com.atguigu.result.Result;
import com.atguigu.service.CommunityService;
import com.atguigu.service.HouseBrokerService;
import com.atguigu.service.HouseImageService;
import com.atguigu.service.HouseService;
import com.atguigu.service.UserFollowService;
import com.atguigu.vo.HouseQueryVo;
import com.atguigu.vo.HouseVo;
import com.github.pagehelper.PageInfo;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/house")
public class HouseController {

  @Reference
  private HouseService houseService;
  @Reference
  private CommunityService communityService;

  @Reference
  private HouseImageService houseImageService;

  @Reference
  private HouseBrokerService houseBrokerService;

  @Reference
  private UserFollowService userFollowService;
  //分页及带条件的查询方法
  @RequestMapping("/list/{pageNum}/{pageSize}")
  public Result findPageList(@PathVariable("pageNum")Integer pageNum, @PathVariable("pageSize") Integer pageSize,
                             @RequestBody HouseQueryVo houseQueryVo){
    //调用HouseService中前端分页及带条件查询的方法
    PageInfo<HouseVo> pageInfo = houseService.findPageList(pageNum,pageSize,houseQueryVo);
    return Result.ok(pageInfo);
  }
  //查询房源详情
  @RequestMapping("/info/{id}")
  public Result info(@PathVariable("id")Long id, HttpSession session){
    //查询房源信息
    House house = houseService.getById(id);
    //获取小区信息
    Community community = communityService.getById(house.getCommunityId());
    //获取房源的房源图片
    List<HouseImage> houseImage1List = houseImageService.getHouseImageByHouseIdAndType(id, 1);
    //获取房源的经纪人
    List<HouseBroker> houseBrokerList = houseBrokerService.getHouseBrokerByHouseId(id);
    //创建一个Map
    Map map = new HashMap<>();
    //将房源信息、小区信息、房源图片和房源的经纪人放到Map中
    map.put("house",house);
    map.put("community",community);
    map.put("houseImage1List",houseImage1List);
    map.put("houseBrokerList",houseBrokerList);
    //设置默认没有关注的房源
//    map.put("isFollow",false);
    //设置一个变量
    Boolean isFollowed = false;
    //从Session域中获取UserInfo对象
    UserInfo userInfo = (UserInfo) session.getAttribute("user");
    if (userInfo != null) {
      //证明已经登录，调用UserFollowService中查询是否关注房源的方法
      isFollowed = userFollowService.isFollowed(userInfo.getId(),id);
    }
    //将isFollowed放到Map中
    map.put("isFollow",isFollowed);
    return Result.ok(map);

  }
}
