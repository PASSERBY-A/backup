package com.hp.config.service;

import com.hp.config.model.DbConfig;
import com.hp.core.service.GenericManager;

/**
 * 
 * @author Zhefang Chen
 *
 */
public interface DbConfigManager extends GenericManager<DbConfig, Long>  {
	DbConfig getDbConfigByKey(String key);

}
