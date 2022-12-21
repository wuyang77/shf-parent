package com.atguigu.dao;

import com.atguigu.entity.RolePermission;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface RolePermissionDao extends BaseDao<RolePermission> {

  List<Long> findPermissionIdsByRoleId(Long roleId);

  void deletePermissionIdsByRoleId(Long roleId);

  void addRoleIdAndPermission(@Param("roleId") Long roleId,@Param("permissionId") Long permissionId);
}
