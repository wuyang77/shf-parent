package com.atguigu.service;

import com.atguigu.entity.HouseImage;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface HouseImageService extends BaseService<HouseImage>{
  //根据房源id和类型查询房源或房产图片
  List<HouseImage> getHouseImageByHouseIdAndType(@Param("houseId")Long houseId,@Param("type")Integer type);
}
