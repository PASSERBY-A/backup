package com.volkswagen.tel.billing.billcall.jpa.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.volkswagen.tel.billing.billcall.jpa.domain.UserPrivateNumberEntity;
import com.volkswagen.tel.billing.billcall.jpa.repository.UserPrivateNumberRepository;

@Service("UserPrivateNumberDaoService")
public class UserPrivateNumberDaoServiceImpl implements
		UserPrivateNumberDaoService {
	@Autowired
	private UserPrivateNumberRepository repository;

	@Override
	public List<UserPrivateNumberEntity> findPrivateNumbersByUserId(
			String userId) {
		return repository.findPrivateNumbersByUserId(userId);
	}

	@Override
	public List<String> findPrivateNumberStringListByUserId(String userId) {
		return repository.findPrivateNumberStringListByUserId(userId);
	}

	@Override
	public UserPrivateNumberEntity savePrivateNumber(
			UserPrivateNumberEntity entity) {
		return repository.save(entity);
	}

	@Override
	public void deletePrivateNumberById(Long id) {
		repository.delete(id);
	}

}
