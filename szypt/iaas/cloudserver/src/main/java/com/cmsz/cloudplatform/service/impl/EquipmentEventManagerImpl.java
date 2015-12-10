package com.cmsz.cloudplatform.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmsz.cloudplatform.dao.TEquipmenteventDao;
import com.cmsz.cloudplatform.model.TEquipmentevent;
import com.cmsz.cloudplatform.model.request.ListTEquipmenteventRequest;
import com.cmsz.cloudplatform.model.response.ListResponse;
import com.cmsz.cloudplatform.service.EquipmentEventManager;
import com.hp.core.service.impl.GenericManagerImpl;
@Service(value="equipmentEventManager")
public class EquipmentEventManagerImpl extends GenericManagerImpl<TEquipmentevent,Long> implements EquipmentEventManager {

	
	private TEquipmenteventDao tEquipmenteventDao;
	
	public EquipmentEventManagerImpl(TEquipmenteventDao tEquipmenteventDao){
		super(tEquipmenteventDao);
		this.tEquipmenteventDao = tEquipmenteventDao;
	}
	
	
	public EquipmentEventManagerImpl(){
		super(); 
	}
	
	@Autowired
	public void settEquipmenteventDao(TEquipmenteventDao tEquipmenteventDao) {
		this.dao = tEquipmenteventDao;
		this.tEquipmenteventDao = tEquipmenteventDao;
	}



	@Override
	public ListResponse<TEquipmentevent> list(ListTEquipmenteventRequest request) {
		ListResponse<TEquipmentevent> result = new ListResponse<TEquipmentevent>();
		request.setSerialnumber(request.getKeyword());
		List<TEquipmentevent> list = null;
		Integer count = 0;
		list =tEquipmenteventDao.findList(request, request.getPagesize(),request.getPrePage());
		count = tEquipmenteventDao.countByExample(request);
		result.setCount(count);
		result.setResponses(list);
		return result;
	}


}
