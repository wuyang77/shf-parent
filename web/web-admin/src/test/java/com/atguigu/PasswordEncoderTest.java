package com.atguigu;

import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordEncoderTest {

  /**
   * 测试BCryptPasswordEncoder加密器
   */
  @Test
  public void testBCryptPasswordEncoder() {
    //创建加密器对象
    BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
    //对111111进行加密
    String encode = bCryptPasswordEncoder.encode("111111");
    System.out.println("encode = " + encode);
    /**
     * 长度60位
     * 第一次运行的结果：$2a$10$Aq2uD4tc4UEmJixyU5o4ZuuOUP7Dbw1OmC6lrKwXdt98COfhDBRfK
     * 第二次运行的结果：$2a$10$G24ugygWRkhaSvbGGJ.dhOhgq8uI6fqaEBPlCXe2Qb5iXKqmQVpgi
     * 第三次运行的结果：$2a$10$NWBpfoLNQtey/5NxlxmDMeZKkCiC7S87ouklBPx.y69AgRLTXP5/W
     */
    //进行密码匹配
    boolean matches = bCryptPasswordEncoder.matches("111111","$2a$10$Aq2uD4tc4UEmJixyU5o4ZuuOUP7Dbw1OmC6lrKwXdt98COfhDBRfK");
    System.out.println("matches = " + matches);
    boolean matches2 = bCryptPasswordEncoder.matches("111111","$2a$10$G24ugygWRkhaSvbGGJ.dhOhgq8uI6fqaEBPlCXe2Qb5iXKqmQVpgi");
    System.out.println("matches2 = " + matches2);
    boolean matches3 = bCryptPasswordEncoder.matches("111111","$2a$10$NWBpfoLNQtey/5NxlxmDMeZKkCiC7S87ouklBPx.y69AgRLTXP5/W");
    System.out.println("matches3 = " + matches3);
  }

}
