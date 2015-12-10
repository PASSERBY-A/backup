package com.cmsz.cloudplatform.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmsz.cloudplatform.model.ResourcePool;
import com.cmsz.cloudplatform.service.ProvisionAttributeValueManager;
import com.cmsz.cloudplatform.service.ResourcePoolManager;
@Service("newProjectAttributeValueManager")
public class NewProjectAttributeValueManagerImpl extends
		GenericCloudServerManagerImpl implements ProvisionAttributeValueManager {

	private String EXTERNAL_ATTRIBUTE1 ="resourcePoolId";
	
	@Autowired
	private ResourcePoolManager resourcePoolManager;
	
	@Override
	public Map<String, Object> getExtval(String attributeName, String sessionkey, Map<String, Object[]> valueParam) {
		if(EXTERNAL_ATTRIBUTE1.equalsIgnoreCase(attributeName)){
			List<ResourcePool> pools = resourcePoolManager.getAll();
			Map<String,Object> result = new HashMap<String,Object>();
			for(int i=0 ; i < pools.size() ; ++ i){
				ResourcePool p = pools.get(i);
				result.put(p.getResourcePoolId(),p.getName());
			}
			result.put("all","所有资源池");
			return result;
		}
		return null;
	}
	
}
