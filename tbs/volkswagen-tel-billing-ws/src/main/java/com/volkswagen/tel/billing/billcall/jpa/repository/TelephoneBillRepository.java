package com.volkswagen.tel.billing.billcall.jpa.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.volkswagen.tel.billing.billcall.jpa.domain.TelephoneBillEntity;

public interface TelephoneBillRepository extends
		CrudRepository<TelephoneBillEntity, Long> {
	
	@Query("from TelephoneBillEntity a where a.telephoneNumber=?1 and a.year=?2 and a.month=?3")
	public List<TelephoneBillEntity> findTelephoneBillByTelNumberAndMonth(String telephoneNumber, int year, int month);
	
	@Query("from TelephoneBillEntity a where a.telephoneNumber=?1 and a.year=?2 and a.month>=?3 and a.month<=?4")
	public List<TelephoneBillEntity> findAvailableMonthsByTelAndYear(
			String telNumber, int year, int monthLimitStart, int monthLimitEnd);

	@Query("from TelephoneBillEntity a where a.telephoneNumber=?1")
	public List<TelephoneBillEntity> findAvailableYearsByTel(String telephoneNumber);
	
	@Modifying
	@Query("update TelephoneBillEntity a set a.status=?4 where a.telephoneNumber=?1 and a.year=?2 and a.month=?3")
	public int updateTelephoneBillStatus(String telephoneNumber, int year,
			int month, String status);
	
	@Modifying
	@Query("update TelephoneBillEntity a set a.status=?2 where a.billId=?1")
	public int updateTelephoneBillStatusByBillId(long billId, String status);
	
	@Query("from TelephoneBillEntity a where a.telephoneNumber=?1 and (to_date(to_char(a.year,'0000')||to_char(a.month,'00')||'01', 'yyyymmdd') between to_date(?2, 'yyyymmdd') and to_date(?3, 'yyyymmdd')) order by to_date(to_char(a.year,'0000')||to_char(a.month,'00')||'01', 'yyyymmdd') desc")
	public List<TelephoneBillEntity> findBillsByTelTimePeriod(String telephoneNumber, String startDate, String endDate);
	
	public List<TelephoneBillEntity> findByTelephoneNumber(String telephoneNumber);
	
	public List<TelephoneBillEntity> findByTelephoneNumberAndStatusNot(String telephoneNumber, String status);
	
	public List<TelephoneBillEntity> findByTelephoneNumberAndStatus(String telephoneNumber, String status);
	
	@Query("from TelephoneBillEntity a where a.month=?2 and a.year=?1 and a.status!='SENT' and a.vendorName= ?3")
	public List<TelephoneBillEntity> findAllOpenBillsByType(int year, int month,String vendorName,Pageable pageable);
	
	@Query("select count(*) from TelephoneBillEntity a where a.month=?2 and a.year=?1 and a.status!='SENT' and a.vendorName= ?3")
	public  Long countOpenBillsByType(int year, int month,String vendorName);
	
	public TelephoneBillEntity findByTelephoneNumberAndYearAndMonth(String telephoneNumber, int year, int month);
	
	
}
