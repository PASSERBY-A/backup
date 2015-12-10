/*
 * Copyright (c) 2009 Hutchison Global Communications Limited,
 *
 * All Rights Reserved.
 * This document contains proprietary information that shall be
 * distributed or routed only within HGC, and its authorized
 * clients, except with written permission of HGC.
 *
 */
package com.hp.core.configuration.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.hp.core.Constants;
import com.hp.core.configuration.AbstractConfigManager;
import com.hp.util.StringUtil;

/**
 * System configuration property manager
 * 
 * @author Zhefang Chen
 *
 */
public final class AppConfigMgr extends AbstractConfigManager {
    private final Log log = LogFactory.getLog(AppConfigMgr.class);
    
	private static final long	serialVersionUID			= -7550261500010783076L;
	private static final String	CONFIG_PATH					= "app.config.path";	
	private static final String	CONFIG_FILE					= "app.config.file";
	private static final String	DEFAULT_CONFIG_PROPERTIES	= "config.properties";
	private static final String	CONFIG_PREFIX				= "config";
	private static final char	KEY_SEPARATOR				= '.';
//	private static final String SYS_FILE 					= "system.properties";
//	private static final String APP_FILE	 				= "app.properties";
//	private static final String PATH_SEPARATOR				= "/";
//	private static final String APP_NAME 					= "appname";
//	private static final String APP_ENVIROMENT				= "app.environment";
//	private static final String CONSTANT_PREFIX				= "constant";
//	private static Map<String, String> CONSTANTS			= new HashMap<String, String>();
	private static AppConfigMgr	configFactory				= null;

	static {
		configFactory = new AppConfigMgr();
		configFactory.load();
	}

	private AppConfigMgr() {
		super();
	}

	/**
	 * Obtain system configuration manager singleton instance
	 * 
	 * @return System configuration manager
	 */
	public static AppConfigMgr getInstance() {
		return configFactory;
	}

//	private String getConfigFilePath() {
//		InputStream appStream = null;
//		InputStream sysStream = null;
//		String appname = null;
//		String appenv = null;
//		String configFilePath = null;		
//		
//		try {
//			log.info("Loading Current Application Configuration properties file: " + APP_FILE + " ... ");
//			appStream = ClassLoaderUtil.getResourceAsStream(APP_FILE, SysConfigMgr.class);
//			Properties appprops = new Properties();
//			appprops.load(appStream);
//			appname = (String)appprops.get(APP_NAME);
//		
//		} catch (Exception e) {
//			String detail = "file is [" + APP_FILE + "]";
//			throwException(SystemErrorCode.E01200001000, new String[]{detail}, e);
//		} finally {
//			try {		
//				if (appStream != null) {
//					appStream.close();
//				}
//			} catch (Exception ex) {				
//				String detail = "file name is " + APP_FILE;
//				throwException(SystemErrorCode.E01200001006, new String[]{detail}, ex);
//			}
//		}
//		
//		if (StringUtil.isNullString(appname)) {
//			String detail = "item key is [" + APP_NAME + "]";
//			throwException(SystemErrorCode.E01200001001, new String[]{detail}, null);
//		}
//		
//		
//		try {
//			System.out.println("Loading System Configuration properties file: " + SYS_FILE + " ... ");			
//			sysStream = ClassLoaderUtil.getResourceAsStream(SYS_FILE, SysConfigMgr.class);
//			System.out.println("sysStream:" + sysStream);
//			Properties sysprops = new Properties();
//			sysprops.load(sysStream);
//					
//			appenv = (String)sysprops.get(APP_ENVIROMENT);
//			String filePath = (String)sysprops.get(CONFIG_PREFIX + KEY_SEPARATOR + appname);
//			
//			Iterator<Object> itr = sysprops.keySet().iterator();
//			while (itr.hasNext()) {
//				String cfgkey = (String) itr.next();
//				String _prefix = CONSTANT_PREFIX + KEY_SEPARATOR;
//				if (cfgkey.startsWith(_prefix)) {
//					String _value = sysprops.getProperty(cfgkey);
//					String _key = cfgkey.substring(_prefix.length());
//					if (StringUtil.isNotEmpty(_value)) {
//						_value = _value.trim();
//					}
//					System.out.println("CONSTANTS[ " + _key + " = " + _value + "]");
//					CONSTANTS.put(_key, _value);
//				}
//
//			}
//			
//			if (StringUtil.isNullString(filePath)) {
//				configFilePath = null;
//			} else {
//				if (filePath.startsWith(PATH_SEPARATOR)) {
//					configFilePath = appenv + filePath;
//				} else {
//					configFilePath = appenv + PATH_SEPARATOR + filePath;
//				}
//				
//				if (false == filePath.endsWith(PATH_SEPARATOR)) {
//					configFilePath = configFilePath + PATH_SEPARATOR;
//				}
//			}
//			
//		} catch (Exception e) {
//			String detail = "file name [ " + SYS_FILE + " ]";
//			throwException(SystemErrorCode.E01200001002, new String[]{detail}, e);
//		} finally {
//			try {		
//				if (sysStream != null) {
//					sysStream.close();
//				}
//			} catch (Exception ex) {				
//				String detail = "file name is " + SYS_FILE;
//				throwException(SystemErrorCode.E01200001006, new String[]{detail}, ex);
//			}
//		}
//			
//		if (StringUtil.isNullString(configFilePath)) {
//			String detail = "property item key is (" +  CONFIG_PREFIX + KEY_SEPARATOR + appname + "]";
//			throwException(SystemErrorCode.E01200001003, new String[]{detail}, null);
//		}
//		
//		return configFilePath;
//	}
//	
//	private String replaceConstants(final String src) {
//		String result = new String(""+src);
//		Iterator<String> _ci = CONSTANTS.keySet().iterator();
//		while (_ci.hasNext()) {
//			String _key = _ci.next();
//			String _value = CONSTANTS.get(_key);
//			String _target = "${" +_key + "}";
//			result = result.replace(_target, _value);						
//		}	
//		return result;
//	}	

	public void load() {
		
		log.info("Loading configuration file now....");
		PropertyConfigMgr propsConfig = null;
		String fileName = null;
		
		// try to get the config properties name from environment level
		String envPropsPath = System.getenv(CONFIG_PATH);
		String envPropsName = System.getenv(CONFIG_FILE);
		// try to get the config properties name from JVM level
		String jvmPropsPath = System.getProperty(CONFIG_PATH);
		String jvmPropsName = System.getProperty(CONFIG_FILE);
		// using default config properties name
		if (!StringUtil.isNullString(envPropsName)) {
			System.out.println("Found environment variable [ " + envPropsPath + " ].");
			System.out.println("Found environment variable [ " + envPropsName + " ].");
			filePath = envPropsPath;
			fileName = envPropsName;
			
		} else if (!StringUtil.isNullString(jvmPropsName)) {
			System.out.println("Found JVM parameter -D [ " + jvmPropsPath + " ].");			
			System.out.println("Found JVM parameter -D [ " + jvmPropsName + " ].");
			filePath = jvmPropsPath;
			fileName = jvmPropsName;

		} else {
//			filePath = ClassLoaderUtil.getResource(DEFAULT_CONFIG_PROPERTIES, SysConfigMgr.class).;
			fileName = DEFAULT_CONFIG_PROPERTIES;
						
		}
		
//		if (false == filePath.endsWith(PATH_SEPARATOR)) {
//			filePath = filePath + PATH_SEPARATOR;
//			
//		} 
		if (true == fileName.startsWith(Constants.FILE_SEP)) {
			fileName = fileName.substring(1);
		}
		
		
//		System.out.println("Configuration Properties path: " + filePath );

		propsConfig = new PropertyConfigMgr(fileName);
		
		propsConfig.load();
				
		this._config = propsConfig.getConfigData();
		
		Iterator<String> _ci = this._config.keySet().iterator();
		Map<String, String> _rcfg = new HashMap<String, String>();
		while (_ci.hasNext()) {
			String _key = _ci.next();
			String _value = propsConfig.getValue(_key);
//			_value = replaceConstants(_value);			
			log.info("config key: " + _key + " value: " + _value);
			_rcfg.put(_key, _value);
		}		
		this._config = _rcfg;
		
		String[] keys = (String[]) this._config.keySet().toArray(new String[this._config.size()]);
		for (String key : keys) {
			if (key.startsWith(CONFIG_PREFIX)) {
				propsConfig = new PropertyConfigMgr(getContextFilePath(key));
				propsConfig.load();
				int pos = key.indexOf(KEY_SEPARATOR);
				String propPrefix = key.substring(pos + 1, key.length());

				Iterator<String> _itr = propsConfig.getConfigData().keySet().iterator();
				while (_itr.hasNext()) {
					String _key = _itr.next();
					String _value = propsConfig.getValue(_key);
//					_value = replaceConstants(_value);
					log.info("config-" + propPrefix + " key: " + propPrefix + KEY_SEPARATOR + _key + " value: " + _value);
					this._config.put(propPrefix + KEY_SEPARATOR + _key, _value);
				}
			}
		}
	}

	public void reload() {
		load();
	}

	@Override
	public String getEncryptedValue(String key) {
		try {			
			String value = this.getValue(key);
			return null ;//StringUtil.decrypt(value);
		} catch (Exception e) {
			throw new RuntimeException(e);			
		}
	}
	
	public static void main(String[] args) {
	//	String rowcount = AppConfigMgr.getInstance().getValue(AppConfigKey.DATATABLE_ROWCOUNT);
	//	System.out.println(rowcount);
		
	}
}
