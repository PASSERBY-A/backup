package com.volkswagen.tel.billing.billcall.jpa.service;

import java.util.List;

import com.volkswagen.tel.billing.billcall.jpa.domain.NotifyEntity;

public interface NotifyDaoService {
	
	
	public List<NotifyEntity> findAll();

	public NotifyEntity saveNotify(NotifyEntity entity);
	
	public List<NotifyEntity> findBySendFlagFalse();
	
	public void setSendFlagAndSendsendTime(long id);
	
	public void notification(); 
}
