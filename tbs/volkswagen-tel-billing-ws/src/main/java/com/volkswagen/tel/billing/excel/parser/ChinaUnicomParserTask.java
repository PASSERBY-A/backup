package com.volkswagen.tel.billing.excel.parser;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.volkswagen.tel.billing.billcall.biz.util.TaskQueue;
import com.volkswagen.tel.billing.billcall.jpa.service.NotifyDaoService;
import com.volkswagen.tel.billing.common.GenericConfig;
import com.volkswagen.tel.billing.common.util.CommonUtil;

public class ChinaUnicomParserTask {
	
	private static final Logger log = LoggerFactory.getLogger(ChinaUnicomParserTask.class);
	
	private static final String DIR_DISCRIMINATOR = "path-for-china-unicom";
	
	@Autowired
	private ChinaUnicomParser parser;
	
	@Resource(name="excelFilePathConfig")
	private Map<String, String> excelFilePathConfig;	
	
	@Autowired
	private NotifyDaoService NotifyDaoServiceImpl;
	
	private Set<String> set;
	
	public ChinaUnicomParserTask() throws Exception {
		
		set = new HashSet<String>();
	}
	
	public void parsingMethod() {
		
		log.info(">>> ChinaUnicomParserTask parsingMethod start ...");
		
		String dirPath = excelFilePathConfig.get(DIR_DISCRIMINATOR);

		File dir = new File(dirPath);
		File[] files = dir.listFiles();
		for (int i = 0; files != null && i < files.length; i++) {
			String filePath = files[i].getAbsolutePath();
			log.info(">>> filePath="+filePath);
			
			if (filePath.endsWith(".xls")) {
				log.info(filePath);
				// - parse the excel file.
				try {
					parser.parseExcel(filePath);
					
					parser.persistDataToDatabase();
					
					boolean isDeleted = files[i].delete();
					
					log.info(">>> Delete("+isDeleted+"): " + files[i].getAbsolutePath());
					
					
					set.add(DIR_DISCRIMINATOR);
					
				} catch (Exception e) {
					log.error(e.getMessage(), e);
				}
			}
		}
		
		log.info(">>> ChinaUnicomParserTask parsingMethod end.");
		
		
		if(set.size()!=0)
		{
			String filePath = FileUtils.getTempDirectoryPath();
			File chinaunicom= new File(filePath+"chinaunicom.tbs");
			chinaunicom.mkdir();
			
			//GenericConfig.write("chinaunicom", "true");
			set.clear();
			NotifyDaoServiceImpl.notification();
		}
		
	}

	
	
	
public static void main(String[] args) throws Exception {
		
		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
		
		ChinaUnicomParserTask t = context.getBean(ChinaUnicomParserTask.class);
		
		t.parsingMethod();
			
		
	}
	
}
