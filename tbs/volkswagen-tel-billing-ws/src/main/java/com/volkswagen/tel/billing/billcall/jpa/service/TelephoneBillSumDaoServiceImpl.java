package com.volkswagen.tel.billing.billcall.jpa.service;

import com.volkswagen.tel.billing.billcall.jpa.domain.TelephoneBillSumEntity;
import com.volkswagen.tel.billing.billcall.jpa.repository.TelephoneBillSumRepository;
import com.volkswagen.tel.billing.common.util.PreciseCompute;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("TelephoneBillSumDaoService")
public class TelephoneBillSumDaoServiceImpl implements TelephoneBillSumDaoService {
	@Autowired
	private TelephoneBillSumRepository repository;

	@Override
    public TelephoneBillSumEntity findBillSumByUserIdAndTelephone(String telephone, int year, int month){

        return repository.findTelephoneBillByTelNumberAndMonth(telephone, year, month);
	}

    @Override
    public TelephoneBillSumEntity saveTelephoneBillSum(TelephoneBillSumEntity tbsEntity) {

        return repository.save(tbsEntity);
    }

    @Override
    public void saveTelephoneBillSumList(List<TelephoneBillSumEntity> billSumEntities) {

        for (TelephoneBillSumEntity billSumEntity : billSumEntities) {
            repository.save(billSumEntities);
        }
    }
    
    @Override
    public  Double  computePKGTotalByTelNumberAndMonth(String telephone, int year, int month){
    	
    	double result = 0;
    	
    	Float monthpkg = repository.findMonthPKGByTelNumberAndMonth(telephone, year, month)==null?0:repository.findMonthPKGByTelNumberAndMonth(telephone, year, month);
    	
    	Float smsbopkg = repository.findsmsBoPKGByTelNumberAndMonth(telephone, year, month)==null?0:repository.findsmsBoPKGByTelNumberAndMonth(telephone, year, month);
    	
    	Float databopkg = repository.findDataBoPkgPKGByTelNumberAndMonth(telephone, year, month)==null?0:repository.findDataBoPkgPKGByTelNumberAndMonth(telephone, year, month);
    	
    	result = PreciseCompute.add(monthpkg, result);
    	
    	result = PreciseCompute.add(smsbopkg, result);
    	
    	result = PreciseCompute.add(databopkg, result);
    	
    	return PreciseCompute.round(result, 2);
    }
    
    

public static void main(String[] args) throws Exception {
		
		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
		
		TelephoneBillSumDaoServiceImpl n= context.getBean(TelephoneBillSumDaoServiceImpl.class);
		
		  Double  d = n.computePKGTotalByTelNumberAndMonth("13501024407", 2015, 8);
		
		System.out.println(d); 
		 
		//System.out.println(n.getAllYearandMonth("65318888"));
		
		//System.out.println(n.getAllYearandMonth(""));
		
		
		
	}
    
}
