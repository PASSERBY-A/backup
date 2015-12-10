package com.volkswagen.tel.billing.billcall.jpa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.volkswagen.tel.billing.billcall.jpa.domain.BillSubmitHistoryEntity;
import com.volkswagen.tel.billing.billcall.jpa.repository.BillSubmitHistoryRepository;

@Service("billSubmitHistoryDaoServiceImpl")
public class BillSubmitHistoryDaoServiceImpl implements
		BillSubmitHistoryDaoService {
	@Autowired
	private BillSubmitHistoryRepository repository;

	@Override
	public BillSubmitHistoryEntity saveBillSubmitHistory(
			BillSubmitHistoryEntity entity) {
		return repository.save(entity);
	}

	@Override
	public long countBillSubmitTimes(String staffCode, String telephoneNumber,
			int year, int month) {
		return repository.countBillSubmitTimes(staffCode, telephoneNumber,
				year, month);
	}

}
