package com.volkswagen.tel.billing.billcall.biz;

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

import com.volkswagen.tel.billing.billcall.biz.BillCallRecordBizService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class BillCallRecordBizServiceTest {

	public static String userId = "ELIYUA0";

	public static String callingNumber = "13911523445";
	public static String calledNumbers = "18601234567";
	public static int isPrivate = 0;
	public static int year = 2014;
	public static int month = 03;
	public static int recordId = 231191;

	@Autowired
	BillCallRecordBizService billCallRecordBizService;

	@Test
	@Transactional
	@Rollback(false)
	public void getBillCallRecordsByTelAndMonth() throws Exception {
		JSONObject jObj = billCallRecordBizService
				.getBillCallRecordsByTelAndMonth(userId, callingNumber,
						year, month, 1, 50);

		assertEquals(jObj.get("returnCode"), "SUCCESS");
		JSONArray resultList = (JSONArray) jObj.get("resultList");
		for (int i = 0; i < resultList.size(); i++) {
			JSONArray ary = (JSONArray) resultList.get(i);
			assertNotNull(ary);
		}
	}

	@Test
	@Transactional
	@Rollback(false)
	public void updatePrivatePurposeByRecordId() throws Exception {
		JSONObject jObj = billCallRecordBizService
				.updatePrivatePurposeByRecordId(recordId, 1);

		assertEquals(jObj.get("returnCode"), "SUCCESS");
		assertEquals(jObj.get("updateCount"), 1);
	}
	
	@Test
	@Transactional
	@Rollback(false)
	public void getNumbersToExecuteInBatch() throws Exception {
		JSONObject jObj = billCallRecordBizService.getNumbersToExecuteInBatch(userId, callingNumber, year, month);

		assertEquals(jObj.get("returnCode"), "SUCCESS");
		Object list = jObj.get("numbersToExecuteInBatch");
		assertNotNull(list);
	}
	
	@Test
	@Transactional
	@Rollback(false)
	public void updatePrivatePurposeInBatch() throws Exception {
		JSONObject jObj = billCallRecordBizService.updatePrivatePurposeInBatch(callingNumber, year, month, calledNumbers, isPrivate);

		assertEquals(jObj.get("returnCode"), "SUCCESS");
		Object list = jObj.get("numbersToExecuteInBatch");
		assertNotNull(list);
	}
}