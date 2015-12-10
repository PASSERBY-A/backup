package com.volkswagen.tel.billing.billcall.jpa.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.volkswagen.tel.billing.billcall.jpa.domain.BillCallRecordEntity;

public interface BillCallRecordRepository extends
		CrudRepository<BillCallRecordEntity, Long> {
	@Query("from BillCallRecordEntity a where a.callRecordId=?1")
	public BillCallRecordEntity getBillCallRecordById(Long id);

	@Query("from BillCallRecordEntity a where a.callingNumber=?1 and a.year=?2 and a.month=?3 and a.dateOfCall>=?4 and a.dateOfCall<?5")
	public List<BillCallRecordEntity> findCallRecordsByCallingNumberAndDatePeriod(
			String callingNumber, int year, int month, Date startingDate,
			Date endDate, Pageable pageable);

	@Query("select count(*) from BillCallRecordEntity a where a.callingNumber=?1 and a.year=?2 and a.month=?3 and a.dateOfCall>=?4 and a.dateOfCall<?5")
	public long countCallRecordsByCallingNumberAndDatePeriod(
			String callingNumber, int year, int month, Date startingDate,
			Date endDate);

	@Modifying
	@Query("update BillCallRecordEntity a set a.privatePurpose=?1 where a.callingNumber=?2 and a.calledNumber=?3 and a.year=?4 and a.month=?5")
	public int updatePrivatePurposeByTelYearMonth(int isPrivate,
			String callingNumber, String calledNumber, int year, int month);

	@Modifying
	@Query("update BillCallRecordEntity a set a.privatePurpose=?1 where a.callingNumber=?2 and a.calledNumber in (?3) and a.year=?4 and a.month=?5")
	public int updatePrivatePurposeByPrivateNumberStringList(int isPrivate,
			String callingNumber, List<String> calledNumberList, int year,
			int month);

	@Modifying
	@Query("update BillCallRecordEntity a set a.privatePurpose=?2 where a.callRecordId=?1")
	public int updatePrivatePurposeByRecordId(long recordId, int isPrivate);
	
	@Query("select distinct(a.calledNumber) from BillCallRecordEntity a where a.callingNumber=?1 and a.year=?2 and a.month=?3")
	public List<String> findCalledNumbersByTelNumberAndMonth(String callingNumber, int year, int month);
	
	@Query("select a.cost from BillCallRecordEntity a where a.callingNumber=?1 and a.year=?2 and a.month=?3")
	public List<Float> findCostByCallingNumberAndYearAndMonth(String callingNumber, int year, int month);
	
	
}
