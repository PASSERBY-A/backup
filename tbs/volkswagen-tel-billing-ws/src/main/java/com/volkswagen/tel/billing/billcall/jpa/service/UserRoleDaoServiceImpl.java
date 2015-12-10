package com.volkswagen.tel.billing.billcall.jpa.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.volkswagen.tel.billing.billcall.jpa.domain.UserRoleEntity;
import com.volkswagen.tel.billing.billcall.jpa.repository.UserRoleRepository;

@Service("userRoleDaoService")
public class UserRoleDaoServiceImpl implements UserRoleDaoService {
	@Autowired
	private UserRoleRepository repository;
	
	@Override
	public UserRoleEntity saveUserRole(UserRoleEntity entity) {
		return repository.save(entity);
	}

	@Override
	public void deleteUserRoleById(Long id) {
		repository.delete(id);
	}

	@Override
	public List<UserRoleEntity> getUsersByRoleName(String roleName) {
		return repository.findUsersByRoleName(roleName);
	}

	@Override
	public List<UserRoleEntity> getRolesByUserId(String userId) {
		return repository.findRolesByUserId(userId);
	}

	@Override
	public List<UserRoleEntity> getAllUsers() {
		return repository.findAllUsers();
	}
	
	

}
