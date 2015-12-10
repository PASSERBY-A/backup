package com.volkswagen.tel.billing.excel.parser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.volkswagen.tel.billing.billcall.jpa.domain.BillCallRecordEntity;
import com.volkswagen.tel.billing.billcall.jpa.domain.TelephoneBillEntity;
import com.volkswagen.tel.billing.billcall.jpa.service.BillCallRecordDaoService;
import com.volkswagen.tel.billing.billcall.jpa.service.TelephoneBillDaoService;
import com.volkswagen.tel.billing.common.util.CommonUtil;
import com.volkswagen.tel.billing.common.util.CountryCodeUtil;
import com.volkswagen.tel.billing.excel.ExcelFileLoader;

@Component
public class ChinaUnicomParser {
	private static final Logger log = LoggerFactory
	.getLogger(ChinaUnicomParser.class);
	
	@Autowired
	BillCallRecordDaoService billCallRecordDaoService;
	
	@Autowired
	TelephoneBillDaoService telephoneBillDaoService;
	
	private List<List<String>> dataList = null;
	private static final String dateFormatString = "yyyy-MM-dd HH:mm:ss";
	private static final int batchUpdateThreshold = 400;
	private static final String VENDOR_NAME = "China Unicom"; 

	public ChinaUnicomParser() {
	}

	public void parseExcel(String excelFilePath) throws Exception {
		ExcelFileLoader loader = new ExcelFileLoader(new SimpleDateFormat(dateFormatString), null);
		loader.load(excelFilePath);
		dataList = loader.getAllDataByRow(0, null);
	}

	public void persistDataToDatabase() throws Exception {
		if (dataList == null) {
			throw new Exception("Excel file can not be parsed successfully.");
		}

		List<BillCallRecordEntity> recList = new ArrayList<BillCallRecordEntity>();
		int i = 0;
		int year = 0;
		int month = 0;
		for (List<String> columns : dataList) {
			if (i > Long.MAX_VALUE) { // - this config is for testing.
				break;
			} else {
				log.info(i + ">>> ");
			}
			
			if (i == 1) {
				String telephone = columns.get(0);
				Date yyyyMM = CommonUtil.toDate(columns.get(1), "yyyyMM");
				log.info("*** yyyyMM=" + yyyyMM);
				Calendar cal = Calendar.getInstance();
				cal.setTime(yyyyMM);
				year = cal.get(Calendar.YEAR);
				month = cal.get(Calendar.MONTH) + 1;
				log.info("*** telephone=" + telephone + ", year="
						+ year + ", month=" + month);

				this.prepareTelBill(telephone, year, month);
			}
			
			if (i >= 4 && columns != null && columns.size() == 8) {
				for (String str : columns) {
					log.info(str + ", ");
				}
				BillCallRecordEntity entity = this.composeBillCallRecord(columns);
				log.info(">>> BillCallRecordEntity=" + entity);
				recList.add(entity);
				if (recList.size() == batchUpdateThreshold) {
					// go insert, then clear recList
					this.batchInsertBillCallRecord(recList);

					recList.clear();
					log.info("==== recList is cleaned. size=" + recList.size());
				}
			}
			log.info("\n");
			i++;
		}
		
		if (recList.size()>0) {
			// go insert, then clear recList
			this.batchInsertBillCallRecord(recList);
			
			//TODO
			
		}
	}
	
	private List<BillCallRecordEntity> batchInsertBillCallRecord(
			List<BillCallRecordEntity> callRecordList) throws Exception {
		// - insert bill call records.
		List<BillCallRecordEntity> list = billCallRecordDaoService
				.saveCallRecordList(callRecordList);
		return list;
	}
	
	private void batchInsertTelBill(Map<String, TelephoneBillEntity> tbMap)
			throws Exception {
		if (tbMap == null) {
			return;
		}

		for (Map.Entry<String, TelephoneBillEntity> entry : tbMap.entrySet()) {
			log.info(entry.getKey() + "---" + entry.getValue());

			TelephoneBillEntity ent = entry.getValue();
			TelephoneBillEntity dbEntity = telephoneBillDaoService
					.findTelephoneBillByTelNumberAndMonth(
							ent.getTelephoneNumber(), ent.getYear(),
							ent.getMonth());

			if (dbEntity == null) {
				log.info(">>> saveTelephoneBill: " + ent);
				telephoneBillDaoService.saveTelephoneBill(ent);
			}
		}
	}

	private BillCallRecordEntity composeBillCallRecord(List<String> columns) {
		BillCallRecordEntity entity = new BillCallRecordEntity();
		entity.setCallingNumber(columns.get(0));
		entity.setCalledNumber(columns.get(4));
		
		// - country code
		String countryCode = columns.get(5);
		entity.setCountryCode(countryCode);
		// - communication type
		entity.setCommunicationType(this.getCommunicationType(countryCode));
		// - location
		entity.setLocation(CountryCodeUtil
				.getCountryNameByCountryCode(countryCode));
		
		// - duration
		long tmpSec = Long.parseLong(columns.get(6));
		entity.setDuration(CommonUtil.convertSecondsToHHMMSS(tmpSec));
		
		entity.setCost(Float.parseFloat(columns.get(7)));
		
		Date dateOfCall = CommonUtil.toDate(
				columns.get(1) + " " + columns.get(2), dateFormatString);
		entity.setDateOfCall(dateOfCall);
		entity.setStartingTime(dateOfCall);
		
		Calendar dateOfCallCal = Calendar.getInstance();
		dateOfCallCal.setTime(dateOfCall);
		entity.setYear(dateOfCallCal.get(Calendar.YEAR));
		entity.setMonth(dateOfCallCal.get(Calendar.MONTH) + 1);
		
		
		return entity;
	}
	
	private String getCommunicationType(String countryCode) {
		String rtn = "International";
		if ("86".equals(countryCode)) {
			rtn = "Domestic";
		}
		return rtn;
	}
	
	private TelephoneBillEntity prepareTelBill(String telNumber, int year,
			int month) throws Exception {
		TelephoneBillEntity tbEntity = telephoneBillDaoService
				.findTelephoneBillByTelNumberAndMonth(telNumber, year, month);

		if (tbEntity == null || tbEntity.getBillId() <= 0) {
			tbEntity = new TelephoneBillEntity();
			tbEntity.setTelephoneNumber(telNumber);
			tbEntity.setYear(year);
			tbEntity.setMonth(month);
			tbEntity.setLastUpdateTime(Calendar.getInstance().getTime());
			tbEntity.setVendorName(VENDOR_NAME);
			tbEntity.setStatus("ACTIVE");
			tbEntity = telephoneBillDaoService.saveTelephoneBill(tbEntity);
		}

		return tbEntity;
	}
}