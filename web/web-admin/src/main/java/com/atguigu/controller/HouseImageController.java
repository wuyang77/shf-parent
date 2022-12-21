package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.entity.HouseImage;
import com.atguigu.result.Result;
import com.atguigu.service.HouseImageService;
import com.atguigu.util.QiniuUtils;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/houseImage")
public class HouseImageController {
  @Reference
  private HouseImageService houseImageService;

  //上传图片的页面
  @RequestMapping("/uploadShow/{houseId}/{type}")
  public String goUploadShow(@PathVariable("houseId")Long houseId,@PathVariable("type")Integer type,Map map){
    map.put("houseId",houseId);
    map.put("type",type);
    return "house/upload";
  }

  //删除房源或房产图片
  @ResponseBody
  @RequestMapping("/upload/{houseId}/{type}")
  public Result upload(@PathVariable("houseId")Long houseId,@PathVariable("type")Integer type, @RequestParam("file")MultipartFile[] files){

    try {
      if(files != null && files.length > 0){
        for (MultipartFile file : files) {
          //获取字节数组
          byte[] bytes = file.getBytes();
          //获取图片的名字
          String originalFilename = file.getOriginalFilename();
          //通过UUID随机生成一个字符串作为上传到七牛云的图片的名字
          String newFileName = UUID.randomUUID().toString();
          //通过七牛工具类上传文件到七牛云
          QiniuUtils.upload2Qiniu(bytes,newFileName);
          //创建HouseImage对象
          HouseImage houseImage = new HouseImage();
          houseImage.setHouseId(houseId);
          houseImage.setType(type);
          houseImage.setImageName(originalFilename);
          //设置图片的路径,路径的格式：http://七牛云的域名/随机生成的图片的名字
          houseImage.setImageUrl("http://rn1s9e76a.hd-bkt.clouddn.com/"+newFileName);
          //调用HouseImageService中保存的方法
          houseImageService.insert(houseImage);
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return Result.ok();
  }

  //删除房源或者房产图片
  @RequestMapping("/delete/{houseId}/{id}")
  public String delete(@PathVariable("houseId")Long houseId,@PathVariable("id")Long id){
    houseImageService.delete(id);
    return "redirect:/house/"+houseId;
  }
}
