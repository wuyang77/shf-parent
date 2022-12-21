package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.entity.Admin;
import com.atguigu.entity.HouseBroker;
import com.atguigu.service.AdminService;
import com.atguigu.service.HouseBrokerService;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/houseBroker")
public class HouseBrokerController {

  @Reference
  private AdminService adminService;
  @Reference
  private HouseBrokerService houseBrokerService;

  //去添加经纪人的页面
  @RequestMapping("/create")
  public String goAddPage(@RequestParam("houseId")Long houseId, Map map){
    //将房源的id放到request域中
    map.put("houseId",houseId);
    //调用AdminService中获取所有用户的方法
    List<Admin> adminList = adminService.findAll();
    //将所欲用户放到request域中
    map.put("adminList",adminList);
    return "houseBroker/create";
  }

  //保存经纪人
  @RequestMapping("/save")
  public String save(HouseBroker houseBroker){
    //根据经纪人的id查询经纪人的完整信息
    Admin admin = adminService.getById(houseBroker.getBrokerId());
    houseBroker.setBrokerHeadUrl(admin.getHeadUrl());
    houseBroker.setBrokerName(admin.getName());
    //调用HouseBrokerService中保存的方法
    houseBrokerService.insert(houseBroker);
    //去成功页面
    return "common/successPage";
  }

  //去修改经济人页面
  @RequestMapping("/edit/{id}")
  public String goEditPage(@PathVariable("id")Long id,Map map){

    HouseBroker houseBroker = houseBrokerService.getById(id);
    List<Admin> adminList = adminService.findAll();
    map.put("houseBroker",houseBroker);
    map.put("adminList",adminList);
    return "houseBroker/edit";
  }

  //修改成功页面
  @RequestMapping("/update")
  public String update(HouseBroker houseBroker){
    Admin admin = adminService.getById(houseBroker.getBrokerId());
    houseBroker.setBrokerHeadUrl(admin.getHeadUrl());
    houseBroker.setBrokerName(admin.getName());
    houseBrokerService.update(houseBroker);
    return "common/successPage";
  }

  //删除经济人
  @RequestMapping("/delete/{houseId}/{brokerId}")
  public String delete(@PathVariable("houseId")Long houseId,@PathVariable("brokerId")Long brokerId){
    houseBrokerService.delete(brokerId);
    return "redirect:/house/"+houseId;
  }

}
