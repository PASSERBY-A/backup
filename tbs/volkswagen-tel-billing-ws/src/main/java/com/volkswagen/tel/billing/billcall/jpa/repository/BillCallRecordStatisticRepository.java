package com.volkswagen.tel.billing.billcall.jpa.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.volkswagen.tel.billing.billcall.jpa.domain.TelephoneBillEntity;

public interface BillCallRecordStatisticRepository extends CrudRepository<TelephoneBillEntity, Long>{
	
	@Query("SELECT  a.calledNumber, TO_CHAR(TRUNC(SUM(SUBSTR(a.duration,1,2) * 60 * 60 + SUBSTR(a.duration, 4, 2) * 60 + SUBSTR(a.duration, 7,2))/3600),'FM9900') || ':' ||TO_CHAR(TRUNC(MOD(SUM(SUBSTR(a.duration,1,2) * 60 * 60 + SUBSTR(a.duration, 4, 2) * 60 + SUBSTR(a.duration, 7,2)),3600)/60),'FM00') || ':' ||TO_CHAR(MOD(SUM(SUBSTR(a.duration,1,2) * 60 * 60 + SUBSTR(a.duration, 4, 2) * 60 + SUBSTR(a.duration, 7,2)),60),'FM00') as duration, sum(a.cost) as cost from  BillCallRecordEntity a WHERE a.callingNumber =?1 and a.year = ?2 and a.month=?3 and a.dateOfCall>=?4 and a.dateOfCall<?5 and a.privatePurpose = '1' GROUP BY a.calledNumber")
	public List<Object> getCommunicationTypeStatisticByCallingNumerYearMonth(String callingNumber, int year, int month, Date startingDate, Date endDate);
}


