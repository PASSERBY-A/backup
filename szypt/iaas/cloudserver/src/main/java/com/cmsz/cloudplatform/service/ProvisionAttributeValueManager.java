package com.cmsz.cloudplatform.service;

import java.util.Map;


public interface ProvisionAttributeValueManager  {
	Map<String,Object> getExtval(String attributeName, String sessionkey, Map<String, Object[]> valueParam);
}
