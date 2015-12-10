package com.volkswagen.tel.billing.billcall.jpa.service;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class BillCallRecordDaoServiceTest {
	private static final Logger log = LoggerFactory
			.getLogger(BillCallRecordDaoServiceTest.class);

	@Resource
	private BillCallRecordDaoService billCallRecordDaoService;

	@Test
	public void saveEntity() {
	}
}
