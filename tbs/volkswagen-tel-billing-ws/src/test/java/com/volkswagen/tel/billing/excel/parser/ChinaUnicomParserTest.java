package com.volkswagen.tel.billing.excel.parser;

import org.eclipse.core.runtime.Assert;
import org.junit.Test;

import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class ChinaUnicomParserTest {
	private static final Logger log = LoggerFactory
			.getLogger(ChinaUnicomParserTest.class);

	@Autowired
	private ChinaUnicomParser cmParser;
	
	@Test
	public void testParsing() {
		log.info(">>> testParsing start.");
		String filePath = ""; // specify an excel file in 97-2003 version.

		try {
			cmParser.parseExcel(filePath);
			cmParser.persistDataToDatabase();
			
			Assert.isTrue(1==1);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		
		log.info(">>> testParsing end.");
	}
}
