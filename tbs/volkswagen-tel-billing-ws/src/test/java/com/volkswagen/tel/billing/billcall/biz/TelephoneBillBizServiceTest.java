package com.volkswagen.tel.billing.billcall.biz;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class TelephoneBillBizServiceTest {
	public static String userId = "ELIYUA0";

	public static String telephoneNumber = "13911523445";
	public static int selectedYear = 2014;
	public static int selectedMonth = 03;
	public static int recordId = 231191;
	public static int billId = 3279;

	@Autowired
	TelephoneBillBizService telephoneBillBizService;

	@Test
	@Transactional
	@Rollback(false)
	public void saveTelephoneBill() throws Exception {
		JSONObject jObj = telephoneBillBizService.saveTelephoneBill(
				telephoneNumber, selectedYear, selectedMonth);
		assertEquals(jObj.get("returnCode"), "SUCCESS");

		Object entity = jObj.get("billEntity");
		assertNotNull(entity);
	}

	@Test
	@Transactional
	@Rollback(false)
	public void getAvailableMonthsByTelAndYear() throws Exception {
		JSONObject jObj = telephoneBillBizService
				.getAvailableMonthsByTelAndYear(userId, telephoneNumber,
						selectedYear);

		assertEquals(jObj.get("returnCode"), "SUCCESS");
		JSONArray array = JSONArray
				.fromObject(jObj.get("monthList").toString());

		for (int i = 0; i < array.size(); i++) {
			JSONObject objcet = (JSONObject) array.get(i);
			assertNotNull(objcet.get("month"));
			assertNotNull(objcet.get("status"));
		}
	}

	@Test
	@Transactional
	@Rollback(false)
	public void inactivateTelephoneBillStatus() throws Exception {
		JSONObject jObj = telephoneBillBizService
				.inactivateTelephoneBillStatus(telephoneNumber, selectedYear,
						selectedMonth);

		assertEquals(jObj.get("returnCode"), "SUCCESS");
		assertEquals(jObj.get("updateCount"), 1);
	}

	@Test
	@Transactional
	@Rollback(false)
	public void activateTelephoneBillStatus() throws Exception {
		JSONObject jObj = telephoneBillBizService
				.activateTelephoneBillStatus(billId);

		assertEquals(jObj.get("returnCode"), "SUCCESS");
		assertEquals(jObj.get("updateCount"), 1);
	}

	@Test
	@Transactional
	@Rollback(false)
	public void getTelephoneBillInfo() throws Exception {
		JSONObject jObj = telephoneBillBizService.getTelephoneBillInfo(
				telephoneNumber, selectedYear, selectedMonth);

		assertEquals(jObj.get("returnCode"), "SUCCESS");
		JSONObject entity = (JSONObject) jObj.get("telephoneBillEntity");
		assertNotNull(entity.get("billId"));
	}

	@Test
	@Transactional
	@Rollback(false)
	public void getAvailableYearsByTel() throws Exception {
		JSONObject jObj = telephoneBillBizService
				.getAvailableYearsByTel(telephoneNumber);

		assertEquals(jObj.get("returnCode"), "SUCCESS");
		JSONArray array = JSONArray.fromObject(jObj.get("yearList").toString());

		for (int i = 0; i < array.size(); i++) {
			JSONObject objcet = (JSONObject) array.get(i);
			assertNotNull(objcet.get("year"));
			assertNotNull(objcet.get("status"));
		}
	}
}
