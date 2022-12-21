package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.entity.Community;
import com.atguigu.entity.Dict;
import com.atguigu.service.CommunityService;
import com.atguigu.service.DictService;
import com.github.pagehelper.PageInfo;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/community")
public class CommunityController extends BaseController{
  @Reference
  private CommunityService communityService;
  @Reference
  private DictService dictService;
  //分页及带条件查询的方法
  @RequestMapping
  public String index(Map map,HttpServletRequest request){
    Map<String, Object> filters = getFilters(request);
    map.put("filters",filters);
    PageInfo<Community> pageInfo = communityService.findPage(filters);
    map.put("page",pageInfo);
    //根据编码获取北京所在区域
    List<Dict> areaList = dictService.findListByDictCode("beijing");
    //将北京所有的区域放到request域中
    map.put("areaList",areaList);
    return "community/index";
  }

  //添加小区页面
  @GetMapping("/create")
  public String goAddPage(Map map){
    //根据编码获取北京所在区域
    List<Dict> areaList = dictService.findListByDictCode("beijing");
    //将北京所有的区域放到request域中
    map.put("areaList",areaList);
    return "community/create";
  }

  //添加小区
  @PostMapping("/save")
  public String save(Community community){
    //调用CommunityService中的方法
    communityService.insert(community);
    //去成功页面
    return "common/successPage";
  }

  //去修改小区的页面
  @GetMapping("/edit/{id}")
  public String goEditPage(@PathVariable("id")Long id, Map map){
    //根据编码获取北京所在区域
    List<Dict> areaList = dictService.findListByDictCode("beijing");
    //将北京所有的区域放到request域中
    map.put("areaList",areaList);
    //调用CommunityService查询的方法
    Community community = communityService.getById(id);
    //将小区放到request域中
    map.put("community",community);
    return "community/edit";
  }

  //更新
  @PostMapping("/update")
  public String update(Community community){
    //调用CommunityService中更新的方法
    communityService.update(community);
    return "common/successPage";
  }

  //删除
  @GetMapping("/delete/{id}")
  public String delete(@PathVariable("id")Long id){
    communityService.delete(id);
    return "redirect:/community";
  }


}
