package com.cmsz.cloudplatform.dao;

import java.util.List;

import com.cmsz.cloudplatform.model.TEquipmentevent;
import com.cmsz.cloudplatform.model.request.ListTEquipmenteventRequest;
import com.hp.core.dao.GenericDao;

public interface TEquipmenteventDao
		extends GenericDao<TEquipmentevent, Long> {
	public int removeUnProcessed(String serialNumber);
	
	
	List<TEquipmentevent> findList(ListTEquipmenteventRequest queryRequest, int pageSize, int pageNumber);
	public Integer countByExample(ListTEquipmenteventRequest queryRequest);
	
}
