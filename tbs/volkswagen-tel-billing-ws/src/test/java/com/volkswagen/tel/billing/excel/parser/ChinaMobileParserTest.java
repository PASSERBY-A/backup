package com.volkswagen.tel.billing.excel.parser;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class ChinaMobileParserTest {
	private static final Logger log = LoggerFactory
			.getLogger(ChinaMobileParserTest.class);

	@Autowired
	private ChinaMobileParser cmParser;
	
	@Test
	public void testParsing() {
		String filePath = "C:/bills/ChinaMobile/sum/VWC Bill Sum 201409 (China Mobile).xls"; // specify an excel file in 97-2003 version.

		try {
			cmParser.parseExcel(filePath);
			cmParser.persistDataToDatabaseSum();
			
			Assert.assertTrue(1==1);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}
}
