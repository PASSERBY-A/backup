package com.hp.core;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class BaseTestCase {
	private static ApplicationContext context = null;
	
	public static ApplicationContext getApplicationContext() {
		if (null == context) { 
			context = new ClassPathXmlApplicationContext(new String[]{"applicationContext.xml"});
		}
		return context;
	}
}
