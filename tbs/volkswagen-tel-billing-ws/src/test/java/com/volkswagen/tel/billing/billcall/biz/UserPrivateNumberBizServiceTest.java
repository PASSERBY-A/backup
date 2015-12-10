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
public class UserPrivateNumberBizServiceTest {
	public static String userId = "ELIYUA0";
	public static String roleName = "EMERGENCY-USER";
	public static String privateNumber = "1234567";
	

	public static String telephoneNumber = "13911523445";
	public static int selectedYear = 2014;
	public static int selectedMonth = 03;
	public static int recordId = 231191;

	@Autowired
	UserPrivateNumberBizService userPrivateNumberBizService;

	@Test
	@Transactional
	@Rollback(false)
	public void savePrivateNumber() throws Exception {
		JSONObject jObj = userPrivateNumberBizService.savePrivateNumber(userId,
				privateNumber);

		assertEquals(jObj.get("returnCode"), "SUCCESS");
		Object entity = jObj.get("userPrivateEntityEntity");
		assertNotNull(entity);
	}
	
	@Test
	@Transactional
	@Rollback(false)
	public void deletePrivateNumberById() throws Exception {
		JSONObject jObj = userPrivateNumberBizService.deletePrivateNumberById(1);

		assertEquals(jObj.get("returnCode"), "SUCCESS");
	}
	
	@Test
	@Transactional
	@Rollback(false)
	public void getPrivateNumbers() throws Exception {
		JSONObject jObj = userPrivateNumberBizService.getPrivateNumbers(userId);

		assertEquals(jObj.get("returnCode"), "SUCCESS");
		JSONArray list = (JSONArray) jObj.get("userPrivateNumberList");
		assertNotNull(list);
	}
}
