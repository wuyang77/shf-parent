package com.atguigu.dao;

import com.atguigu.entity.AdminRole;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface AdminRoleDao extends BaseDao<AdminRole> {

  List<Long> findRoleIdsByAdminId(Long adminId);

  void deleteRoleIdsByAdminId(Long adminId);

  void addRoleIdAndAdminId(@Param("roleId") Long roleId,@Param("adminId") Long adminId);
}
