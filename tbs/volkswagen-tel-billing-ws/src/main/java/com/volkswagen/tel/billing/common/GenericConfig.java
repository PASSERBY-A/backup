package com.volkswagen.tel.billing.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class GenericConfig {
	private static final Logger log = LoggerFactory.getLogger(GenericConfig.class);

	private static String sysconfigPath=null;
	
	public static boolean enableSapSubmit = false;

	public static String sapSubmitDirectory = null;
	
	public static String onlineMonth="201407";
	
	public static String reportBeginMonth;
	
	//public static boolean chinamobile=false;
	
	//public static boolean chinaunicom=false;
	
	//public static boolean sum = false;

	static Properties prop = new Properties();
	
	static{
		sysconfigPath = GenericConfig.class.getResource("/sysConfig.properties").getPath();
		
		InputStream in=null;
		
		try {
			    in = new FileInputStream(new File(sysconfigPath));
				 
				prop.load(in);
				
				enableSapSubmit = Boolean.parseBoolean(prop.getProperty("system.enablesap"));
				
				sapSubmitDirectory = prop.getProperty("system.sapsubmitdirectory");
				
				onlineMonth = prop.getProperty("system.onlinemonth");
				
				reportBeginMonth = prop.getProperty("report.beginmonth");
				
				//chinamobile = Boolean.valueOf(prop.getProperty("chinamobile"));
				
				//chinaunicom = Boolean.valueOf(prop.getProperty("Chinaunicom"));
				
				//sum =  Boolean.valueOf(prop.getProperty("sum"));
				
		} catch (Exception e) {
			log.error("Could not find sysConfig.properties in classpath!");
			log.error(e.getMessage());
		}finally{
			
			IOUtils.closeQuietly(in);
		}
	}
	
	
	public static void write(String key,String value)  
	{
		prop.setProperty(key, value);
		
		if(sysconfigPath!=null)
		{
			OutputStream out = null;
			try {
				out =  new FileOutputStream(new File(sysconfigPath));
				
				prop.store(out, "update");
				
			}  catch (Exception e) {
				log.error("Could not find sysConfig.properties in classpath!");
				log.error(e.getMessage());
			}finally{
				IOUtils.closeQuietly(out);
			}
		}else{
			throw new RuntimeException("sysconfigPath can not empty!");
		}
		
	} 
	

	public static String get(String key)  
	{
		return prop.getProperty(key);
	}
	
	
	public static void main(String[] args) throws FileNotFoundException, IOException {
		
		
		//System.out.println(GenericConfig.enableSapSubmit);
		//System.out.println(GenericConfig.sapSubmitDirectory);
		//System.out.println(new File(sysconfigPath));
		//System.out.println(GenericConfig.get("chinamobile"));
		
		
		GenericConfig.write("chinamobile", "true");
		GenericConfig.write("chinaunicom", "true");
		GenericConfig.write("sum", "true");
	} 
	
	
	 
	
}
