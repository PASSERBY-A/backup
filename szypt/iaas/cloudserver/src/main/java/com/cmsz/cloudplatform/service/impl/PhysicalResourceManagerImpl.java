package com.cmsz.cloudplatform.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmsz.cloudplatform.dao.TEquipmenteventDao;
import com.cmsz.cloudplatform.dao.THostDao;
import com.cmsz.cloudplatform.dao.TRackDao;
import com.cmsz.cloudplatform.exception.ServiceException;
import com.cmsz.cloudplatform.model.Property;
import com.cmsz.cloudplatform.model.TEquipmentevent;
import com.cmsz.cloudplatform.model.THost;
import com.cmsz.cloudplatform.model.TRack;
import com.cmsz.cloudplatform.model.request.ListTHostRequest;
import com.cmsz.cloudplatform.model.request.ListTRackRequest;
import com.cmsz.cloudplatform.model.request.SaveTHostRequest;
import com.cmsz.cloudplatform.model.request.SaveTRackRequest;
import com.cmsz.cloudplatform.model.response.ListResponse;
import com.cmsz.cloudplatform.model.vo.HostPropertyVO;
import com.cmsz.cloudplatform.model.vo.RackPropertyVO;
import com.cmsz.cloudplatform.service.PhysicalResourceManager;
import com.cmsz.cloudplatform.service.PropertyManager;
import com.hp.core.service.impl.GenericManagerImpl;

@Service(value = "physicalResourceManager")
public class PhysicalResourceManagerImpl extends GenericManagerImpl implements PhysicalResourceManager {

	@Autowired
	private THostDao thostDao;

	@Autowired
	private TRackDao trackDao;

	@Autowired
	private TEquipmenteventDao tEquipmenteventDao;

	@Autowired
	private PropertyManager propertyManager;

	@Override
	public ListResponse<THost> listHost(ListTHostRequest request) {
		ListResponse<THost> result = new ListResponse<THost>();
		List<THost> list = null;
		Integer count = 0;

		if (StringUtils.isNotBlank(request.getKeyword())) {
			request.setHostName(request.getKeyword());
		}
		list = thostDao.findTHostList(request, request.getPagesize(), request.getPage());
		count = thostDao.countByExample(request);
		result.setCount(count);
		result.setResponses(list);
		return result;
	}

	@Override
	public ListResponse<TRack> listRack(ListTRackRequest request) {

		ListResponse<TRack> result = new ListResponse<TRack>();
		List<TRack> list = null;
		Integer count = 0;
		list = trackDao.findTRackList(request, request.getPagesize(), request.getPage());
		count = trackDao.countByExample(request);
		result.setCount(count);
		result.setResponses(list);
		return result;

	}

	@Override
	public void updateHostResourcePool(Long hostId, String resourcepoolid) {
		THost host = thostDao.get(hostId);
		host.setModifiedOn(new Date());
		host.setResourcepoolid(resourcepoolid);
		thostDao.save(host);

	}

	@Override
	public void archivedHost(SaveTHostRequest host) {

		THost updateObj = null;

		/* BeanUtils.copyProperties(host,saveOjb); */
		if (host.getId() != null) {
			updateObj = thostDao.get(host.getId());
			host.setCreatedBy(updateObj.getCreatedBy());
			host.setCreatedOn(updateObj.getCreatedOn());
			// BeanUtils.copyProperties(host,updateObj);
			updateObj.setModifiedOn(new Date());
			updateObj.setSaveStatus(THost.SAVE_STATUS_DEAL);
			updateObj.setStatus(THost.STATUS_ARCHIVE);
			updateObj.setModifiedBy(host.getLoginId());
			thostDao.save(updateObj);
			TEquipmentevent example = new TEquipmentevent();
			example.setSerialnumber(updateObj.getSerialnumber());
			example.setEventstatus(TEquipmentevent.EVENT_STATUS_PENDING);
			List<TEquipmentevent> events = (List<TEquipmentevent>) tEquipmenteventDao.findByExample(example);
			if (events != null && events.size() > 0) {
				for (TEquipmentevent e : events) {
					e.setEventstatus(TEquipmentevent.EVENT_STATUS_FINISH);
					e.setModifiedBy(host.getLoginId());
					e.setModifiedOn(new Date());
					tEquipmenteventDao.save(e);
				}
			}
		} else {
			throw new ServiceException("The entity " + host + "is not exists. ");
		}
	}

	@Override
	public void saveRack(TRack rack) {
		trackDao.save(rack);

	}

	@Override
	public void archivedRack(SaveTRackRequest rack) {
		TRack updateObj = null;

		/* BeanUtils.copyProperties(host,saveOjb); */
		if (rack.getId() != null) {
			updateObj = trackDao.get(rack.getId());

			updateObj.setModifiedBy(rack.getLoginId());
			updateObj.setModifiedOn(new Date());

			updateObj.setSaveStatus(TRack.SAVE_STATUS_DEAL);
			updateObj.setStatus(TRack.STATUS_ARCHIVE);
			updateObj.setSerialnumber(rack.getSerialnumber());
			updateObj.setUuid(rack.getUuid());
			updateObj.setName(rack.getName());
			updateObj.setType(rack.getType());

			trackDao.save(updateObj);
			TEquipmentevent example = new TEquipmentevent();
			example.setSerialnumber(updateObj.getSerialnumber());
			example.setEventstatus(TEquipmentevent.EVENT_STATUS_PENDING);
			List<TEquipmentevent> events = (List<TEquipmentevent>) tEquipmenteventDao.findByExample(example);
			if (events != null && events.size() > 0) {
				for (TEquipmentevent e : events) {
					e.setEventstatus(TEquipmentevent.EVENT_STATUS_FINISH);
					e.setModifiedBy(rack.getLoginId());
					e.setModifiedOn(new Date());
					tEquipmenteventDao.save(e);
				}
			}
		} else {
			throw new ServiceException("The entity " + rack + "is not exists. ");
		}

	}

	@Override
	public void removeHost(SaveTHostRequest thostRequest) {
		if (thostRequest == null || thostRequest.getId() == null) {
			throw new ServiceException("删除主机失败异常，ID为空");
		} else {
			THost thost = thostDao.get(thostRequest.getId());
			propertyManager.deleteProperty(thost.getSerialnumber());
			thostDao.remove(thost);
			/*
			 * thost.setSaveStatus(THost.SAVE_STATUS_DEAL);
			 * thost.setStatus(THost.STATUS_DELETE);
			 * thost.setModifiedBy(thostRequest.getLoginId());
			 * thost.setModifiedOn(new Date()); thostDao.save(thost);
			 */
		}
	}

	@Override
	public void removeOA(Long trackId) {
		TRack track = trackDao.get(trackId);
		propertyManager.deleteProperty(track.getSerialnumber());
		trackDao.remove(track);

	}

	@SuppressWarnings({ "unchecked" })
	@Override
	public ListResponse<HostPropertyVO> listHostProperty(ListTHostRequest request) {
		ListResponse<HostPropertyVO> result = new ListResponse<HostPropertyVO>();
		List<HostPropertyVO> list = null;

		if (request != null && request.getId()!=null && request.getId()>0) {
			// 查询具体主机信息
			list = new ArrayList<HostPropertyVO>();
			HostPropertyVO hostPropertyVO = new HostPropertyVO();
			THost thost = thostDao.get(request.getId());

			if (thost != null) {
				BeanUtils.copyProperties(thost, hostPropertyVO);
				hostPropertyVO.setId(null);
				hostPropertyVO.setStatus(null);
				hostPropertyVO.setType(null);
				hostPropertyVO.setHostId(thost.getId());
				hostPropertyVO.setHostStatus(thost.getStatus());
				hostPropertyVO.setHostType(thost.getType());
			

				Property exampleEntity = new Property();
				exampleEntity.setSerial_num(thost.getSerialnumber());
				List<Property> propertys = (List<Property>) propertyManager.findByExample(exampleEntity);
				if (CollectionUtils.isNotEmpty(propertys)) {
					BeanUtils.copyProperties(propertys.get(0), hostPropertyVO);
				}

				hostPropertyVO.setHbaList(null);
				list.add(hostPropertyVO);
			}

			result.setCount(thost == null ? 0 : 1);
			result.setResponses(list);

		}
		return result;
	}

	@Override
	public ListResponse<RackPropertyVO> listRackProperty(ListTRackRequest request) {
		ListResponse<RackPropertyVO> result = new ListResponse<RackPropertyVO>();
		List<RackPropertyVO> list = null;

		if (request != null && request.getId() != null && request.getId()!=0) {
			// 查询具体主机信息
			list = new ArrayList<RackPropertyVO>();
			RackPropertyVO rackPropertyVO = new RackPropertyVO();
			
			TRack tRack = trackDao.get(request.getId());
					//.getTRackBySeriralNumber(request.getSerialnumber());

			if (tRack != null) {
				BeanUtils.copyProperties(tRack, rackPropertyVO);
				rackPropertyVO.setId(null);
				rackPropertyVO.setStatus(null);
				//rackPropertyVO.setType();
				rackPropertyVO.setType(Property.TYPE_X86OABOX);
				rackPropertyVO.setRackId(tRack.getId());
				rackPropertyVO.setRackStatus(tRack.getStatus());
				rackPropertyVO.setSerialnumber(tRack.getSerialnumber());
				rackPropertyVO.setSerial_num(tRack.getSerialnumber());
				//rackPropertyVO.setRackType();
				rackPropertyVO.setModel(tRack.getType());
				Property exampleEntity = new Property();
				exampleEntity.setSerial_num(tRack.getSerialnumber());
				List<Property> propertys = (List<Property>) propertyManager.findByExample(exampleEntity);
				if (CollectionUtils.isNotEmpty(propertys)) {
					BeanUtils.copyProperties(propertys.get(0), rackPropertyVO);
				}

				rackPropertyVO.setBayinfos(null);
				list.add(rackPropertyVO);
			}

			result.setCount(tRack == null ? 0 : 1);
			result.setResponses(list);

		}
		return result;
	}

}
