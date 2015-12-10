package com.volkswagen.tel.billing.excel.parser;

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

import javax.annotation.Resource;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ChinaMobileParserTask {
	private static final Logger log = LoggerFactory.getLogger(ChinaMobileParserTask.class);
	
	private static final String DIR_DISCRIMINATOR = "path-for-china-mobile";
    private static final String SUM_DIR_DISCRIMINATOR = "path-for-china-mobile-sum";
	
	@Autowired
	private ChinaMobileParser parser;
	 
	@Resource(name="excelFilePathConfig")
	private Map<String, String> excelFilePathConfig;

	@Autowired
	private NotifyDaoService NotifyDaoServiceImpl;
	
	private Set<String> set;
	
	
	public ChinaMobileParserTask() throws Exception {
		
		set = new HashSet<String>();
	}
	
	public void parsingMethod() {
		
		
		
		log.info(">>> ChinaMobileParserTask parsingMethod start ...");
		String dirPath = excelFilePathConfig.get(DIR_DISCRIMINATOR);

		File dir = new File(dirPath);
		File[] files = dir.listFiles();
		for (int i = 0; files != null && i < files.length; i++) {
			String filePath = files[i].getAbsolutePath();
			log.info("==>> filePath="+filePath);
			if (filePath.endsWith(".xls")) {
				log.info(filePath);
				// - parse the excel file.
				try {
					parser.parseExcel(filePath);
					parser.persistDataToDatabase();

					boolean isDeleted = files[i].delete();
					
					log.info(">>> Delete("+isDeleted+"): " + files[i].getAbsolutePath());
					//表示账单已经开始导，至于有没全部倒完，可以忽略。
					set.add(DIR_DISCRIMINATOR);


				} catch (Exception e) {
					log.error(e.getMessage(), e);
				}
			}
		}

		
		
        // SUM
        dirPath = excelFilePathConfig.get(SUM_DIR_DISCRIMINATOR);

        dir = new File(dirPath);
        files = dir.listFiles();
        for (int i = 0; files != null && i < files.length; i++) {
            String filePath = files[i].getAbsolutePath();
            log.info("==>> filePath="+filePath);
            if (filePath.endsWith(".xls")) {
                log.info(filePath);
                // - parse the excel file.
                try {
                    parser.parseExcel(filePath);
                    parser.persistDataToDatabaseSum();

                    boolean isDeleted = files[i].delete();
                    log.info(">>> Delete("+isDeleted+"): " + files[i].getAbsolutePath());
                    set.add(SUM_DIR_DISCRIMINATOR);
                   
                   
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
		log.info(">>> ChinaMobileParserTask parsingMethod end.");
		
		
		
		
		if(set.size()==2)
		{
			String filePath = FileUtils.getTempDirectoryPath();
			
			File chinamobile= new File(filePath+"chinamobile.tbs");
			File sum= new File(filePath+"sum.tbs");
			
			chinamobile.mkdir();
			sum.mkdir();
			 //GenericConfig.write("chinamobile", "true");
	         //GenericConfig.write("sum", "true");
	         set.clear();
	         NotifyDaoServiceImpl.notification();
		}
         
         
		
	}

	
	

	public static void main(String[] args) throws Exception {
		
		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
		
		ChinaMobileParserTask t = context.getBean(ChinaMobileParserTask.class);
		
		/*Map<String,String> map = new HashMap<String, String>();
		map.put("path-for-china-mobile", "d:/data/ChinaMobile");
		map.put("path-for-china-unicom", "d:/data/ChinaUnicom");
		map.put("path-for-china-mobile-sum", "d:/data/sum");
		t.excelFilePathConfig = map;*/
		
		//t.parsingMethod();
		
/*		String filePath = FileUtils.getTempDirectoryPath();
		
		File chinamobile= new File(filePath+"chinamobile.tbs");
		
		File sum= new File(filePath+"sum.tbs");
		
		chinamobile.mkdir();
		sum.mkdir();
		System.out.println(FileUtils.getTempDirectoryPath());*/
	}
}
