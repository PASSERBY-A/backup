package com.cmsz.cloudplatform.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmsz.cloudplatform.dao.PropertyDao;
import com.cmsz.cloudplatform.dao.THostDao;
import com.cmsz.cloudplatform.dao.TRackDao;
import com.cmsz.cloudplatform.exception.ServiceException;
import com.cmsz.cloudplatform.model.Property;
import com.cmsz.cloudplatform.model.THost;
import com.cmsz.cloudplatform.model.TRack;
import com.cmsz.cloudplatform.model.request.ListPropertyRequest;
import com.cmsz.cloudplatform.model.request.SavePropertyRequest;
import com.cmsz.cloudplatform.model.response.EntityResponse;
import com.cmsz.cloudplatform.model.response.ListResponse;
import com.cmsz.cloudplatform.service.PropertyManager;
import com.hp.core.service.impl.GenericManagerImpl;

@Service(value = "propertyManager")
public class PropertyManagerImpl extends GenericManagerImpl<Property, Long> implements PropertyManager {
	private PropertyDao propertyDao;

	@Autowired
	private THostDao tHostDao = null;

	@Autowired
	private TRackDao tRackDao = null;

	public PropertyManagerImpl() {
		super();
	}

	public PropertyManagerImpl(PropertyDao propertyDao) {
		super(propertyDao);
		this.propertyDao = propertyDao;
	}

	@Autowired
	public void setPropertyDao(PropertyDao propertyDao) {
		this.dao = propertyDao;
		this.propertyDao = propertyDao;
	}

	public ListResponse<Property> listProperty(ListPropertyRequest request) {
		ListResponse<Property> result = new ListResponse<Property>();
		List<Property> list = null;
		Integer count = 0;
		Property example = new Property();
		if (null != request.getId()) {
			example = propertyDao.get(request.getId());
			list = new ArrayList<Property>();
			list.add(example);
		} else {
			example.setSection(request.getSection());
			if (StringUtils.isNotBlank(request.getKeyword())) {
				example.setCode(request.getKeyword());
				list = (List<Property>) propertyDao.findPropertyListByExample(example, request.getPage(), request.getPagesize());
			} else {

				example.setType(request.getType());
				example.setVendor(request.getVendor());
				example.setModel(request.getModel());
				example.setCode(request.getCode());
				example.setSerial_num(request.getSerial_num());
				example.setStatus(request.getStatus());
				example.setOwner(request.getOwner());
				example.setStart_date(request.getStart_date());
				example.setEnd_date(request.getEnd_date());
				// TODO filling other fields
				count = propertyDao.countByExample(example);
				list = (List<Property>) propertyDao.findPropertyListByExample(example, request.getPage(), request.getPagesize());

			}
		}

		result.setCount(count);
		result.setResponses(list);

		return result;
	}

	public EntityResponse<Property> saveProperty(SavePropertyRequest request) {
		EntityResponse<Property> result = null;
		Property entity = new Property();

		BeanUtils.copyProperties(request, entity);

		if (null != entity.getId() && 0 != entity.getId()) {
			Property oldEntity = null;
			oldEntity = this.propertyDao.get(entity.getId());
			if (null == oldEntity) {
				// TODO
				throw new ServiceException("The entity[id=" + entity.getId() + "] is not exists. ");
			}
			// not update these fields.
//			entity.setType(oldEntity.getType());
//			entity.setVendor(oldEntity.getVendor());
//			entity.setModel(oldEntity.getModel());
//			entity.setCode(oldEntity.getCode());
//			entity.setSerial_num(oldEntity.getSerial_num());
			// TODO 不知道为什么不需要修改
			// entity.setOwner(oldEntity.getOwner());
			// entity.setU_high(oldEntity.getU_high());
			// entity.setStart_date(oldEntity.getStart_date());
			// entity.setEnd_date(oldEntity.getEnd_date());
			// entity.setExpire_date(oldEntity.getExpire_date());
			entity.setCreatedBy(oldEntity.getCreatedBy());
			entity.setCreatedOn(oldEntity.getCreatedOn());
			entity.setModifiedBy(request.getLoginId());
			entity.setModifiedOn(new Date());
		} else {
			entity.setCreatedBy(request.getLoginId());
			entity.setCreatedOn(new Date());
			entity.setModifiedBy(request.getLoginId());
			entity.setModifiedOn(new Date());
		}

		// TODO handle exception
		entity = this.propertyDao.save(entity);

		// 保存Host记录和Rack记录
		if (Property.TYPE_X86HOST.equals(entity.getType()) || Property.TYPE_X86RACKHOST.equals(entity.getType())) {
			//THost thost = tHostDao.getTHostBySeriralNumber(entity.getSerial_num());
			THost thost = null;
			if(request.getHostId()!=null && request.getHostId()!=0){
				thost = tHostDao.get(request.getHostId()); 
			}
			if (thost == null) {
				thost = new THost();
				thost.setCreatedBy(request.getLoginId());
				thost.setCreatedOn(new Date());
			}
			thost.setManufacturer(request.getVendor());
			thost.setSerialnumber(request.getSerial_num());
			thost.setCpucores(request.getCpucores());
			thost.setCpucount(Integer.parseInt(request.getCpu_account()));
			thost.setNic(request.getNic());
			thost.setServername(request.getServername());
			thost.setCputype(request.getCputype());
			thost.setMemory(Integer.parseInt(request.getMemory_size()));
			thost.setProductname(request.getProductname());
			thost.setHostname(request.getHostname());
			thost.setResourcepoolid(request.getResourcepoolid());
			thost.setSaveStatus(THost.SAVE_STATUS_DEAL);
			thost.setModifiedBy(request.getLoginId());
			thost.setModifiedOn(new Date());
			thost.setStatus(THost.STATUS_ARCHIVE);
			thost.setResourcepoolid(request.getResourcepoolid());

			thost.setType(Property.TYPE_X86HOST.equals(entity.getType()) ? THost.TYPE_X86_ENCLOSURE : THost.TYPE_X86_RACK);

			tHostDao.save(thost);

		} else if (Property.TYPE_X86OABOX.equals(entity.getType())) {
			TRack track = null;
			if(request.getRackId()!=null && request.getRackId()!=0){
				track = tRackDao.get(request.getRackId()); 
			}
			if (track == null) {
				track = new TRack();
				track.setCreatedBy(request.getLoginId());
				track.setCreatedOn(new Date());
				track.setSerialnumber(request.getSerial_num());
				track.setType(request.getModel());// 刀箱类型
			}
			track.setName(request.getProductname());// 刀箱名称
			track.setUuid(request.getUuid());
			track.setSerialnumber(request.getSerial_num());
			track.setSaveStatus(TRack.SAVE_STATUS_DEAL);
			track.setType(request.getModel());
			track.setModifiedBy(request.getLoginId());
			track.setModifiedOn(new Date());
			track.setStatus(TRack.STATUS_ARCHIVE);
			
			tRackDao.save(track);
		}

		result = new EntityResponse<Property>();
		result.setEntity(entity);

		return result;
	}

	@Override
	public int deleteProperty(String serialnumber) {

		if (StringUtils.isNotBlank(serialnumber)) {
			Property example = new Property();
			example.setSerial_num(serialnumber);
			List<Property> list = (List<Property>) findByExample(example);
			if (list != null && list.size() > 0) {
				for (Property p : list) {
					propertyDao.remove(p);
				}
				return list.size();
			}
		}

		return 0;
	}

}
