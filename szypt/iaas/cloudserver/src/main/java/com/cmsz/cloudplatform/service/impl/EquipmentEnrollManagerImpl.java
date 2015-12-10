package com.cmsz.cloudplatform.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmsz.cloudplatform.dao.TEquipmentenrollDao;
import com.cmsz.cloudplatform.model.TEquipmentenroll;
import com.cmsz.cloudplatform.model.request.EquipmentEnrollRequest;
import com.cmsz.cloudplatform.model.response.ListResponse;
import com.cmsz.cloudplatform.service.EquipmentEnrollManager;
import com.hp.core.service.impl.GenericManagerImpl;
import com.hp.util.StringUtil;

@Service(value = "equipmentEnrollManager")
public class EquipmentEnrollManagerImpl extends GenericManagerImpl<TEquipmentenroll, Long> implements EquipmentEnrollManager {

	private TEquipmentenrollDao equipmentEnrollDao;

	public EquipmentEnrollManagerImpl() {
		super();
	}

	public EquipmentEnrollManagerImpl(TEquipmentenrollDao equipmentEnrollDao) {
		super(equipmentEnrollDao);
	}

	@Autowired
	public void setEquipmentEnrollDao(TEquipmentenrollDao equipmentEnrollDao) {
		this.dao = equipmentEnrollDao;
		this.equipmentEnrollDao = equipmentEnrollDao;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ListResponse<TEquipmentenroll> get(EquipmentEnrollRequest request) {
		ListResponse<TEquipmentenroll> repsone = new ListResponse<TEquipmentenroll>();

		List<TEquipmentenroll> list = new ArrayList<TEquipmentenroll>();
		if (request.getId() != null) {
			list.add(this.get(request.getId()));
		} else {
			TEquipmentenroll exampleEntity = new TEquipmentenroll();
			if (StringUtil.isNotEmpty(request.getKeyword())) {
				exampleEntity.setIp("%" + request.getKeyword() + "%");
			}
			list = (List<TEquipmentenroll>) this.findByExample(null, exampleEntity, request.getPage(), request.getPageSize());
			repsone.setCount(this.countByExample(exampleEntity));
		}

		repsone.setResponses(list);
		return repsone;
	}

	@Override
	public ListResponse<TEquipmentenroll> create(EquipmentEnrollRequest request) {
		ListResponse<TEquipmentenroll> repsone = new ListResponse<TEquipmentenroll>();
		if (request != null) {
			TEquipmentenroll tEquipmentenroll = new TEquipmentenroll();

			// 验证IP是否存在
			// 同一种类型的同一个主机名称不运行重复注册。
			tEquipmentenroll.setIp(request.getIp());
			tEquipmentenroll.setTargetType(request.getTargetType());
			List<TEquipmentenroll> list = (List<TEquipmentenroll>) this.equipmentEnrollDao.getList(tEquipmentenroll);
			if (CollectionUtils.isNotEmpty(list)) {
				List<TEquipmentenroll> result = new ArrayList<TEquipmentenroll>();
				result.add(tEquipmentenroll);
				repsone.setResponses(result);
				return repsone;
			}

			BeanUtils.copyProperties(request, tEquipmentenroll);
			tEquipmentenroll.setCreatedBy(request.getLoginId());
			tEquipmentenroll.setCreatedOn(new Date());
			tEquipmentenroll.setModifiedBy(tEquipmentenroll.getCreatedBy());
			tEquipmentenroll.setModifiedOn(tEquipmentenroll.getCreatedOn());
			tEquipmentenroll = this.save(tEquipmentenroll);

			List<TEquipmentenroll> result = new ArrayList<TEquipmentenroll>();
			result.add(tEquipmentenroll);
			repsone.setResponses(result);
		}
		return repsone;
	}

	@Override
	public ListResponse<TEquipmentenroll> update(EquipmentEnrollRequest request) {
		ListResponse<TEquipmentenroll> repsone = new ListResponse<TEquipmentenroll>();
		if (request != null && request.getId() != null) {
			// 验证IP是否存在
			// 同一种类型的同一个主机名称不运行重复注册。
			TEquipmentenroll exampleEntity = new TEquipmentenroll();
			exampleEntity.setIp(request.getIp());
			exampleEntity.setTargetType(request.getTargetType());
			List<TEquipmentenroll> list = (List<TEquipmentenroll>) this.equipmentEnrollDao.getList(exampleEntity);
			if (CollectionUtils.isNotEmpty(list)) {
				for (TEquipmentenroll tee : list) {
					if (tee.getId() != request.getId()) {
						List<TEquipmentenroll> result = new ArrayList<TEquipmentenroll>();
						result.add(exampleEntity);
						repsone.setResponses(result);
						return repsone;
					}
				}
			}

			TEquipmentenroll tEquipmentenroll = this.get(request.getId());

			tEquipmentenroll.setModifiedBy(request.getLoginId());
			tEquipmentenroll.setModifiedOn(new Date());
			tEquipmentenroll.setAvailable(request.getAvailable());
			tEquipmentenroll.setDescript(request.getDescript());
			tEquipmentenroll.setIp(request.getIp());
			tEquipmentenroll.setTargetType(request.getTargetType());
			this.save(tEquipmentenroll);

			List<TEquipmentenroll> result = new ArrayList<TEquipmentenroll>();
			result.add(tEquipmentenroll);

			repsone.setResponses(result);
		}
		return repsone;
	}

	@Override
	public ListResponse<TEquipmentenroll> remove(EquipmentEnrollRequest request) {
		ListResponse<TEquipmentenroll> repsone = new ListResponse<TEquipmentenroll>();
		if (request != null && request.getId() != null) {
			TEquipmentenroll entity = this.get(request.getId());
			this.remove(entity);

			List<TEquipmentenroll> result = new ArrayList<TEquipmentenroll>();
			result.add(entity);

			repsone.setResponses(result);
		}

		return repsone;
	}

}
