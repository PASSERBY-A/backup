package com.volkswagen.tel.billing.billcall.jpa.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.volkswagen.tel.billing.billcall.jpa.domain.BillCallRecordEntity;
import com.volkswagen.tel.billing.billcall.jpa.repository.BillCallRecordRepository;
import com.volkswagen.tel.billing.billcall.jpa.repository.BillCallRecordStatisticRepository;


@Service("telephoneBillStatisticDaoService")
public class TelephoneBillStatisticDaoServiceImpl implements TelephoneBillStatisticDaoService{

	@Autowired
	private BillCallRecordStatisticRepository repository;
	
	@Autowired
	private BillCallRecordRepository recordRepository;

	public List<Object> getCommunicationTypeStatisticByCallingNumerYearMonth(
			String callingNumber, int year, int month, Date startingDate,
			Date endDate) {
		return repository.getCommunicationTypeStatisticByCallingNumerYearMonth(
				callingNumber, year, month, startingDate, endDate);
	}
	
	public List<BillCallRecordEntity> getSavedCallRecordList(
			String callingNumber, int year, int month, Date startingDate,
			Date endDate) {
		return recordRepository.findCallRecordsByCallingNumberAndDatePeriod(callingNumber, year, month, startingDate, endDate, null);
	}
}
