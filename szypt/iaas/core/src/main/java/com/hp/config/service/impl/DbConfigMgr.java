/**
 * 
 * 
 */
package com.hp.config.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;

import com.hp.config.model.DbConfig;
import com.hp.config.service.DbConfigManager;
import com.hp.core.configuration.AbstractConfigManager;
import com.hp.util.SpringUtil;
import com.hp.util.StringUtil;
import com.hp.util.CoreOptionUtil;


/**
 * 
 * @author Zhefang Chen
 *
 */
public class DbConfigMgr extends AbstractConfigManager {
	private static final Log log = LogFactory.getLog(DbConfigMgr.class);
	
	private static final long	serialVersionUID	= 6424579799432290657L;
	private static DbConfigMgr	configFactory		= null;
	private static Date lastLoadTime				= new Date();
	private static DbConfigManager dbConfigManager 		= null;
	
	
	private DbConfigMgr() {		
		super();
	}
	public static DbConfigMgr getInstance() {
        ApplicationContext ctx = SpringUtil.getApplicationContext();
		return getInstance(ctx);
	}
	
	public static DbConfigMgr getInstance(ApplicationContext ctx) {
		Long spacetime = CoreOptionUtil.obtainReloadSpacetimeForDbConfig();

		Date currentTime = new Date();
		long time = currentTime.getTime() - lastLoadTime.getTime();
		
		if (null == configFactory || time > spacetime) {
			if (log.isDebugEnabled()) {
				log.debug( "Current Time: " + currentTime + " - last Load Time: " + lastLoadTime + " == time = " + time  + " ; spacetime = " + spacetime + ";time > spacetime = "+(time>spacetime));
			}
			dbConfigManager = (DbConfigManager)ctx.getBean("dbConfigManager");
			configFactory = new DbConfigMgr();
			configFactory.load();
			lastLoadTime = currentTime;
		}
		return configFactory;
	}

	@Override
	public String getEncryptedValue(final String key) {
		try {			
			String value = this.getValue(key);			
			return StringUtil.decrypt(value);
		} catch (Exception e) {
			throw new RuntimeException(e);			
		}
	}

	@Override
	public void load() {	
		log.info("load application config start ..... ");
		
		List<DbConfig> configs = this.dbConfigManager.getAll();
//		if (log.isDebugEnabled())
//			log.debug("--------configs-------" + configs);
		if (null != configs) {
			for (DbConfig config : configs) {
				log.info("  " + config.getKey() + " = " + config.getValue());
				
				this._config.put(config.getKey(), null == config.getValue() ? "" : config.getValue());
			}
		} else {
			log.info("dbConfig list is null");
		}
		
		log.info("load appliction config end.");
		lastLoadTime = new Date();	// last load time
	}
	
	@Override
	public void reload() {
		this.load();
	}

}
