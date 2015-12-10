package com.hp.config.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hp.config.dao.DbConfigDao;
import com.hp.config.model.DbConfig;
import com.hp.config.service.DbConfigManager;
import com.hp.core.service.impl.GenericManagerImpl;

/**
 * 
 * @author Zhefang Chen
 *
 */
@Service("dbConfigManager")
public class DbConfigManagerImpl extends GenericManagerImpl<DbConfig, Long> implements DbConfigManager  {
	private DbConfigDao dbConfigDao;
	
	@Autowired
	public void setDbConfigDao(DbConfigDao dbConfigDao) {
		this.dbConfigDao = dbConfigDao;
		this.dao = dbConfigDao;
	}

	public DbConfigManagerImpl() {
		super();
	}
	
    public DbConfigManagerImpl(DbConfigDao dbConfigDao) {
        super(dbConfigDao);
        this.dbConfigDao = dbConfigDao;
        this.dao = dbConfigDao;
    }

	public DbConfig getDbConfigByKey(String key) {
		return this.dbConfigDao.getDbConfigByKey(key);
	}
}
