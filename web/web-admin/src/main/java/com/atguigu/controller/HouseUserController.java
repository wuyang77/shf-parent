package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.entity.HouseUser;
import com.atguigu.service.HouseUserService;
import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/houseUser")
public class HouseUserController {

  @Reference
  private HouseUserService houseUserService;

  //添加房东信息的的页面
  @RequestMapping("/create")
  public String goAddPage(@RequestParam("houseId")Long houseId, Map map){
    map.put("houseId",houseId);
    return "houseUser/create";
  }

  //保存房东信息
  @RequestMapping("/save")
  public String save(HouseUser houseUser){
    houseUserService.insert(houseUser);
    return "common/successPage";
  }

  //去修改的页面
  @RequestMapping(value = "/edit/{id}")
  public String goEditPage(@PathVariable("id")Long id,Map map){
    HouseUser houseUser = houseUserService.getById(id);
    map.put("houseUser",houseUser);
    return "houseUser/edit";
  }

  //更新页面
  @RequestMapping("/update")
  public String update(HouseUser houseUser){
    houseUserService.update(houseUser);
    return "common/successPage";
  }

  //删除页面
  @RequestMapping("/delete/{houseId}/{houseUserId}")
  public String delete(@PathVariable("houseId")Long houseId,@PathVariable("houseUserId")Long houseUserId){
    houseUserService.delete(houseUserId);
    return "redirect:/house/"+houseId;
  }
}
