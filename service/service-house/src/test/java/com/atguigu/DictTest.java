package com.atguigu;

import com.atguigu.dao.DictDao;
import com.atguigu.entity.Dict;
import java.util.List;
import javax.annotation.Resource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@ContextConfiguration(locations = "classpath:spring/spring-dao.xml")
@RunWith(SpringRunner.class)
public class DictTest {
  @Resource
  private DictDao dictDao;

  /*
      测试根据父Id查询所有子节点的方法
   */
  @Test
  public void testFindListByParentId(){
    List<Dict> listByParentId = dictDao.findListByParentId(1L);
    for (Dict dict : listByParentId) {
      System.out.println("dict = " + dict);
    }
  }

}
