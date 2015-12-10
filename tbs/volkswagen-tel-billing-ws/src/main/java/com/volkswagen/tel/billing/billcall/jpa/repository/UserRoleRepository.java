package com.volkswagen.tel.billing.billcall.jpa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.volkswagen.tel.billing.billcall.jpa.domain.UserRoleEntity;

public interface UserRoleRepository extends
		CrudRepository<UserRoleEntity, Long> {

	@Query("from UserRoleEntity a where lower(a.roleName)=lower(?1)")
	public List<UserRoleEntity> findUsersByRoleName(String roleName);
	
	@Query("from UserRoleEntity a where 1=1")
	public List<UserRoleEntity> findAllUsers();
	
	@Query("from UserRoleEntity a where lower(a.userId)=lower(?1)")
	public List<UserRoleEntity> findRolesByUserId(String userId);
}
