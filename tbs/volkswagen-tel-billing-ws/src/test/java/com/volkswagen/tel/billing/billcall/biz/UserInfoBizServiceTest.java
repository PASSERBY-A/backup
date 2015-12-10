package com.volkswagen.tel.billing.billcall.biz;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
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
public class UserInfoBizServiceTest {
	public static String userId = "ELIYUA0";

	@Autowired
	UserInfoBizService service;
	
	@Test
	@Transactional
	@Rollback(false)
	public void getUserInfoByUserId() throws Exception {
		JSONObject jObj = service.getUserInfoByUserId(userId);

		assertEquals(jObj.get("returnCode"), "SUCCESS");
		String staffCode = jObj.getString("staffCode");
		assertNotNull(staffCode);
	}
	
}