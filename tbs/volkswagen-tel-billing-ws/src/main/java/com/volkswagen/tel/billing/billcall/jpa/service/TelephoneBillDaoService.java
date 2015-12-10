package com.volkswagen.tel.billing.billcall.jpa.service;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Sort.Direction;

import com.volkswagen.tel.billing.billcall.jpa.domain.TelephoneBillEntity;

public interface TelephoneBillDaoService 
{
	public TelephoneBillEntity saveTelephoneBill(TelephoneBillEntity entity);

	public void deleteTelephoneBillById(Long id);

	public long count();

	public TelephoneBillEntity findTelephoneBillByTelNumberAndMonth(
			String telephoneNumber, int year, int month) throws Exception;

	public List<TelephoneBillEntity> findAvailableMonthsByTelAndYear(
			String telNumber, int year, int monthLimitStart, int monthLimitEnd);

	public List<TelephoneBillEntity> findAvailableYearsByTel(String telephoneNumber);

	public int updateTelephoneBillStatus(String telephoneNumber, int year,int month, String status);

	public int updateTelephoneBillStatusByBillId(long billId, String status);

	public List<TelephoneBillEntity> findBillsByTelTimePeriod(String telephoneNumber, Calendar startCalendar, Calendar endCalendar);
	
	public Map<String,List<TelephoneBillEntity>> findNoSendBillByUser(String userId);
	
	public List<TelephoneBillEntity> findAllOpenBills(String type,int year,int month,int page, int size, Direction direction,String properties);
	
	public Long countOpenBillsByType(String vendorName,int year,int month);
	
}
