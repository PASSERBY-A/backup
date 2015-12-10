package com.cmsz.cloudplatform.dao;

import java.util.List;

import com.cmsz.cloudplatform.model.THost;
import com.cmsz.cloudplatform.model.request.ListTHostRequest;
import com.hp.core.dao.GenericDao;

public interface THostDao extends GenericDao<THost,Long> {
	public THost getTHostBySeriralNumber(String serialNumber);
	public THost getTHostBySeriralNumber(String serialNumber, Integer type);
	public THost getTHostByIp(String ip);
	
	List<THost> findTHostList(ListTHostRequest queryRequest, int pageSize, int pageNumber);
	public Integer countByExample(ListTHostRequest queryRequest);
}
