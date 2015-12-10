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
public class UserRoleBizServiceTest {

	public static String userId = "ELIYUA0";
	public static String roleName = "EMERGENCY-USER";

	public static String telephoneNumber = "13911523445";
	public static int selectedYear = 2014;
	public static int selectedMonth = 03;
	public static int recordId = 231191;

	@Autowired
	UserRoleBizService userRoleBizService;
	
	@Test
	@Transactional
	@Rollback(false)
	public void getAllRoleUsers() throws Exception {
		JSONObject jObj = userRoleBizService.getAllRoleUsers();
		
		assertEquals(jObj.get("returnCode"), "SUCCESS");
		JSONArray roleList = (JSONArray) jObj.get("userRoleList");
		assertNotNull(roleList);
	}

	@Test
	@Transactional
	@Rollback(false)
	public void getRoleUsers() throws Exception {
		JSONObject jObj = userRoleBizService.getRoleUsers("ADMINISTRATOR");

		assertEquals(jObj.get("returnCode"), "SUCCESS");
		JSONArray roleList = (JSONArray) jObj.get("userRoleList");
		assertNotNull(roleList);
	}

	@Test
	@Transactional
	@Rollback(false)
	public void getUserRoles() throws Exception {
		JSONObject jObj = userRoleBizService.getUserRoles(userId);
		
		assertEquals(jObj.get("returnCode"), "SUCCESS");
		JSONArray roleList = (JSONArray) jObj.get("userRoleList");
		assertNotNull(roleList);
	}
	
	@Test
	@Transactional
	@Rollback(false)
	public void saveRoleUser() throws Exception {
		JSONObject jObj = userRoleBizService.saveRoleUser(userId, roleName);
		
		assertEquals(jObj.get("returnCode"), "SUCCESS");
		Object entity = jObj.get("userRoleEntity");
		assertNotNull(entity);
	}
	
	@Test
	@Transactional
	@Rollback(false)
	public void deleteRoleUser() throws Exception {
		JSONObject jObj = userRoleBizService.deleteRoleUser(1);
		assertEquals(jObj.get("returnCode"), "SUCCESS");
	}
}
