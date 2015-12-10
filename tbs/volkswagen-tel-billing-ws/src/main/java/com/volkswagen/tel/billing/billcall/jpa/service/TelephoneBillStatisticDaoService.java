package com.volkswagen.tel.billing.billcall.jpa.service;

import java.util.Date;
import java.util.List;

import com.volkswagen.tel.billing.billcall.jpa.domain.BillCallRecordEntity;

public interface TelephoneBillStatisticDaoService {

	public List<Object> getCommunicationTypeStatisticByCallingNumerYearMonth(
			String callingNumber, int year, int month, Date startingDate,
			Date endDate);
	
	public List<BillCallRecordEntity> getSavedCallRecordList(
			String callingNumber, int year, int month, Date startingDate,
			Date endDate); 

}
