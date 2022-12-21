package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.entity.Dict;
import com.atguigu.result.Result;
import com.atguigu.service.DictService;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/dict")
public class DictController {
  @Reference
  private DictService dictService;

  //去展示数据字典
  @RequestMapping
  public String index(){
    return "dict/index";
  }

  @ResponseBody
  //获取数据字典中的数据
  @RequestMapping("/findZnodes")
  public Result findZnodes(@RequestParam(value = "id",defaultValue = "0") Long id){


    //调用DictService中根据父id查询所有
    List<Map<String,Object>> zNodes = dictService.findZnodes(id);

    return Result.ok(zNodes);
  }

  //根据父id所有的子节点
  @ResponseBody
  @RequestMapping("/findListByParentId/{areaId}")
  public Result findListByParenId(@PathVariable("areaId")Long areaId){
    //调用DictService中根据父id查询所有子节点的方法
    List<Dict> listByParentId = dictService.findListByParentId(areaId);
    return  Result.ok(listByParentId);
  }
}
