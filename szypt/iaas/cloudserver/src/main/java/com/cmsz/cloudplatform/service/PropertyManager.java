package com.cmsz.cloudplatform.service;


import com.cmsz.cloudplatform.model.Property;
import com.cmsz.cloudplatform.model.request.ListPropertyRequest;
import com.cmsz.cloudplatform.model.request.SavePropertyRequest;
import com.cmsz.cloudplatform.model.response.EntityResponse;
import com.cmsz.cloudplatform.model.response.ListResponse;
import com.hp.core.service.GenericManager;

public interface PropertyManager extends GenericManager<Property, Long> {

	ListResponse<Property> listProperty(ListPropertyRequest request);
	EntityResponse<Property> saveProperty(SavePropertyRequest request);

	int deleteProperty(String serialnumber);
}
