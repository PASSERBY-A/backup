/*
 * Copyright (c) 2009 Hutchison Global Communications Limited,
 *
 * All Rights Reserved.
 * This document contains proprietary information that shall be
 * distributed or routed only within HGC, and its authorized
 * clients, except with written permission of HGC.
 *
 */
package com.hp.core.configuration;


import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.hp.core.Constants;
import com.hp.util.StringUtil;

/**
 * Abstract configuration implement class
 * 
 * @author Zhefang Chen
 * 
 */
public abstract class AbstractConfig implements Config, Serializable {

	private static final long serialVersionUID = 1L;
	protected transient Map<String, String>	_config;
	protected String filePath = null;

	protected AbstractConfig() {
		super();		
		_config = new ConcurrentHashMap<String, String>();
	}

	public String getContextPath() {
		return filePath;
	}
	
	public String getContextFilePath(final String key) {
		String fname =  getValue(key);
		
		if (StringUtil.isNotEmpty(fname) && fname.startsWith("/")) {
			fname = fname.substring(1);
		}
		return (filePath + fname);
	}
		
	public abstract String getEncryptedValue(String key);

	public Boolean getBooleanValue(final String key) {
		String str = getValue(key);
		if (str == null) {
			return null;
		}
		return Boolean.parseBoolean(str);
	}

	public Date getDateValue(final String key) {
		String str = getValue(key);
		if (str == null) {
			return null;
		}
		SimpleDateFormat formatter = new SimpleDateFormat(Constants.DEFAULT_DATE_PATTERN);
		Date date = null;
		try {
			date = formatter.parse(str);
		} catch (Exception e) {
			//
		}
		return date;
	}

	public Float getFloatValue(final String key) {
		String str = getValue(key);
		if (str == null) {
			return null;
		}
		return Float.parseFloat(str);
	}

	public Integer getIntegerValue(final String key) {
		String str = getValue(key);
		if (str == null) {
			return null;
		}
		return Integer.parseInt(str);
	}

	public Long getLongValue(final String key) {
		String str = getValue(key);
		if (str == null) {
			return null;
		}
		
		return Long.parseLong(str);
	}

	public String getValue(final String key) {
		if (this._config == null) {
			return null;
		}

		if (key == null || key.trim().equals("")) {
			System.out.println("key is empty or null");
			return null;
		}
		if (this._config.containsKey(key)) {
			String value = this._config.get(key);
			if (null != value) {
				value = value.trim();
			}
			return value;
		} else {
			System.out.println("key [ " + key + " ] not found");
			return null;
		}
	}

	public Date getTimestampValue(final String key) {
		String str = getValue(key);
		if (str == null) {
			return null;
		}
		SimpleDateFormat formatter = new SimpleDateFormat(Constants.DEFAULT_DATETIME_PATTERN);
		Date date = null;
		try {
			date = formatter.parse(str);
		} catch (Exception e) {
		}
		return date;
	}

	public Map<String, String> getValuesAsMap(final String prefix) {
		if (this._config == null) {
			return null;
		}

		Map<String, String> map = new HashMap<String, String>();
		Iterator<String> itr = this._config.keySet().iterator();
		while (itr.hasNext()) {
			String key = itr.next();
			if (key.startsWith(prefix)) {
				String value = this._config.get(key);
				map.put(key, value);
			}
		}
		return map;
	}

	public Double getDoubleValue(final String key) {
		String str = getValue(key);
		if (str == null) {
			return null;
		}
		return Double.parseDouble(str);
	}

	public Map<String, String> getConfigData() {
		HashMap<String, String> map = new HashMap<String, String>(this._config.size());
		map.putAll(this._config);
		return map;
	}
}
