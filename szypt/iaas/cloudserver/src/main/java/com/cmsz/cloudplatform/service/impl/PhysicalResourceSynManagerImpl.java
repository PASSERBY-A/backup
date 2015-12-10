package com.cmsz.cloudplatform.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmsz.cloudplatform.dao.PropertyDao;
import com.cmsz.cloudplatform.dao.TBayinfoDao;
import com.cmsz.cloudplatform.dao.TEquipmentenrollDao;
import com.cmsz.cloudplatform.dao.TEquipmenteventDao;
import com.cmsz.cloudplatform.dao.THbaDao;
import com.cmsz.cloudplatform.dao.THostDao;
import com.cmsz.cloudplatform.dao.TRackDao;
import com.cmsz.cloudplatform.dto.HBA;
import com.cmsz.cloudplatform.dto.Host;
import com.cmsz.cloudplatform.dto.x86.BayInfo;
import com.cmsz.cloudplatform.dto.x86.Rack;
import com.cmsz.cloudplatform.model.Property;
import com.cmsz.cloudplatform.model.TBayinfo;
import com.cmsz.cloudplatform.model.TEquipmentenroll;
import com.cmsz.cloudplatform.model.TEquipmentevent;
import com.cmsz.cloudplatform.model.THba;
import com.cmsz.cloudplatform.model.THost;
import com.cmsz.cloudplatform.model.TRack;
import com.cmsz.cloudplatform.service.PhysicalResourceSynManager;
import com.cmsz.cloudplatform.spi.plugin.hpvm.HPPhysicalResourceService;
import com.cmsz.cloudplatform.spi.plugin.x86.X86PhysicalResourceService;

@Service(value = "physicalResourceSynManager")
public class PhysicalResourceSynManagerImpl
		implements PhysicalResourceSynManager {

	/**
	 * Slf4j Logger
	 **/
	private static Logger logger = LoggerFactory.getLogger(PhysicalResourceSynManagerImpl.class);

	@Autowired
	private TEquipmentenrollDao equipmentenrollDao;

	@Autowired
	private TEquipmenteventDao equipmenteventDao;

	@Autowired
	private THostDao thostDao;

	@Autowired
	private THbaDao hbaDao;

	@Autowired
	private TRackDao trackDao;

	@Autowired
	private TBayinfoDao tbayinfoDao;

	@Autowired
	private PropertyDao propertyDao;

	@Autowired
	private X86PhysicalResourceService x86PhysicalResourceService;

	@Autowired
	private HPPhysicalResourceService hpPhysicalResourceService;

	public PhysicalResourceSynManagerImpl() {
	}

	// @Override
	// public void synResource() {
	// List<TEquipmentenroll> equipmentEnrollList = findEquipmentEnroll();
	// if (equipmentEnrollList == null || equipmentEnrollList.size() == 0) {
	// return;
	// }
	//
	// for (TEquipmentenroll equipEnroll : equipmentEnrollList) {
	// try {
	// // equipEnroll
	// int targetType = equipEnroll.getTargetType();
	// String ip = equipEnroll.getIp();
	// if (targetType == 0) {
	// // 机架式服务器
	// synX86Rack(ip);
	// } else if (targetType == 1) {
	// // X86刀片机，刀片式服务器
	// synX86Host(ip);
	// } else if (targetType == 7) {
	// // 刀箱
	// synX86Enclosure(ip);
	// } else if (targetType == 8) {
	// // HP 小机
	// synHpHost(ip);
	// } else {
	// logger.error("Invalid target type . It is not supported.");
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// logger.error( "Failure enroll equipment [" + equipEnroll + "]",
	// e);
	// }
	// }
	// }

	@Override
	public List<TEquipmentenroll> findEquipmentEnroll() {
		return equipmentenrollDao.getAllAvailable();
	}

	// 同步X86刀片机资源
	@Override
	public void synX86Host(String ip) {
		// call plugin for get host info.
		List<Host> hostList = x86PhysicalResourceService.getAllHost(ip);
		if (hostList == null) {
			return;
		}

		for (Host host : hostList) {
			synHost(ip,
							host,
							THost.TYPE_X86_ENCLOSURE);
		}
	}

	// 同步X86机架式服务器资源
	@Override
	public void synX86Rack(String ip) {
		// call plugin for get
		Host host = x86PhysicalResourceService.getRackmount(ip);

		synHost(ip,
						host,
						THost.TYPE_X86_RACK);
	}

	// 同步X86刀箱资源
	@Override
	public void synX86Enclosure(String ip) {
		// call plugin for get
		Rack rack = x86PhysicalResourceService.getEnclosureInfo(ip);

		synRack(ip,
						rack);
	}

	// 同步HP小型机资源，HP小机是传的主机名
	@Override
	public void synHpHost(String hostName) {
		// call plugin for get
		Host host = hpPhysicalResourceService.getHost(hostName);
		// synHost(hostName,
		// host,
		// THost.TYPE_HP);
		synHost(hostName,
						host);
	}

	/**
	 * 
	 * synHost:同步HP小机信息. <br/>
	 * 只同步数据，不生成消息事件.<br/>
	 * 
	 * @param ip
	 * @param host
	 * 
	 */
	private void synHost(	String ip,
												Host host) {
		THost thost = null;
		
		Property p = null;

		try {
			if (host == null) {
				THost oldHost = thostDao.getTHostByIp(ip);
				if (oldHost != null) {
					thostDao.remove(oldHost);
				}
			} else {
				String serialNumber = host.getSerialNumber();
				if (serialNumber == null || serialNumber.trim().length() == 0) {
					logger.error("SerialNumber is null. Host : " + host);
					return;
				}
				THost oldHost = thostDao.getTHostBySeriralNumber(serialNumber);
				
				Property example = new Property();
				example.setSerial_num(serialNumber);
				List<Property> l = propertyDao.findPropertyListByExample(example, 10, 1);
				if(l != null && l.size() > 0)
					p = l.get(0);

				if (oldHost == null) {
					// 主机记录不存在，添加主机信息，产生新增消息事件
					thost = oldHost = new THost();
					thost.setSerialnumber(serialNumber);
					thost.setType(THost.TYPE_HP);
					thost.setStatus(THost.STATUS_ARCHIVE);
					thost.setSaveStatus(THost.SAVE_STATUS_DEAL);

					thost.setBayIndex(host.getBay_index());
					thost.setCpucores(host.getCpuCores());
					thost.setCpucount(host.getCpuCount());
					thost.setCputype(host.getCpuType());
					thost.setCreatedBy("admin");
					thost.setCreatedOn(new Date());
					thost.setDescript(host.getDescript());
					thost.setHostname(host.getHostName());
					thost.setManufacturer(host.getManufacturer());
					thost.setMemory(host.getMemory());
					thost.setModifiedBy("admin");
					thost.setModifiedOn(new Date());
					thost.setNic(host.getNic());
					thost.setProductname(host.getProductName());
//					thost.setResourcepoolid(host.getResorucepoolid());
					thost.setServername(host.getServerName());

					synHba(	host,
									thost);
				} else {
					thost = oldHost;
					thost.setSerialnumber(serialNumber);
					thost.setType(THost.TYPE_HP);
					thost.setStatus(THost.STATUS_ARCHIVE);
					thost.setSaveStatus(THost.SAVE_STATUS_DEAL);

					thost.setBayIndex(host.getBay_index());
					thost.setCpucores(host.getCpuCores());
					thost.setCpucount(host.getCpuCount());
					thost.setCputype(host.getCpuType());
					thost.setCreatedBy("admin");
					thost.setCreatedOn(new Date());
					thost.setDescript(host.getDescript());
					thost.setHostname(host.getHostName());
					thost.setManufacturer(host.getManufacturer());
					thost.setMemory(host.getMemory());
					thost.setModifiedBy("admin");
					thost.setModifiedOn(new Date());
					thost.setNic(host.getNic());
					thost.setProductname(host.getProductName());
					// thost.setResourcepoolid(host.getResorucepoolid());
					thost.setServername(host.getServerName());

					synHba(	host,
									thost);
				}
				
				// 更新资产信息
				if(p != null) {
					p.setVendor(thost.getManufacturer());
//					p.set
//					p.set
				}
			}

			if (thost != null) {
				// remove hba of thost
				hbaDao.removeHbaBySerialNumber(thost.getSerialnumber());

				thostDao.save(thost);
			}
			
			if(p != null) {
				propertyDao.save(p);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(	"Synchronize Host failured.",
										e);
		}
	}

	/**
	 * 
	 * synHost: 主机同步. <br/>
	 * 将接口返回的主机信息同数据库表中的主机信息比较，更新数据库表中的主机信息，生成消息事件.<br/>
	 * 支持三种主机信息，X86机架式服务器、X86刀片机、HP小机.<br/>
	 * 
	 * @param ip
	 * @param host
	 * @param hostType
	 * 
	 */
	private void synHost(	String ip,
												Host host,
												Integer hostType) {
		TEquipmentevent event = null;
		THost thost = null;
		
		Property p = null;

		try {
			if (host == null) {
				// 刀箱和主机同步结果没有数据时，而同步时的IP在t_host表中状态是“2：归档”或“3：废弃”时，产生一条“移除物理”的消息；
				// 如果是归档状态，更新为废弃，原是废弃状态的不作更新;
				THost oldHost = thostDao.getTHostByIp(ip);
				if (oldHost != null) {
					// if (oldHost.getStatus().intValue() == THost.STATUS_INIT) {
					if (oldHost.getSaveStatus().intValue() == THost.SAVE_STATUS_UNDEAL) {
						thostDao.remove(oldHost);
					} else {
						thost = oldHost;
						thost.setStatus(THost.STATUS_ERASE);
						thost.setSaveStatus(THost.SAVE_STATUS_UNDEAL);

						// HBA
						synHba(	host,
										thost);

						event = new TEquipmentevent();
						event.setSerialnumber(oldHost.getSerialnumber());
						event.setEventtype(TEquipmentevent.EVENT_TYPE_DELETE);
						event.setEventstatus(TEquipmentevent.EVENT_STATUS_PENDING);
						event.setCreatedBy("admin");
						event.setCreatedOn(new Date());
					}
				}
			} else {
				String serialNumber = host.getSerialNumber();
				if (serialNumber == null || serialNumber.trim().length() == 0) {
					logger.error("SerialNumber is null. Host : " + host);
					return;
				}
				THost oldHost = thostDao.getTHostBySeriralNumber(	serialNumber,
																													hostType);
				
				Property example = new Property();
				example.setSerial_num(serialNumber);
				List<Property> l = propertyDao.findPropertyListByExample(example, 10, 1);
				p = (l != null && l.size() > 0) ? l.get(0) : null;

				if (oldHost == null) {
					// 主机记录不存在，添加主机信息，产生新增消息事件
					thost = oldHost = new THost();
					thost.setSerialnumber(serialNumber);
					thost.setType(hostType);
					thost.setStatus(THost.STATUS_INIT);
					thost.setSaveStatus(THost.SAVE_STATUS_UNDEAL);

					thost.setBayIndex(host.getBay_index());
					thost.setCpucores(host.getCpuCores());
					thost.setCpucount(host.getCpuCount());
					thost.setCputype(host.getCpuType());
					thost.setCreatedBy("admin");
					thost.setCreatedOn(new Date());
					thost.setDescript(host.getDescript());
					thost.setHostname(host.getHostName());
					thost.setManufacturer(host.getManufacturer());
					thost.setMemory(host.getMemory());
					thost.setModifiedBy("admin");
					thost.setModifiedOn(new Date());
					thost.setNic(host.getNic());
					thost.setProductname(host.getProductName());
					// thost.setResourcepoolid(host.getResorucepoolid());
					thost.setServername(host.getServerName());

					synHba(	host,
									thost);

					event = new TEquipmentevent();
					event.setSerialnumber(serialNumber);
					event.setEventtype(TEquipmentevent.EVENT_TYPE_CREATE);
					event.setEventstatus(TEquipmentevent.EVENT_STATUS_PENDING);
					event.setCreatedBy("admin");
					event.setCreatedOn(new Date());
					// } else if (oldHost.getStatus() == THost.STATUS_ARCHIVE) {
				} else if (oldHost.getSaveStatus() == THost.SAVE_STATUS_DEAL) {
					// 已归档
					thost = oldHost;
					thost.setSerialnumber(serialNumber);
					thost.setType(hostType);
					//thost.setStatus(THost.STATUS_INIT);
					//thost.setSaveStatus(THost.SAVE_STATUS_UNDEAL);

					thost.setBayIndex(host.getBay_index());
					thost.setCpucores(host.getCpuCores());
					thost.setCpucount(host.getCpuCount());
					thost.setCputype(host.getCpuType());
					//thost.setCreatedBy("admin");
					//thost.setCreatedOn(new Date());
					thost.setDescript(host.getDescript());
					thost.setHostname(host.getHostName());
					thost.setManufacturer(host.getManufacturer());
					thost.setMemory(host.getMemory());
					thost.setModifiedBy("admin");
					thost.setModifiedOn(new Date());
					thost.setNic(host.getNic());
					thost.setProductname(host.getProductName());
					// thost.setResourcepoolid(host.getResorucepoolid());
					thost.setServername(host.getServerName());
					
					thost.setHbaList(null);

					synHba(	host,
									thost);
				} else {
					// 已有未归档的主机记录，不再添加主机信息，只产生新增消息事件
					event = new TEquipmentevent();
					event.setSerialnumber(serialNumber);
					event.setEventtype(TEquipmentevent.EVENT_TYPE_CREATE);
					event.setEventstatus(TEquipmentevent.EVENT_STATUS_PENDING);
					event.setCreatedBy("admin");
					event.setCreatedOn(new Date());
				}
				
				// 更新资产信息
				if(p != null) {
					p.setVendor(thost.getManufacturer());
				//	p.setModel(model)
//					p.set
//					p.set
				}
			}

			if (thost != null) {
				// remove hba of thost
				hbaDao.removeHbaBySerialNumber(thost.getSerialnumber());

				thostDao.save(thost);
			}
			
			if(p != null) {
				propertyDao.save(p);
			}

			if (event != null) {
				// 先删除未处理的消息事件
				equipmenteventDao.removeUnProcessed(event.getSerialnumber());

				equipmenteventDao.save(event);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(	"Synchronize Host failured.",
										e);
		}
	}

	private void synHba(Host host,
											THost thost) {
		List<THba> thbaList = new ArrayList<THba>();
		thost.setHbaList(thbaList);
		// create THba list
		List<HBA> hbaList = host.getHbas();

		if (hbaList == null || hbaList.size() == 0)
			return;

		for (HBA hba : hbaList) {
			THba h = new THba();
			h.setFkHost(thost);
			h.setType(hba.getType());
			h.setWwn(hba.getWwn());

			h.setCreatedBy(thost.getCreatedBy());
			h.setCreatedOn(thost.getCreatedOn());
			h.setModifiedBy(thost.getModifiedBy());
			h.setModifiedOn(thost.getModifiedOn());

			thbaList.add(h);
		}
	}

	/**
	 * 
	 * synRack:刀箱同步. <br/>
	 * .<br/>
	 * 
	 * @param ip
	 * @param rack
	 * 
	 */
	private void synRack(	String ip,
												Rack rack) {
		TEquipmentevent event = null;
		List<TEquipmentevent> eventList = new ArrayList<TEquipmentevent>();
		TRack track = null;
		List<TBayinfo> bayinfoList = null;
		
		Property p = null;

		try {
			// 4种操作，新增、移除、槽位变化、物理机状态变化
			// 一个卡槽对应一个刀片机
			if (rack == null) {
				// 刀箱和主机同步结果没有数据时，而同步时的IP在t_host表中状态是“2：归档”或“3：废弃”时，产生一条“移除物理”的消息；
				// 如果是归档状态，更新为废弃，原是废弃状态的不作更新;
				TRack oldRack = trackDao.getTRackByIp(ip);
				if (oldRack != null) {
					// if (oldRack.getStatus().intValue() == TRack.STATUS_INIT) {
					if (oldRack.getSaveStatus().intValue() == TRack.SAVE_STATUS_UNDEAL) {
						trackDao.remove(oldRack);
					} else {
						track = oldRack;
						track.setStatus(TRack.STATUS_ERASE);
						track.setSaveStatus(TRack.SAVE_STATUS_UNDEAL);

						event = new TEquipmentevent();
						event.setSerialnumber(oldRack.getSerialnumber());
						event.setEventtype(TEquipmentevent.EVENT_TYPE_DELETE);
						event.setEventstatus(TEquipmentevent.EVENT_STATUS_PENDING);
						event.setCreatedBy("admin");
						event.setCreatedOn(new Date());

						eventList.add(event);
					}
				}
			} else {
				String serialNumber = rack.getSerialNumber();
				if (serialNumber == null || serialNumber.trim().length() == 0) {
					logger.error("SerialNumber is null. Rack : " + rack);
					return;
				}
				TRack oldRack = trackDao.getTRackBySeriralNumber(serialNumber);
				
				Property example = new Property();
				example.setSerial_num(serialNumber);
				List<Property> l = propertyDao.findPropertyListByExample(example, 10, 1);
				p = (l != null && l.size() > 0) ? l.get(0) : null;

				if (oldRack == null) {
					// create
					track = oldRack = new TRack();

					track.setSerialnumber(serialNumber);
					track.setStatus(TRack.STATUS_INIT);
					track.setSaveStatus(TRack.SAVE_STATUS_UNDEAL);

					track.setBaycount((long) rack.getBayCount());
					track.setCreatedBy("admin");
					track.setCreatedOn(new Date());
					track.setModifiedBy("admin");
					track.setModifiedOn(new Date());
					track.setName(rack.getName());
					track.setOaip(rack.getIp());
					track.setUuid(rack.getUuid());
					track.setType(rack.getType());

					bayinfoList = new ArrayList<TBayinfo>();
					track.setBayinfos(bayinfoList);
					// 同步槽位信息
					synBayinfo(	rack,
											eventList,
											bayinfoList);

					event = new TEquipmentevent();
					event.setSerialnumber(serialNumber);
					event.setEventtype(TEquipmentevent.EVENT_TYPE_CREATE);
					event.setEventstatus(TEquipmentevent.EVENT_STATUS_PENDING);
					event.setCreatedBy("admin");
					event.setCreatedOn(new Date());

					eventList.add(event);
					// } else if (oldRack.getStatus() == TRack.STATUS_ARCHIVE) {
				} else if (oldRack.getSaveStatus() == TRack.SAVE_STATUS_DEAL) {
					// 已归档，检查槽位和物理机状态
					track = oldRack;
					track.setSerialnumber(serialNumber);
					// track.setStatus(THost.STATUS_INIT);
					track.setBaycount((long) rack.getBayCount());
					// track.setCreatedBy("admin");
					// track.setCreatedOn(new Date());
					track.setModifiedBy("admin");
					track.setModifiedOn(new Date());
					track.setName(rack.getName());
					track.setOaip(rack.getIp());
					track.setUuid(rack.getUuid());
					track.setType(rack.getType());

					// bayinfoList = tbayinfoDao.getAllByRackId(oldRack.getId());
					bayinfoList = track.getBayinfos();

					// 同步槽位信息
					synBayinfo(	rack,
											eventList,
											bayinfoList);
				} else {
					// 已有未归档的刀箱记录，不再添加刀箱信息，只产生新增消息事件
					event = new TEquipmentevent();
					event.setSerialnumber(serialNumber);
					event.setEventtype(TEquipmentevent.EVENT_TYPE_CREATE);
					event.setEventstatus(TEquipmentevent.EVENT_STATUS_PENDING);
					event.setCreatedBy("admin");
					event.setCreatedOn(new Date());

					eventList.add(event);
				}
				
				// 更新资产信息
				if(p != null) {
//					p.setVendor(track.get.getManufacturer());
//					p.set
//					p.set
				}
			}

			if (track != null) {
				if (bayinfoList != null) {
					for (TBayinfo tbayinfo : bayinfoList) {
						tbayinfo.setFkRack(track);
						// tbayinfoDao.save(tbayinfo);
					}
				}
				trackDao.save(track);
			}

			for (TEquipmentevent e : eventList) {
				// 先删除未处理的消息事件
				equipmenteventDao.removeUnProcessed(e.getSerialnumber());

				equipmenteventDao.save(e);
			}
			
			if(p != null) {
				propertyDao.save(p);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(	"Synchronize Rack failured.",
										e);
		}
	}

	/**
	 * 
	 * synBayinfo:槽位同步. <br/>
	 * 将接口返回的槽位信息同步到数据库中，数据库中没有则新增，同时生成消息事件.消息事件如下：<br/>
	 * 1、如果同步刀箱卡槽变化了(卡槽中对应的刀片机SN存在己归档，但是索引位置变化了，产生一个“变化位置”的消息，如果刀片机SN未归档或是废弃，则不处理，
	 * 不产生消息.<br/>
	 * 2、物理机状态变化，生成物理机状态变化消息，优先考虑槽位变化.<br/>
	 * 
	 * @param rack
	 * @param eventList
	 * @param oldBayinfos
	 * 
	 */
	private void synBayinfo(Rack rack,
													List<TEquipmentevent> eventList,
													List<TBayinfo> oldBayinfos) {
		Map<Integer, TBayinfo> indexMap = new HashMap<Integer, TBayinfo>();
		Map<String, TBayinfo> snumberMap = new HashMap<String, TBayinfo>();
		// 按槽位位置和槽位刀片机对系统中的现有槽位信息做映射，方便后面查询
		for (TBayinfo tbayinfo : oldBayinfos) {
			Integer index = tbayinfo.getBayindex();
			String snumber = tbayinfo.getSerialnum();
			indexMap.put(	index,
										tbayinfo);
			if (snumber != null && snumber.trim().length() > 0) {
				snumberMap.put(	snumber,
												tbayinfo);
			}
		}

		// 接口返回的槽位信息
		List<BayInfo> bayInfos = rack.getBayInfos();

		// 检查槽位变化和主机状态变化，生成消息
		generateEvent(bayInfos,
									snumberMap,
									eventList);

		// 更新数据库槽位信息
		updateTBayinfo(	bayInfos,
										oldBayinfos,
										indexMap);
	}

	/**
	 * 
	 * generateEvent:生成槽位变化和物理机状态变化消息事件. <br/>
	 * 
	 * @param eventList
	 * @param bayInfos
	 * @param snumberMap
	 * 
	 */
	private void generateEvent(	List<BayInfo> bayInfos,
															Map<String, TBayinfo> snumberMap,
															List<TEquipmentevent> eventList) {
		if (bayInfos == null)
			return;

		TEquipmentevent event;

		for (BayInfo bayInfo : bayInfos) {
			Integer index = bayInfo.getBayIndex();
			String snumber = bayInfo.getSerialNum();

			// 卡槽里没有刀片机，不做处理
			if (snumber == null || snumber.trim().length() == 0)
				continue;

			// 检查刀片机是否已归档
			THost thost = thostDao.getTHostBySeriralNumber(snumber);
			if (thost != null && (thost.getStatus() != THost.STATUS_ARCHIVE)) {
				// 如果刀片机SN未归档或是废弃，则不处理，不产生消息
				continue;
			}

			// 在现有槽位数据中查找刀片机对应的槽位信息
			TBayinfo tbayinfo = snumberMap.get(snumber);
			if (tbayinfo != null) {
				if (tbayinfo.getBayindex().intValue() != index.intValue()) {
					// 远程刀箱槽位接品返回数据与归档的刀箱槽位信息不一致

					event = new TEquipmentevent();
					event.setSerialnumber(snumber);
					event.setEventtype(TEquipmentevent.EVENT_TYPE_CHANGE_LOC);
					event.setEventstatus(TEquipmentevent.EVENT_STATUS_PENDING);
					event.setCreatedBy("admin");
					event.setCreatedOn(new Date());

					eventList.add(event);
				} else {
					// 槽位位置没有改变，检查主机状态是否改变
					String newStatus = bayInfo.getHostStatus();
					String oldStatus = tbayinfo.getHostStatus();
					newStatus = newStatus == null ? "" : newStatus.trim();
					oldStatus = oldStatus == null ? "" : oldStatus.trim();
					if (!newStatus.equals(oldStatus)) {
						// 远程刀箱槽位接口返回数据与归档的刀箱槽位信息一致,远程刀箱里刀片状态变化
						event = new TEquipmentevent();
						event.setSerialnumber(snumber);
						event.setEventtype(TEquipmentevent.EVENT_TYPE_CHANGE_STATUS);
						event.setEventstatus(TEquipmentevent.EVENT_STATUS_PENDING);
						event.setCreatedBy("admin");
						event.setCreatedOn(new Date());

						eventList.add(event);
					}
				}
			}
		}
	}

	/**
	 * 
	 * updateTBayinfo:更新槽位信息. <br/>
	 * 
	 * @param bayInfos
	 * @param oldBayinfos
	 * @param indexMap
	 * 
	 */
	private void updateTBayinfo(List<BayInfo> bayInfos,
															List<TBayinfo> oldBayinfos,
															Map<Integer, TBayinfo> indexMap) {
		if (bayInfos == null)
			return;
		
		 // 清空数据库中卡槽的刀片机序列号和其他信息，后面就不用反向循环检查需要删除的卡槽
		for (TBayinfo tBayinfo : oldBayinfos) {
			tBayinfo.setSerialnum(null);
			tBayinfo.setServername(null);
			tBayinfo.setStatus(null);
			tBayinfo.setHostStatus(null);
			tBayinfo.setPowerflag(null);
			tBayinfo.setIloip(null);
			tBayinfo.setIloname(null);
			tBayinfo.setModifiedBy("admin");
			tBayinfo.setModifiedOn(new Date());
		}

		for (BayInfo bayInfo : bayInfos) {
			Integer index = bayInfo.getBayIndex();
			TBayinfo tbayinfo = indexMap.get(index);

			if (tbayinfo == null) {
				// 槽位信息不存在，则新增
				tbayinfo = new TBayinfo();
				tbayinfo.setBayindex(bayInfo.getBayIndex());
				tbayinfo.setCreatedBy("admin");
				tbayinfo.setCreatedOn(new Date());

				// 添加到数据库现有槽位列表中
				oldBayinfos.add(tbayinfo);
			}

			// tbayinfo.setFkRackId(fkRackId);
			tbayinfo.setHostStatus(bayInfo.getHostStatus());
			tbayinfo.setIloip(bayInfo.getiLOIP());
			tbayinfo.setIloname(bayInfo.getiLOName());
			tbayinfo.setModifiedBy("admin");
			tbayinfo.setModifiedOn(new Date());
			tbayinfo.setPowerflag(bayInfo.getPowerFlag());
			tbayinfo.setSerialnum(bayInfo.getSerialNum());
			tbayinfo.setServername(bayInfo.getServerName());
			tbayinfo.setStatus(bayInfo.getStatus());
		}
		
		// 反向循环，检查数据库中哪些卡槽在本次同步中没有出现，设置serialnum为空或删除？或者如前先清空数据库中卡槽的刀片机信息。
	}
}
