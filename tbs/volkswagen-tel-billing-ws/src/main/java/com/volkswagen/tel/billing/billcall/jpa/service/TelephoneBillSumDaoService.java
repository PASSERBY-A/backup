package com.volkswagen.tel.billing.billcall.jpa.service;

import com.volkswagen.tel.billing.billcall.jpa.domain.TelephoneBillSumEntity;

import java.util.List;

public interface TelephoneBillSumDaoService {

	public TelephoneBillSumEntity findBillSumByUserIdAndTelephone(String telephone, int year, int month);

    public TelephoneBillSumEntity saveTelephoneBillSum(TelephoneBillSumEntity tbEntity);

    public void saveTelephoneBillSumList(List<TelephoneBillSumEntity> billSumEntities);

	public  Double  computePKGTotalByTelNumberAndMonth(String telephone, int year,int month);
	
	
	
	
}
