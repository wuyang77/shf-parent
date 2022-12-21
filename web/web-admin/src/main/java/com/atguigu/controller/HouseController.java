package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.entity.Community;
import com.atguigu.entity.Dict;
import com.atguigu.entity.House;
import com.atguigu.entity.HouseBroker;
import com.atguigu.entity.HouseImage;
import com.atguigu.entity.HouseUser;
import com.atguigu.result.Result;
import com.atguigu.service.CommunityService;
import com.atguigu.service.DictService;
import com.atguigu.service.HouseBrokerService;
import com.atguigu.service.HouseImageService;
import com.atguigu.service.HouseService;
import com.atguigu.service.HouseUserService;
import com.github.pagehelper.PageInfo;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.io.filefilter.FalseFileFilter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/house")
public class HouseController extends BaseController{
  @Reference
  private HouseService houseService;

  @Reference
  private CommunityService communityService;

  @Reference
  private DictService dictService;

  @Reference
  private HouseBrokerService houseBrokerService;

  @Reference
  private HouseImageService houseImageService;

  @Reference
  private HouseUserService houseUserService;

  //分页及带条件的的查询
  @RequestMapping
  public String index(Map map, HttpServletRequest request){

    Map<String, Object> filters = getFilters(request);
    map.put("filters",filters);
    PageInfo<House> pageInfo = houseService.findPage(filters);
    map.put("page",pageInfo);
    setRequestAttribute(map);
    return "house/index";
  }

    //去添加房源的页面
    @RequestMapping("/create")
    public String goAddPage(Map map){

      setRequestAttribute(map);
      return "house/create";
    }

    //添加房源
    @RequestMapping("/save")
    public String save(House house){

      houseService.insert(house);
      return "common/successPage";
    }

  //去修改的页面
  @RequestMapping("/edit/{id}")
  public String goEditPage(@PathVariable("id")Long id, Map map){

    House house = houseService.getById(id);
    map.put("house",house);
    setRequestAttribute(map);
    return "house/edit";
  }

  //更新
  @RequestMapping("/update")
  public String update(House house){
    //调用HouseService中更新的方法
    houseService.update(house);
    return "common/successPage";
  }

  //删除
  @RequestMapping("/delete/{id}")
  public String delete(@PathVariable("id")Long id){

    houseService.delete(id);
    return "redirect:/house";
  }

  //获取所有小区及数据字典数据放到Request的方法
  public void setRequestAttribute(Map map){
    //获取所有的小区
    List<Community> communityList = communityService.findAll();
    //获取所有的户型
    List<Dict> houseTypeList = dictService.findListByDictCode("houseType");
    //获取楼层
    List<Dict> floorList = dictService.findListByDictCode("floor");
    //获取建筑结构
    List<Dict> buildStructureList = dictService.findListByDictCode("buildStructure");
    //获取朝向
    List<Dict> directionList = dictService.findListByDictCode("direction");
    //获取装修情况
    List<Dict> decorationList = dictService.findListByDictCode("decoration");
    //获取房屋用途
    List<Dict> houseUseList = dictService.findListByDictCode("houseUse");
    map.put("communityList",communityList);
    map.put("houseTypeList",houseTypeList);
    map.put("floorList",floorList);
    map.put("buildStructureList",buildStructureList);
    map.put("directionList",directionList);
    map.put("decorationList",decorationList);
    map.put("houseUseList",houseUseList);
  }

  //发布和取消发布
  @RequestMapping("/publish/{houseId}/{status}")
  public String publish(@PathVariable("houseId")Long houseId,@PathVariable("status")Integer status){
    houseService.publish(houseId,status);
    return "redirect:/house";
  }
  //查看房源详情
  @RequestMapping("/{houseId}")
  public String show(@PathVariable("houseId")Long houseId,Map map){

    House house = houseService.getById(houseId);
    //将房源信息放入到request中
    map.put("house",house);

    Community community = communityService.getById(house.getCommunityId());
    //将小区信息放置到request中
    map.put("community",community);

    List<HouseImage> houseImages = houseImageService.getHouseImageByHouseIdAndType(houseId, 1);
    //将房源图片放到Request域中
    map.put("houseImage1List",houseImages);

    List<HouseBroker> houseBrokers= houseBrokerService.getHouseBrokerByHouseId(houseId);
    //将经纪人放到Request域中
    map.put("houseBrokerList",houseBrokers);

    List<HouseUser> houseUsers = houseUserService.getHouseUserByHouseId(houseId);
    //将房主放到Request域中
    map.put("houseUserList",houseUsers);

    return "house/show";
  }



}
