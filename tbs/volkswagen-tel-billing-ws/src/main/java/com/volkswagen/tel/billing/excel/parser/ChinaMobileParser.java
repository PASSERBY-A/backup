package com.volkswagen.tel.billing.excel.parser;

import com.volkswagen.tel.billing.billcall.jpa.domain.BillCallRecordEntity;
import com.volkswagen.tel.billing.billcall.jpa.domain.TelephoneBillEntity;
import com.volkswagen.tel.billing.billcall.jpa.domain.TelephoneBillSumEntity;
import com.volkswagen.tel.billing.billcall.jpa.service.BillCallRecordDaoService;
import com.volkswagen.tel.billing.billcall.jpa.service.TelephoneBillDaoService;
import com.volkswagen.tel.billing.billcall.jpa.service.TelephoneBillSumDaoService;
import com.volkswagen.tel.billing.common.util.CommonUtil;
import com.volkswagen.tel.billing.excel.ExcelFileLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Component
public class ChinaMobileParser {
	private static final Logger log = LoggerFactory
	.getLogger(ChinaMobileParser.class);
	
	@Autowired
	BillCallRecordDaoService billCallRecordDaoService;
	
	@Autowired
	TelephoneBillDaoService telephoneBillDaoService;

    @Autowired
    TelephoneBillSumDaoService telephoneBillSumDaoService;

	private List<List<String>> dataList = null;
	private static final String dateFormatString = "yyyy-MM-dd HH:mm";
	private static final int batchUpdateThreshold = 400;
	private static final String VENDOR_NAME = "China Mobile";
	
	private boolean CALLRECORD_PROCESSINGSTART_FLAG = false;
    private boolean BILL_SUM_PROCESSINGSTART_FLAG = false;

	public ChinaMobileParser() {
	}

	public void parseExcel(String excelFilePath) throws Exception {
		ExcelFileLoader loader = new ExcelFileLoader(new SimpleDateFormat(dateFormatString), null);
		loader.load(excelFilePath);
        if (dataList!=null)
            dataList.clear();
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
			// - parse year and month
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
			
			if (columns != null && columns.size() == 10) {
				for (String str : columns) {
					log.info(str + ", ");
				}
				if (CALLRECORD_PROCESSINGSTART_FLAG == false) {
					if (isCallRecordTitleLine(columns) == true) {
						CALLRECORD_PROCESSINGSTART_FLAG = true;
					}
				} else {
					BillCallRecordEntity entity = this.composeBillCallRecord(columns, year, month);
					log.info(">>> BillCallRecordEntity=" + entity);
					recList.add(entity);
					if (recList.size() == batchUpdateThreshold) {
						// go insert, then clear recList
						this.batchInsertBillCallRecord(recList);
						recList.clear();
						log.info(">>> recList is cleaned. size=" + recList.size());
					}
				}
			}
			
			log.info("\n");
			i++;
		}
		
		if (recList.size()>0) {
			// go insert, then clear recList
			this.batchInsertBillCallRecord(recList);
		}
		
		// - reset the flag
		if (CALLRECORD_PROCESSINGSTART_FLAG == true) {
			CALLRECORD_PROCESSINGSTART_FLAG = false;
		}
	}

    public void persistDataToDatabaseSum() throws Exception {

        if (dataList == null) {
            throw new Exception("Excel file can not be parsed successfully.");
        }

        List<TelephoneBillSumEntity> sumList = new ArrayList<TelephoneBillSumEntity>();
        int i = 0;
        int year = 0;
        int month = 0;
        for (List<String> columns : dataList) {
			if (BILL_SUM_PROCESSINGSTART_FLAG == false) {
				if (columns.get(0).startsWith("1")) {
					BILL_SUM_PROCESSINGSTART_FLAG = true;
					Date yyyyMM = CommonUtil.toDate(columns.get(1), "yyyyMM");
					Calendar cal = Calendar.getInstance();
					cal.setTime(yyyyMM);
					year = cal.get(Calendar.YEAR);
					month = cal.get(Calendar.MONTH) + 1;
				}
            }
			if (columns.get(0).startsWith("1")) {
                TelephoneBillSumEntity entity = TelephoneBillSumEntity.composeBillSumEntity(columns, year, month, VENDOR_NAME);
                log.info(">>> TelephoneBillSumEntity=" + entity);
                sumList.add(entity);
                if (sumList.size() == batchUpdateThreshold) {
                    this.batchInsertBillSum(sumList);
                    sumList.clear();
                    log.info(">>> phone bill sum imported. size=" + sumList.size());
                }
            }
        }
        if (sumList.size()>0) {
            // go insert, then clear recList
            this.batchInsertBillSum(sumList);
        }
        BILL_SUM_PROCESSINGSTART_FLAG = false;
    }
	
	private boolean isCallRecordTitleLine(List<String> columns) {
		boolean rtn = false;

		if (columns != null
				&& "a_isdn_no".equalsIgnoreCase(columns.get(0))
				&& "Starting Time".equalsIgnoreCase(columns.get(1))
				&& "Add.".equalsIgnoreCase(columns.get(2))
				&& "Call mode".equalsIgnoreCase(columns.get(3))
				&& "Call No.".equalsIgnoreCase(columns.get(4))
				&& "Communication time".equalsIgnoreCase(columns.get(5))
				) {
			log.info(">>> Call record title line is met.");
			rtn = true;
		}
		
		return rtn;
	}
	
	private List<BillCallRecordEntity> batchInsertBillCallRecord(
			List<BillCallRecordEntity> callRecordList) throws Exception {

		// - insert bill call records.
		List<BillCallRecordEntity> list = billCallRecordDaoService
				.saveCallRecordList(callRecordList);
		return list;
	}

    private void batchInsertBillSum(List<TelephoneBillSumEntity> billSumEntities) throws Exception {

        telephoneBillSumDaoService.saveTelephoneBillSumList(billSumEntities);
    }


	private TelephoneBillEntity prepareTelBill(String telNumber, int year, int month)
			throws Exception {
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

	private BillCallRecordEntity composeBillCallRecord(List<String> columns, int year, int month) {
		BillCallRecordEntity entity = new BillCallRecordEntity();
		entity.setCallingNumber(columns.get(0));
		entity.setCalledNumber(columns.get(4));
		entity.setCommunicationType(columns.get(6));
		entity.setCost(Float.parseFloat(columns.get(9)));
		
		entity.setYear(year);
		entity.setMonth(month);
		
		Date dateOfCall = null;
		String sTimeStr = columns.get(1);
		if (sTimeStr != null) {
			if (sTimeStr.indexOf("-") > 3) {
				dateOfCall = CommonUtil.toDate(columns.get(1),
						"yyyy-MM-dd HH:mm:ss");
			} else {
				dateOfCall = CommonUtil
						.toDate(columns.get(1), "MM-dd HH:mm:ss");
			}
		}
		Calendar dateOfCallCal = Calendar.getInstance();
		dateOfCallCal.setTime(dateOfCall);
		dateOfCallCal.set(Calendar.YEAR, year);
		dateOfCallCal.set(Calendar.MONTH, month - 1);
		
		entity.setDateOfCall(dateOfCallCal.getTime());
		entity.setStartingTime(dateOfCallCal.getTime());
		
		// - duration
		String tmpDuration = columns.get(5);
		long tmpSec = 0;
		String tmpMmFlag = "'";
		String tmpSsFlag = "\"";
		if (tmpDuration.indexOf(tmpMmFlag) >= 0) {
			long min = Long.parseLong(tmpDuration.substring(0,
					tmpDuration.indexOf(tmpMmFlag)));
			
			if(tmpDuration.contains(tmpSsFlag)){
				tmpSec = Long.parseLong(tmpDuration.substring(
						tmpDuration.indexOf(tmpMmFlag) + 1, tmpDuration.indexOf(tmpSsFlag)));
			}
		
			tmpSec = min * 60 + tmpSec;
		} else {
			tmpSec = Long.parseLong(tmpDuration.substring(0,
					tmpDuration.indexOf(tmpSsFlag)));
		}
		entity.setDuration(CommonUtil.convertSecondsToHHMMSS(tmpSec));
		entity.setLocation(columns.get(2));
		
		return entity;
	}

}