package com.cmsz.cloudplatform.service;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;

import com.cmsz.cloudplatform.BaseTestCase;
import com.cmsz.cloudplatform.model.Property;
import com.cmsz.cloudplatform.model.request.ListPropertyRequest;
import com.cmsz.cloudplatform.model.response.ListResponse;
import com.cmsz.cloudplatform.service.impl.GenericCloudServerManagerImpl;
import com.cmsz.cloudplatform.utils.RequestWrapper;
import com.hp.config.service.DbConfigManager;

public class PropertyManagerTestCase extends BaseTestCase {
	public void testListProperty() {
		PropertyManager bean1 = (PropertyManager) getApplicationContext().getBean("propertyManager");
		ListPropertyRequest request = new ListPropertyRequest();
		
//		RequestWrapper.getInstance().wrapRequest2(request, this.requestParams);
		Map<String, Object[]> rps = new HashMap<String, Object[]>(); 
//		rps.put("code", new Object[]{"10"});
		rps.put("page", new Object[]{"1"});
		rps.put("pagesize", new Object[]{"1"});
//		rps.put("start_date", new Object[]{"2013-11-11"});
		RequestWrapper.getInstance().wrapRequest2(request, rps);
		System.out.println(request);
//		request.setCode("10");
		ListResponse<Property> list = ( ListResponse<Property>)bean1.listProperty(request);
		
		System.out.println(list.getResponses().size());
	}
	
	public static void main(String[] args) throws SQLException {
		PropertyManagerTestCase tc = new PropertyManagerTestCase();
		tc.testListProperty();
	}
}
