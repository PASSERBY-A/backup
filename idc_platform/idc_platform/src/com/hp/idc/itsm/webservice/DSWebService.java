/**
 * 
 */
package com.hp.idc.itsm.webservice;

import com.hp.idc.context.util.ContextUtil;
import com.hp.idc.itsm.dsm.DSMCenter;

/**
 * @author <a href="mailto:lihz@lianchuang.com">FluteD</a>
 * 
 */
public class DSWebService {

	public String registerSystem(String xml) {
		DSMCenter s = (DSMCenter) ContextUtil.getBean("DSMCenter");
		if (s != null)
			return s.registerSystem(xml);
		return "";
	}
	
	public void getUpdateByOid(String category,int oid) {
		DSMCenter s = (DSMCenter) ContextUtil.getBean("DSMCenter");
		if (s != null)
			s.getUpdateByOid(category, oid);
	}
	
	public void getUpdateById(String category,String id) {
		DSMCenter s = (DSMCenter) ContextUtil.getBean("DSMCenter");
		if (s != null)
			s.getUpdateById(category, id);
	}
	
	public boolean isActive() {
		return true;
	}
}
