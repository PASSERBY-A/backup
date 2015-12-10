package com.volkswagen.tel.billing.billcall.jpa.service;

import java.util.List;

import com.volkswagen.tel.billing.billcall.jpa.domain.UserPrivateNumberEntity;

public interface UserPrivateNumberDaoService {
	
	public List<UserPrivateNumberEntity> findPrivateNumbersByUserId(String userId);
	
	public List<String> findPrivateNumberStringListByUserId(String userId);
	
	public UserPrivateNumberEntity savePrivateNumber(UserPrivateNumberEntity entity);
	
	public void deletePrivateNumberById(Long id);
}
