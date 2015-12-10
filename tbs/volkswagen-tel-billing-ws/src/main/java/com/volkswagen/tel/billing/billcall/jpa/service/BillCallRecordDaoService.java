package com.volkswagen.tel.billing.billcall.jpa.service;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Sort.Direction;

import com.volkswagen.tel.billing.billcall.jpa.domain.BillCallRecordEntity;

public interface BillCallRecordDaoService {
	public BillCallRecordEntity saveCallRecord(BillCallRecordEntity entity);
	
	public List<BillCallRecordEntity> saveCallRecordList(List<BillCallRecordEntity> callRecordList);

	public void deleteCallRecordById(Long id);
	
	public BillCallRecordEntity getBillCallRecordById(Long id);

	public int updatePrivatePurposeByTelYearMonth(int isPrivate, String callingNumber, String calledNumber, int year, int month);
	
	public int updatePrivatePurposeByPrivateNumberStringList(int isPrivate, String callingNumber, List<String> calledNumberList, int year, int month);
	
	public int updatePrivatePurposeByRecordId(long recordId, int isPrivate);

	public long count();

	public List<BillCallRecordEntity> findBillCallRecordsByTelNumberAndMonthAndDatePeriod(
			String callingNumber, int year, int month, Date startingDate,
			Date endDate, int page, int size, Direction direction,
			String properties);

	public long countBillCallRecordsByTelNumberAndMonthAndDatePeriod(
			String callingNumber, int year, int month, Date startingDate,
			Date endDate);
	
	public List<String> findCalledNumbersByTelNumberAndMonth(
			String callingNumber, int year, int month);

	List<Float> findCostByCallingNumberAndYearAndMonth(String callingNumber,int year, int month);
}
