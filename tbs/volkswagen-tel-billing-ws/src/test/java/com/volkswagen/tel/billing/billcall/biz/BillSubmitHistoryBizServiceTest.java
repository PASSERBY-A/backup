package com.volkswagen.tel.billing.billcall.biz;

import static org.junit.Assert.assertEquals;
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
public class BillSubmitHistoryBizServiceTest {
	public static String userId = "ELIYUA0";

	public static String telephoneNumber = "13911523445";
	public static int billingYear = 2014;
	public static int billingMonth = 03;

	@Autowired
	BillSubmitHistoryBizService service;

	@Test
	@Transactional
	@Rollback(false)
	public void submitToSap() throws Exception {
		JSONObject jObj = service.submitToSap(userId, telephoneNumber,
				billingYear, billingMonth);
		assertEquals(jObj.get("returnCode"), "SUCCESS");
	}
}
