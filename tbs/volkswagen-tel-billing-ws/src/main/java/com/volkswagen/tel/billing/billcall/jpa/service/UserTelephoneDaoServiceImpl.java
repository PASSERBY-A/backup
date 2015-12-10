package com.volkswagen.tel.billing.billcall.jpa.service;

import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.volkswagen.tel.billing.billcall.jpa.domain.UserTelephoneEntity;
import com.volkswagen.tel.billing.billcall.jpa.repository.UserTelephoneRepository;
import com.volkswagen.tel.billing.common.util.CommonUtil;

@Service("UserTelephoneDaoService")
public class UserTelephoneDaoServiceImpl implements UserTelephoneDaoService {
	@Autowired
	private UserTelephoneRepository repository;
	
	@Override
	public List<UserTelephoneEntity> findTelephonesByUserIdAndMonth(
			String userId, int year, int month) {
		List<UserTelephoneEntity> resultList = null;
		
		Calendar firstDayOfCurrentMonth = CommonUtil.getCalendar(year, month, 1, 0, 0, 0);
		Calendar firstDayOfNextMonth = CommonUtil.getCalendar(year, month, 1, 0, 0, 0);
		firstDayOfNextMonth.add(Calendar.HOUR, 24);
		resultList = repository.findTelephonesByUserIdAndDatePeriod(userId,
				firstDayOfCurrentMonth.getTime(),
				firstDayOfNextMonth.getTime());

		return resultList;
	}

	@Override
	public List<UserTelephoneEntity> findTelephonesByUserIdAndTelephone(
			String userId, String telephoneNumber) {
		List<UserTelephoneEntity> resultList = repository
				.findTelephonesByUserIdTelephone(userId, telephoneNumber);
		return resultList;
	}

	@Override
	public List<UserTelephoneEntity> findTelephonesByUserId(String userId) {
		return repository.findTelephonesByUserId(userId);
	}

	@Override
	public UserTelephoneEntity saveUserTelephone(UserTelephoneEntity entity) {
		return repository.save(entity);
	}

	@Override
	public List<UserTelephoneEntity> findAllEntities(int page, int size,
			Direction direction, String properties) {
		PageRequest pageReq = new PageRequest(page - 1, size, direction,
				properties);

		return repository.findAllEntities(pageReq);
	}

	@Override
	public long countAllEntities() {
		return repository.countAllEntities();
	}

	@Override
	public List<UserTelephoneEntity> findByUserId(String userId, int page, int size,
			Direction direction, String properties) {
		PageRequest pageReq = new PageRequest(page - 1, size, direction,
				properties);
		
		return repository.findByUserId(userId, pageReq);
	}

	@Override
	public long countByUserId(String userId) {
		return repository.countByUserId(userId);
	}

	@Override
	public UserTelephoneEntity findByTelephoneNumber(String telephoneNumber) {
		
		return repository.findByTelephoneNumber(telephoneNumber);
	}

//	@Override
//	public List<String> findAllUserIds(int page, int size, Direction direction,
//			String properties) {
//		PageRequest pageReq = new PageRequest(page - 1, size, direction,
//				properties);
//		
//		return repository.findAllUserIds(pageReq);
//	}
//	
//	@Override
//	public long countAllUserIds() {
//		return repository.countAllUserIds();
//	}
//
//	@Override
//	public List<String> findUserByUserId(String userId,int page, int size, Direction direction,
//			String properties) {
//		PageRequest pageReq = new PageRequest(page - 1, size, direction,
//				properties);
//		
//		return repository.findUserByUserId(userId, pageReq);
//	}
//	
//	@Override
//	public long countUserByUserId(String userId) {
//		return repository.countUserByUserId(userId);
//	}
	
	
	public static void main(String[] args) throws Exception {
		
		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
		
		UserTelephoneDaoServiceImpl n= context.getBean(UserTelephoneDaoServiceImpl.class);
		
		System.out.println(n.findByTelephoneNumber("65318888"));
		
	}
	
}
