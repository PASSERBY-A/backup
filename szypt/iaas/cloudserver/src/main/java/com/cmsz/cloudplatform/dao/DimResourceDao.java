package com.cmsz.cloudplatform.dao;

import com.cmsz.cloudplatform.model.DimResource;
import com.hp.core.dao.GenericDao;

public interface DimResourceDao extends GenericDao<DimResource,Long> {
	void deleteAll();
}
