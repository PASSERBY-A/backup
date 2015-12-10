package com.cmsz.cloudplatform.dao;


import java.util.List;

import com.cmsz.cloudplatform.model.Log;
import com.cmsz.cloudplatform.model.request.ListLogRequest;
import com.hp.core.dao.GenericDao;

public interface LogDao extends GenericDao<Log,Long> {
	public int count(ListLogRequest logParam);
	
	public List<Log> list(ListLogRequest logParam, int pageIndex, int pageSize);
	
	public void cleanLog();
}
