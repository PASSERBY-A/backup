package com.volkswagen.tel.billing.billcall.jpa.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.volkswagen.tel.billing.billcall.jpa.domain.NotifyEntity;
import com.volkswagen.tel.billing.billcall.jpa.domain.UserRoleEntity;

public interface NotifyRepository extends CrudRepository<NotifyEntity, Long> {

	@Query("from NotifyEntity a where a.sendFlag=false ")
	public List<NotifyEntity> findAll();
	
	public List<NotifyEntity> findBySendFlagFalse();
	
	@Modifying
	@Query("update NotifyEntity u set u.sendFlag = true,u.sendTime = ?2 where u.id=?1")
	public void setSendFlagAndSendsendTime(Long id,Date date);
	
	
}
