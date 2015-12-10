package com.volkswagen.tel.billing.billcall.jpa.service;

import java.util.List;

import com.volkswagen.tel.billing.billcall.jpa.domain.UserRoleEntity;

public interface UserRoleDaoService {
	public UserRoleEntity saveUserRole(UserRoleEntity entity);
	
	public void deleteUserRoleById(Long id);
	
	public List<UserRoleEntity> getUsersByRoleName(String roleName);
	
	public List<UserRoleEntity> getAllUsers();
	
	public List<UserRoleEntity> getRolesByUserId(String userId);
}
