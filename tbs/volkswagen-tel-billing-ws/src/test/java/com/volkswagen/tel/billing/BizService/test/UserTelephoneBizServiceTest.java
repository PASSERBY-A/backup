package com.volkswagen.tel.billing.BizService.test;

import static org.junit.Assert.*;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.volkswagen.tel.billing.billcall.biz.UserTelephoneBizService;

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
	public void testGetAvailableYearsByTel() throws Exception {

		JSONObject jObj = userTelephoneBizService.getTelephonesByUserId(userId);

		assertEquals(jObj.get("returnCode"), "SUCCESS");

		JSONArray array = JSONArray.fromObject(jObj.get("userTelList")
				.toString());

		for (int i = 0; i < array.size(); i++) {

			if (array.get(i).equals(telephoneNumber)) {
				assertTrue(true);
				break;
			}
		}
	}
}