package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.dao.DictDao;
import com.atguigu.entity.Dict;
import com.atguigu.service.DictService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.transaction.annotation.Transactional;

@Service(interfaceClass = DictService.class)
@Transactional
public class DictServiceImpl implements DictService {
  @Resource
  private DictDao dictDao;
  @Override
  public List<Map<String, Object>> findZnodes(Long id) {
    //根据父id查询该节点下所有的子节点
    List<Dict> dictList = dictDao.findListByParentId(id);
    //创建返回的List
    List<Map<String,Object>> zNodes = new ArrayList<>();
    for (Dict dict : dictList) {
      // 返回数据[{ id:2, isParent:true, name:"随意勾选 2"}]
      //传建一个Map
      Map map = new HashMap();
      map.put("id",dict.getId());
      map.put("name",dict.getName());
      //调用DictDao中判断该节点是否是父节点中的方法
      Integer count = dictDao.isParentNode(dict.getId());
      map.put("isParent",count > 0 ? true:false);
      //将Map放入返回的List中
      zNodes.add(map);
    }

    return zNodes;
  }

  @Override
  public List<Dict> findListByParentId(Long parentId) {
    return dictDao.findListByParentId(parentId);
  }

  @Override
  public List<Dict> findListByDictCode(String dictCode) {
    //调用DictDao中根据编码得到Dict对象的方法
    Dict dict = dictDao.getDictByDictCode(dictCode);
    if (dict == null) {
      return null;
    }
    //根据父Id查询所有子节点的方法
    List<Dict> listByParentId = this.findListByParentId(dict.getId());
    return listByParentId;
  }

  @Override
  public String getNameById(Long id) {
    return dictDao.getNameById(id);
  }
}
