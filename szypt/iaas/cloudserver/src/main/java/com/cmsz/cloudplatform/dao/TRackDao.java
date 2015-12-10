package com.cmsz.cloudplatform.dao;

import java.util.List;

import com.cmsz.cloudplatform.model.TRack;
import com.cmsz.cloudplatform.model.request.ListTRackRequest;
import com.hp.core.dao.GenericDao;

public interface TRackDao extends GenericDao<TRack,Long> {
	public TRack getTRackBySeriralNumber(String serialNumber);
	public TRack getTRackByIp(String ip);
	
	List<TRack> findTRackList(ListTRackRequest request,int pageSize,int page);
	
	public Integer countByExample(ListTRackRequest queryRequest);
}
