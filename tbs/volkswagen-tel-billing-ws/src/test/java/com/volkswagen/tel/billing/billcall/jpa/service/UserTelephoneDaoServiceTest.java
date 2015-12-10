package com.volkswagen.tel.billing.billcall.jpa.service;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import com.volkswagen.tel.billing.billcall.jpa.domain.UserTelephoneEntity;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class UserTelephoneDaoServiceTest {
	private static final Logger log = LoggerFactory
			.getLogger(UserTelephoneDaoServiceTest.class);

	@Resource
	private UserTelephoneDaoService userTelephoneDaoService;

	@Test
	public void findTelephonesByUserIdAndMonth() {
		List<UserTelephoneEntity> list = userTelephoneDaoService
				.findTelephonesByUserIdAndMonth("abc", 2014, 1);
		
		Assert.isNull(list);
	}

}
