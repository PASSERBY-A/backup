package com.hp.config.dao;

import com.hp.config.model.DbConfig;
import com.hp.core.dao.GenericDao;

/**
 * 
 * @author Zhefang Chen
 *
 */
public interface DbConfigDao extends GenericDao<DbConfig, Long> {

	DbConfig getDbConfigByKey(String key);
}
