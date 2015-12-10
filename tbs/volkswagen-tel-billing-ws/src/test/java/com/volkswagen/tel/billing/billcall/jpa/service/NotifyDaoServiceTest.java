package com.volkswagen.tel.billing.billcall.jpa.service;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.volkswagen.tel.billing.billcall.jpa.domain.NotifyEntity;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class NotifyDaoServiceTest {
	private static final Logger log = LoggerFactory.getLogger(NotifyDaoServiceTest.class);

	@Resource
	private NotifyDaoService notifyDaoService;

	@Test
	public void saveEntity() {
		
		NotifyEntity entity = new NotifyEntity();
		
		entity.setContent("test");
		
		entity.setLastUpdateTime(new Date());
		
		entity.setMail("qiang.li7@hp.com");
		
		entity.setMobile("18210608090");
		
		entity.setReceiver("Extern.Qiang.Li@volkswagen.com.cn");
		
		entity.setSendFlag(false);
		
		entity.setTelephone("65507100");
		
		notifyDaoService.saveNotify(entity);
		
	}
	
	@Test
	public void findAll()
	{
		
		List<NotifyEntity> list = notifyDaoService.findAll();
		
		System.err.println(list.size());
		
	}
	
	@Test
	public void findbySendFlagFalse()
	{
		List<NotifyEntity> list = notifyDaoService.findBySendFlagFalse();
		
		System.err.println(list.size());
		
	}
	
	
	
	
	@Test
	public void setSendFlagAndSendsendTime()
	{
		
		notifyDaoService.setSendFlagAndSendsendTime(Long.valueOf(4));
		
		
	}
	
	
	@Test
	public void notification()
	{
		notifyDaoService.notification();
	}
	
	
	
	
}
