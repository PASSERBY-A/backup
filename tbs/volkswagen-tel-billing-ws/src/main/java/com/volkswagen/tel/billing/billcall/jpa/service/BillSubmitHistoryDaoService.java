package com.volkswagen.tel.billing.billcall.jpa.service;

import com.volkswagen.tel.billing.billcall.jpa.domain.BillSubmitHistoryEntity;

public interface BillSubmitHistoryDaoService {
	public BillSubmitHistoryEntity saveBillSubmitHistory(
			BillSubmitHistoryEntity entity);

	public long countBillSubmitTimes(String staffCode, String telephoneNumber,
			int year, int month);
}
