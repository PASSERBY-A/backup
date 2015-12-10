package com.volkswagen.tel.billing.billcall.jpa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.volkswagen.tel.billing.billcall.jpa.domain.UserPrivateNumberEntity;

public interface UserPrivateNumberRepository extends
		CrudRepository<UserPrivateNumberEntity, Long> {

	@Query("from UserPrivateNumberEntity a where lower(a.userId)=lower(?1)")
	public List<UserPrivateNumberEntity> findPrivateNumbersByUserId(String userId);
	
	@Query("select distinct(a.privateNumber) from UserPrivateNumberEntity a where lower(a.userId)=lower(?1)")
	public List<String> findPrivateNumberStringListByUserId(String userId);
	
}
