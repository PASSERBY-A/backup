package com.cmsz.cloudplatform.service;

import com.cmsz.cloudplatform.model.THost;
import com.cmsz.cloudplatform.model.TRack;
import com.cmsz.cloudplatform.model.request.ListTHostRequest;
import com.cmsz.cloudplatform.model.request.ListTRackRequest;
import com.cmsz.cloudplatform.model.request.SaveTHostRequest;
import com.cmsz.cloudplatform.model.request.SaveTRackRequest;
import com.cmsz.cloudplatform.model.response.ListResponse;
import com.cmsz.cloudplatform.model.vo.HostPropertyVO;
import com.cmsz.cloudplatform.model.vo.RackPropertyVO;
import com.hp.core.service.GenericManager;
public interface PhysicalResourceManager extends GenericManager {
	
	public ListResponse<THost> listHost(ListTHostRequest request);
	
	public ListResponse<TRack> listRack(ListTRackRequest request);
	//ListResponse<Property> listProperty(ListPropertyRequest request);
	
	public void updateHostResourcePool(Long hostId,String resourcepoolid);
	
	public void archivedHost(SaveTHostRequest host);
	
	public void archivedRack(SaveTRackRequest rack);
	
	
	public void saveRack(TRack rack);
	
	public void removeHost(SaveTHostRequest thostRequest);
	
	public void removeOA(Long trackId);
	
	public ListResponse<HostPropertyVO> listHostProperty(ListTHostRequest request);
	
	
	public ListResponse<RackPropertyVO> listRackProperty(ListTRackRequest request);
}
