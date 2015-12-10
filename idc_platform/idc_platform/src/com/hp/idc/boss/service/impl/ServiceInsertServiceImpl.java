package com.hp.idc.boss.service.impl;

import java.io.FilenameFilter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;

import com.hp.idc.boss.BOSSBase;
import com.hp.idc.boss.service.ServiceInsertService;
import com.hp.idc.business.entity.Service;

public class ServiceInsertServiceImpl extends BOSSBase implements ServiceInsertService {

	@Override
	public boolean saveResult(Map<String, String> result) throws Exception {
		Service service = new Service();
		try {
			BeanUtils.populate(service, result);
			DateFormat df = new SimpleDateFormat(getTimeFormat());
			service.setCreateDate(df.parse(result.get("create_date")));
			if (result.get("parent_service_id") != null
					&& !"".equals(result.get("parent_service_id"))) {
				Long parentId = Long.parseLong(result.get("parent_service_id"));
				if (parentId != service.getId()) {
					Service parentService = getServiceDao().get(parentId);
					if (parentService != null
							&& !"".equals(parentService.getName())) {
						service.setParentService(parentService);
					}
				}
			}
			getServiceDao().save(service);
			return true;
		} catch (Exception e) {
			setErrorMsg(e.getMessage());
			return false;
		}
	}

	FilenameFilter filenameFilter = new WildcardFileFilter("idcservicet*.unl");
    
	@Override
	public FilenameFilter getFilenameFilter() {
		return filenameFilter;
	}
}
