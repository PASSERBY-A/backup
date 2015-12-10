package com.cmsz.cloudplatform.dao;

import java.util.List;

import com.cmsz.cloudplatform.model.TBayinfo;
import com.hp.core.dao.GenericDao;

public interface TBayinfoDao extends GenericDao<TBayinfo,Long> {
	public List<TBayinfo> getAllByRackId(Long rackId);
}
