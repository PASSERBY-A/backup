package com.volkswagen.tel.billing.BizService.test;

import static org.junit.Assert.*;


import net.sf.json.JSONObject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.volkswagen.tel.billing.billcall.biz.UserInfoBizService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class UserInfoBizServiceTest {

	public static String userId = "ELIYUA0";

	public static String telephoneNumber = "13911523445";
	public static int selectedYear = 2014;
	public static int selectedMonth = 03;
	public static int recordId = 231191;

	@Autowired
	UserInfoBizService userInfoBizService;

	@Test
	@Transactional
	@Rollback(false)
	public void testGetUserInfoByUserId() throws Exception {

		JSONObject jObj = userInfoBizService.getUserInfoByUserId(userId);

		assertEquals(jObj.get("returnCode"), "SUCCESS");

		assertNotNull(jObj.get("firstName"));
		assertNotNull(jObj.get("staffCode"));
	}
}