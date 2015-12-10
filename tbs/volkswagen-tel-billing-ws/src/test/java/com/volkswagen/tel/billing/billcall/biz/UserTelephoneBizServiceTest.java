package com.volkswagen.tel.billing.billcall.biz;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class UserTelephoneBizServiceTest {

	@Resource
	private UserTelephoneBizService userTelephoneBizService;

	public static String userId = "ELIYUA0";

	public static String telephoneNumber = "13911523445";
	public static int selectedYear = 2014;
	public static int selectedMonth = 03;
	public static int recordId = 231191;

	@Test
	@Transactional
	@Rollback(false)
	public void getTelephonesByUserId() throws Exception {
		JSONObject jObj = userTelephoneBizService.getTelephonesByUserId(userId);

		assertEquals(jObj.get("returnCode"), "SUCCESS");
		JSONArray list = (JSONArray) jObj.get("userTelList");
		assertNotNull(list);
	}
}