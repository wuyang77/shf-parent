package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.entity.Dict;
import com.atguigu.result.Result;
import com.atguigu.service.DictService;
import java.util.List;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dict")
public class DictController {

  @Reference
  DictService dictService;

  //根据编码获取所有子节点
  @RequestMapping("/findListByDictCode/{dictCode}")
  public Result findListByDictCode(@PathVariable("dictCode") String dictCode) {
    List<Dict> listByDictCode = dictService.findListByDictCode(dictCode);
    return Result.ok(listByDictCode);
  }

  //根据父节点查询所有的子节点
  @RequestMapping("/findListByParentCode/{areaId}")
  public Result findListByParentCode(@PathVariable("areaId") Long areaId) {
    List<Dict> listByParentId = dictService.findListByParentId(areaId);
    return Result.ok(listByParentId);
  }
}