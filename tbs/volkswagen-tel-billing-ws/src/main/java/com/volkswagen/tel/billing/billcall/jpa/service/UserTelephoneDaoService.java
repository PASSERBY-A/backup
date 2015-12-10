package com.volkswagen.tel.billing.billcall.jpa.service;

import java.util.List;

import org.springframework.data.domain.Sort.Direction;

import com.volkswagen.tel.billing.billcall.jpa.domain.UserTelephoneEntity;

public interface UserTelephoneDaoService {
	public List<UserTelephoneEntity> findTelephonesByUserIdAndMonth(
			String userId, int year, int month);
	
	public List<UserTelephoneEntity> findTelephonesByUserIdAndTelephone(
			String userId, String telephoneNumber);
	
	public List<UserTelephoneEntity> findTelephonesByUserId(String userId);
	
	public UserTelephoneEntity saveUserTelephone(UserTelephoneEntity entity);
	
	public List<UserTelephoneEntity> findAllEntities(int page, int size, Direction direction,
			String properties);
	
	public long countAllEntities();
	
	public List<UserTelephoneEntity> findByUserId(String userId, int page, int size, Direction direction,
			String properties);
	
	public long countByUserId(String userId);	
	
	public UserTelephoneEntity findByTelephoneNumber(String telephoneNumber);

	
}
