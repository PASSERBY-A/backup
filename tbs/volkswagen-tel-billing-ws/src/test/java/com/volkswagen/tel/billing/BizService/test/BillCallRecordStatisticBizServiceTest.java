package com.volkswagen.tel.billing.BizService.test;

import static org.junit.Assert.*;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.volkswagen.tel.billing.billcall.biz.BillCallRecordStatisticBizService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class BillCallRecordStatisticBizServiceTest {

	public static String userId = "ELIYUA0";

	public static String telephoneNumber = "13911523445";
	public static int selectedYear = 2014;
	public static int selectedMonth = 03;
	public static int recordId = 231191;

	@Autowired
	BillCallRecordStatisticBizService billCallRecordStatisticBizService;

	@Test
	@Transactional
	@Rollback(false)
	public void testGetCommunicationTypeStatisticByCallingNumerYearMonth()
			throws Exception {

		JSONObject jObj = billCallRecordStatisticBizService
				.getCommunicationTypeStatisticByCallingNumerYearMonth(userId,
						telephoneNumber, selectedYear, selectedMonth);

		assertEquals(jObj.get("returnCode"), "SUCCESS");

		assertNotNull(jObj.get("resultList"));
	}

	@Test
	@Transactional
	@Rollback(false)
	public void testGetSavedCallRecordList() throws Exception {

		JSONObject jObj = billCallRecordStatisticBizService
				.getSavedCallRecordList(userId, telephoneNumber, selectedYear,
						selectedMonth);

		assertEquals(jObj.get("returnCode"), "SUCCESS");

		JSONArray array = (JSONArray) jObj.get("resultList");

		for (int i = 0; i < array.size(); i++) {

			JSONObject record = (JSONObject) array.get(i);

			String calledNumber = record.get("calledNumber").toString();

			assertNotNull(calledNumber);
		}

	}
}