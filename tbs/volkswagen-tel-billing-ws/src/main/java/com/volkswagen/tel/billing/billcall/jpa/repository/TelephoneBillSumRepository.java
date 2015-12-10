package com.volkswagen.tel.billing.billcall.jpa.repository;

import java.util.List;

import com.volkswagen.tel.billing.billcall.jpa.domain.TelephoneBillSumEntity;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface TelephoneBillSumRepository extends
		CrudRepository<TelephoneBillSumEntity, Long> {
	
	@Query("from TelephoneBillSumEntity a where a.telephoneNumber=?1 and a.year=?2 and a.month=?3")
	public TelephoneBillSumEntity findTelephoneBillByTelNumberAndMonth(String telephoneNumber, int year, int month);
	
	@Query("select a.monthPkg from TelephoneBillSumEntity a where a.telephoneNumber=?1 and a.year=?2 and a.month=?3")
	public  Float findMonthPKGByTelNumberAndMonth(String telephoneNumber, int year, int month);
	
	@Query("select a.dataBoPkg from TelephoneBillSumEntity a where a.telephoneNumber=?1 and a.year=?2 and a.month=?3")
	public  Float findDataBoPkgPKGByTelNumberAndMonth(String telephoneNumber, int year, int month);
	
	@Query("select a.smsBoPkg from TelephoneBillSumEntity a where a.telephoneNumber=?1 and a.year=?2 and a.month=?3")
	public  Float findsmsBoPKGByTelNumberAndMonth(String telephoneNumber, int year, int month);
	
}
