package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.entity.UserInfo;
import com.atguigu.result.Result;
import com.atguigu.result.ResultCodeEnum;
import com.atguigu.service.UserInfoService;
import com.atguigu.util.MD5;
import com.atguigu.vo.LoginVo;
import com.atguigu.vo.RegisterVo;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/userInfo")
public class UserInfoController {
  @Reference
  private UserInfoService userInfoService;

  @RequestMapping("/sendCode/{phone}")
  public Result sendCode(@PathVariable("phone")String phone, HttpServletRequest request) {
    //设置验证码为8888
    String code = "8888";
    //将验证码放到Session中
    request.getSession().setAttribute("code",code);
    //将验证码响应到前端
    return Result.ok(code);
  }


  @RequestMapping("/register")
  public Result register(@RequestBody RegisterVo registerVo,HttpSession session){
    //获取手机号、密码、昵称、验证码
    String phone = registerVo.getPhone();
    String password = registerVo.getPassword();
    String nickName = registerVo.getNickName();
    String code = registerVo.getCode();
    //验空
    if (StringUtils.isEmpty(phone) || StringUtils.isEmpty(password) || StringUtils.isEmpty(nickName)|| StringUtils.isEmpty(code)){
      //返回参数错误信息
      return Result.build(null, ResultCodeEnum.PARAM_ERROR);
    }
    //从Session域中获取验证码
    String sessionCode = (String) session.getAttribute("code");
    //判断验证码是否正确
    if (!code.equals(sessionCode)){
      //返回验证码错误信息
      return Result.build(null, ResultCodeEnum.CODE_ERROR);
    }
    //调用UserInfoService中的方法，判断手机号是否已经注册
    UserInfo userInfo = userInfoService.getUserInfoByPhone(phone);
    if (null != userInfo){
      //返回手机号已经注册的消息
      return Result.build(null, ResultCodeEnum.PHONE_REGISTER_ERROR);
    }
    //创建一个UserInfo对象，然后插入到数据库中
    UserInfo userInfo1 = new UserInfo();
    userInfo1.setPhone(phone);
    userInfo1.setNickName(nickName);
    userInfo1.setPassword(MD5.encrypt(password));
    userInfo1.setStatus(1);
    //调用UserInfoService中插入用户的方法
    userInfoService.insert(userInfo1);
    return Result.ok();
  }

  @RequestMapping("/login")
  public Result login(@RequestBody LoginVo loginVo,HttpSession session){
    //获手机号和密码
    String phone = loginVo.getPhone();
    String password = MD5.encrypt(loginVo.getPassword());
    //验空
    if (StringUtils.isEmpty(password) || StringUtils.isEmpty(phone)){
      return Result.build(null, ResultCodeEnum.PARAM_ERROR);
    }
    //根据手机号查询用户信息
    UserInfo userInfo = userInfoService.getUserInfoByPhone(phone);
    if (null == userInfo){
      //返回账号不正确
      return Result.build(null, ResultCodeEnum.ACCOUNT_ERROR);
    }
    //验证密码是否正确
    if (!password.equals(userInfo.getPassword())){
      //返回密码不正确
      return Result.build(null, ResultCodeEnum.PASSWORD_ERROR);
    }
    //判断用户是否已经被锁定
    if (userInfo.getStatus() == 0){
      //返回用户已被锁定
      return Result.build(null, ResultCodeEnum.ACCOUNT_LOCK_ERROR);
    }
    //登录成功
    //将用户信息保存到session域中
    session.setAttribute("user",userInfo);
    //创建一个Map.Map中必须存放nickName的key,值是用户的昵称
    Map map = new HashMap<>();
    map.put("nickName",userInfo.getNickName());
    map.put("phone",phone);
    return Result.ok(map);
  }

  @RequestMapping("/logout")
  public Result logout(HttpSession session){
    //将session域中userInfo对象移除
    session.removeAttribute("user");
    return Result.ok();
  }
}
