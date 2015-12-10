package com.cmsz.cloudplatform.dao;

import java.util.List;

import com.cmsz.cloudplatform.model.TEquipmentenroll;
import com.hp.core.dao.GenericDao;

public interface TEquipmentenrollDao extends GenericDao<TEquipmentenroll,Long> {
	
	public List<TEquipmentenroll> getAllAvailable();
	
	public List<TEquipmentenroll> getList(TEquipmentenroll tEquipmentenroll);
}
