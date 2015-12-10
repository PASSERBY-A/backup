 package com.cmsz.cloudplatform.dao;

import java.util.List;

import com.cmsz.cloudplatform.model.THba;
import com.hp.core.dao.GenericDao;

public interface THbaDao extends GenericDao<THba,Long> {
	public int removeHbaBySerialNumber(String sn);
	
	public List<THba> listBySN(String sn);
}
