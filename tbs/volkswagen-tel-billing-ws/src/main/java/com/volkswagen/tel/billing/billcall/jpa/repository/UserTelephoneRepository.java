package com.volkswagen.tel.billing.billcall.jpa.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.volkswagen.tel.billing.billcall.jpa.domain.NotifyEntity;
import com.volkswagen.tel.billing.billcall.jpa.domain.UserTelephoneEntity;

public interface UserTelephoneRepository extends
		CrudRepository<UserTelephoneEntity, Long> {

	@Query("from UserTelephoneEntity a where lower(a.userId)=lower(?1) and ( (a.status='ACTIVE' and a.validStartingTime<=?2) or (a.status='INACTIVE' and a.validEndTime>=?2 and a.validEndTime<?3) )")
	public List<UserTelephoneEntity> findTelephonesByUserIdAndDatePeriod(
			String userId, Date firstDayOfCurrentMonth, Date firstDayOfNextMonth);
	
	@Query("from UserTelephoneEntity a where lower(a.userId)=lower(?1) and a.telephoneNumber=?2")
	public List<UserTelephoneEntity> findTelephonesByUserIdTelephone(
			String userId, String telephoneNumber);	
	
	@Query("from UserTelephoneEntity a where lower(a.userId)=lower(?1)")
	public List<UserTelephoneEntity> findTelephonesByUserId(String userId);
	
	@Query("from UserTelephoneEntity a where 1=1")
	public List<UserTelephoneEntity> findAllEntities(Pageable pageable);
	
	@Query("select count(*) from UserTelephoneEntity a where 1=1")
	public long countAllEntities();

	@Query("from UserTelephoneEntity a where lower(a.userId)=lower(?1)")
	public List<UserTelephoneEntity> findByUserId(String userId, Pageable pageable);
	
	@Query("select count(*) from UserTelephoneEntity a where lower(a.userId)=lower(?1)")
	public long countByUserId(String userId);
	
	public UserTelephoneEntity findByTelephoneNumber(String telephoneNumber);
	
	public UserTelephoneEntity findByUserIdAndTelephoneNumberAndStatus(String userId,String telephoneNumber,String status); 
	
	public List<UserTelephoneEntity> findByUserIdAndStatus(String userId,String status); 
	
}
