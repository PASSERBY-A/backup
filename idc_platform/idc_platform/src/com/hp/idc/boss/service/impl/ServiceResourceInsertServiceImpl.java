package com.hp.idc.boss.service.impl;

import java.io.FilenameFilter;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;

import com.hp.idc.boss.BOSSBase;
import com.hp.idc.boss.service.ServiceResourceInsertService;
import com.hp.idc.business.entity.Service;
import com.hp.idc.business.entity.ServiceResource;
import com.hp.idc.business.entity.ServiceResourcePK;
import com.hp.idc.resm.model.Model;
import com.hp.idc.resm.service.ServiceManager;

public class ServiceResourceInsertServiceImpl extends BOSSBase implements
		ServiceResourceInsertService {

	@Override
	public boolean saveResult(Map<String, String> result) throws Exception {
		ServiceResource serviceResource = new ServiceResource();
		ServiceResourcePK serviceResourcePK = new ServiceResourcePK();
		try {
			BeanUtils.populate(serviceResource, result);
			String resModelId = result.get("resModelId");
			String id = result.get("id");
			if (id != null && !"".equals(id) && resModelId != null
					&& !"".equals(resModelId)) {
				Long idL = Long.parseLong(id);
				Service service = getServiceDao().get(idL);
				if (service == null || "".equals(service.getName())) {
					setErrorMsg("缺少对应的服务");
					return false;
				}
				Model model = ServiceManager.getModelService().getModelById(
						resModelId);
				if (model == null || "".equals(model.getName())) {
					setErrorMsg("缺少对应的模型");
					return false;
				}
				serviceResourcePK.setResModelId(resModelId);
				serviceResourcePK.setService(service);
			} else {
				setErrorMsg("缺少关键数据");
				return false;
			}
			serviceResource.setId(serviceResourcePK);
			getServiceResourceDao().save(serviceResource);
			return true;
		} catch (Exception e) {
			setErrorMsg(e.getMessage());
			return false;
		}
	}

	FilenameFilter filenameFilter = new WildcardFileFilter("idcserviceconfig*.unl"); 
    
	@Override
	public FilenameFilter getFilenameFilter() {
		return filenameFilter;
	}
}
