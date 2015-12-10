package com.volkswagen.tel.billing.billcall.jpa.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.volkswagen.tel.billing.billcall.jpa.domain.BillCallRecordEntity;
import com.volkswagen.tel.billing.billcall.jpa.repository.BillCallRecordRepository;

@Service("billCallRecordDaoService")
public class BillCallRecordDaoServiceImpl implements BillCallRecordDaoService {
	@Autowired
	private BillCallRecordRepository repository;

	@Override
	public BillCallRecordEntity saveCallRecord(BillCallRecordEntity entity) {
		return repository.save(entity);
	}

	@Transactional
	@Override
	public List<BillCallRecordEntity> saveCallRecordList(
			List<BillCallRecordEntity> callRecordList) {
		List<BillCallRecordEntity> list = null;
		if (callRecordList != null) {
			list = new ArrayList<BillCallRecordEntity>();
			for (BillCallRecordEntity entity : callRecordList) {
				entity = repository.save(entity);
				list.add(entity);
			}
		}
		return list;
	}

	@Override
	public BillCallRecordEntity getBillCallRecordById(Long id) {
		return repository.getBillCallRecordById(id);
	}

	@Override
	@Transactional
	public int updatePrivatePurposeByTelYearMonth(int isPrivate,
			String callingNumber, String calledNumber, int year, int month) {
		return repository.updatePrivatePurposeByTelYearMonth(isPrivate,
				callingNumber, calledNumber, year, month);
	}

	@Override
	@Transactional
	public int updatePrivatePurposeByPrivateNumberStringList(int isPrivate,
			String callingNumber, List<String> calledNumberList, int year,
			int month) {
		return repository.updatePrivatePurposeByPrivateNumberStringList(
				isPrivate, callingNumber, calledNumberList, year, month);
	}

	@Override
	public void deleteCallRecordById(Long id) {
		repository.delete(id);
	}

	@Override
	public long count() {
		return repository.count();
	}

	@Override
	public List<BillCallRecordEntity> findBillCallRecordsByTelNumberAndMonthAndDatePeriod(
			String callingNumber, int year, int month, Date startingTime,
			Date endTime, int page, int size, Direction direction,
			String properties) {
		PageRequest pageReq = new PageRequest(page - 1, size, direction,
				properties);

		return repository.findCallRecordsByCallingNumberAndDatePeriod(
				callingNumber, year, month, startingTime, endTime, pageReq);
	}

	@Override
	public long countBillCallRecordsByTelNumberAndMonthAndDatePeriod(
			String callingNumber, int year, int month, Date startingDate,
			Date endDate) {
		return repository.countCallRecordsByCallingNumberAndDatePeriod(
				callingNumber, year, month, startingDate, endDate);
	}

	@Override
	@Transactional
	public int updatePrivatePurposeByRecordId(long recordId, int isPrivate) {
		return repository.updatePrivatePurposeByRecordId(recordId, isPrivate);
	}

	@Override
	public List<String> findCalledNumbersByTelNumberAndMonth(
			String callingNumber, int year, int month) {
		return repository.findCalledNumbersByTelNumberAndMonth(callingNumber, year, month);
	}
	
	@Override
	public List<Float> findCostByCallingNumberAndYearAndMonth(String callingNumber,int year,int month)
	{
		return repository.findCostByCallingNumberAndYearAndMonth(callingNumber, year, month);
		
	}
			
			
	public static void main(String[] args) throws Exception {
		
		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
		
		BillCallRecordDaoServiceImpl n= context.getBean(BillCallRecordDaoServiceImpl.class);
		
		List<Float> f = n.findCostByCallingNumberAndYearAndMonth("65313004", 2015, 8);
		
		System.out.println(f); 
		 
		//System.out.println(n.getAllYearandMonth("65318888"));
		
		//System.out.println(n.getAllYearandMonth(""));
		
		
		
	}		
			
			
}
