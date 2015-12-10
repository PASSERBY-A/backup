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

import com.volkswagen.tel.billing.billcall.biz.UserRoleBizService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class UserRoleBizServiceTest {

	public static String userId = "ELIYUA0";

	public static String telephoneNumber = "13911523445";
	public static int selectedYear = 2014;
	public static int selectedMonth = 03;
	public static int recordId = 231191;

	@Autowired
	UserRoleBizService userRoleBizService;

	@Test
	@Transactional
	@Rollback(false)
	public void testGetRoleUsers() throws Exception {

		JSONObject jObj = userRoleBizService.getRoleUsers("ADMINISTRATOR");

		assertEquals(jObj.get("returnCode"), "SUCCESS");

		JSONArray roleList = (JSONArray) jObj.get("userRoleList");

		for (int i = 0; i < roleList.size(); i++) {

			JSONObject object = (JSONObject) roleList.get(i);

			assertNotNull(object.get("userId"));
		}
	}

	@Test
	@Transactional
	@Rollback(false)
	public void testGetUserRolesByUid() throws Exception {

		JSONObject jObj = userRoleBizService.getUserRoles(userId);

		assertEquals(jObj.get("returnCode"), "SUCCESS");

		JSONArray roleList = (JSONArray) jObj.get("userRoleList");

		for (int i = 0; i < roleList.size(); i++) {
			JSONObject object = (JSONObject) roleList.get(i);
			assertNotNull(object.get("id"));
			assertNotNull(object.get("roleName"));
			assertEquals(object.get("userId"), userId);
		}
	}
}
