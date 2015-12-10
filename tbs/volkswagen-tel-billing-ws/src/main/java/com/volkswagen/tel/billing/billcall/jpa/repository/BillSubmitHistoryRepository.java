package com.volkswagen.tel.billing.billcall.jpa.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.volkswagen.tel.billing.billcall.jpa.domain.BillSubmitHistoryEntity;

public interface BillSubmitHistoryRepository extends
		CrudRepository<BillSubmitHistoryEntity, Long> {
	
	@Query("select count(*) from BillSubmitHistoryEntity a where a.staffCode=?1 and a.telephoneNumber=?2 and a.billingYear=?3 and a.billingMonth=?4")
	public long countBillSubmitTimes(String staffCode, String telephoneNumber,
			int year, int month);
	
//	@Query("from TelephoneBillEntity a where a.telephoneNumber=?1")
//	public List<TelephoneBillEntity> findAvailableYearsByTel(String telephoneNumber);
//	
//	@Modifying
//	@Query("update TelephoneBillEntity a set a.status=?4 where a.telephoneNumber=?1 and a.year=?2 and a.month=?3")
//	public int updateTelephoneBillStatus(String telephoneNumber, int year,
//			int month, String status);
//	
//	@Modifying
//	@Query("update TelephoneBillEntity a set a.status=?2 where a.billId=?1")
//	public int updateTelephoneBillStatusByBillId(long billId, String status);
}
