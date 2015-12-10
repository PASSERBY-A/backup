package com.cmsz.cloudplatform.service;

import com.cmsz.cloudplatform.model.TEquipmentevent;
import com.cmsz.cloudplatform.model.request.ListTEquipmenteventRequest;
import com.cmsz.cloudplatform.model.response.ListResponse;
import com.hp.core.service.GenericManager;

public interface EquipmentEventManager extends
		GenericManager<TEquipmentevent, Long> {
	public ListResponse<TEquipmentevent> list(ListTEquipmenteventRequest request);

}
