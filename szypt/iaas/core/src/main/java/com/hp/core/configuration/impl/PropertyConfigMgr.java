package com.hp.core.configuration.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Properties;

import com.hp.core.configuration.AbstractConfigManager;
import com.hp.util.ClassLoaderUtil;

/**
 * Property configuration file manager
 * 
 * @author Zhefang Chen
 * 
 */
public class PropertyConfigMgr extends AbstractConfigManager {

	private static final long	serialVersionUID	= 4894211184301378036L;
	protected transient String	propertyName		= null;

	/**
	 * 
	 */
	public PropertyConfigMgr(final String configProperties) {
		super();
		this.propertyName = configProperties;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hgc.fn.framework.configuration.inf.AbstractConfig#getEncryptedValue(java.lang.String)
	 */
	@Override
	public String getEncryptedValue(final String key) {		
			String value = this.getValue(key);
			return null;//TODO StringUtil.decrypt(value);
	}

	/**
	 * load property config file
	 */
	public void load() {
		InputStream inStream = null;
		try {
			System.out.println("Configuration properties file: " + this.propertyName + " loading") ;
			inStream = ClassLoaderUtil.getResourceAsStream(this.propertyName, PropertyConfigMgr.class);

			if (null == inStream) {
				throw new RuntimeException("Not found configurtion file. file name is " + this.propertyName);
			}
			Properties props = new Properties();
			props.load(inStream);

			Iterator<Object> itr = props.keySet().iterator();
			while (itr.hasNext()) {
				String key = (String) itr.next();
				this._config.put(key, props.getProperty(key));

			}			
			System.out.println("Configuration properties file: " + this.propertyName + " loaded") ;
		} catch (IOException ioe) {			
			throw new RuntimeException("Not found configurtion file. file name is " + this.propertyName);
						
		} finally {
			try {
				if (inStream != null) {
					inStream.close();
				}
			} catch (Exception ex) {				
				throw new RuntimeException("Close configurtion file input stream error. file name is " + this.propertyName);
			}
		}
	}

	@Override
	public void reload() {
		load();
	}

	public String getPropertyName() {
		return this.propertyName;
	}
	
}
