package com.cmsz.cloudplatform.service;

import com.cmsz.cloudplatform.model.TEquipmentenroll;
import com.cmsz.cloudplatform.model.request.EquipmentEnrollRequest;
import com.cmsz.cloudplatform.model.response.ListResponse;
import com.hp.core.service.GenericManager;

public interface EquipmentEnrollManager extends GenericManager<TEquipmentenroll, Long> {

	public ListResponse<TEquipmentenroll> get(EquipmentEnrollRequest request);

	public ListResponse<TEquipmentenroll> create(EquipmentEnrollRequest request);

	public ListResponse<TEquipmentenroll> update(EquipmentEnrollRequest request);

	public ListResponse<TEquipmentenroll> remove(EquipmentEnrollRequest request);
}
