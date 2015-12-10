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

import com.volkswagen.tel.billing.billcall.biz.BillCallRecordBizService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class BillCallRecordBizServiceTest {

	public static String userId = "ELIYUA0";

	public static String telephoneNumber = "13911523445";
	public static int selectedYear = 2014;
	public static int selectedMonth = 03;
	public static int recordId = 231191;

	@Autowired
	BillCallRecordBizService billCallRecordBizService;

	@Test
	@Transactional
	@Rollback(false)
	public void testGetBillCallRecordsByTelAndMonth() throws Exception {

		JSONObject jObj = billCallRecordBizService
				.getBillCallRecordsByTelAndMonth(userId, telephoneNumber,
						selectedYear, selectedMonth, 1, 50);

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
	public void testUpdatePrivatePurposeByRecordId() throws Exception {

		JSONObject jObj = billCallRecordBizService
				.updatePrivatePurposeByRecordId(recordId, 1);

		assertEquals(jObj.get("returnCode"), "SUCCESS");

		assertEquals(jObj.get("updateCount"), 1);
	}
}