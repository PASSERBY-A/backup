/*
 * @(#)ContractScan.java
 *
 * Copyright (c) 2011, hp and/or its affiliates. All rights reserved.
 * HP PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.hp.idc.resm.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hp.idc.itsm.message.MessageManager;
import com.hp.idc.itsm.task.TaskInfo;
import com.hp.idc.itsm.task.TaskManager;
import com.hp.idc.resm.cache.ResourceObjectCache;
import com.hp.idc.resm.resource.DateAttribute;
import com.hp.idc.resm.resource.DateTimeAttribute;
import com.hp.idc.resm.resource.ResourceObject;
import com.hp.idc.resm.util.DateTimeUtil;
import com.hp.idc.resm.util.StringUtil;

/**
 * 使用quartz扫描所有具有合同结束日期的资源，发送短信给工单创建人。
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * @version 1.0, 10:11:24 PM Oct 30, 2011
 * 
 */

public class ResourceScan {

	public void checkContractResource() {
		Calendar c = Calendar.getInstance();
		System.out.println(c.getTime() + "开始检查合同结束日期");
		CachedResourceService rs = (CachedResourceService) ServiceManager
				.getResourceService();
		ResourceObjectCache rc = rs.getCache();
		SimpleDateFormat sdf = new SimpleDateFormat(DateAttribute.DATE_FORMAT);
		c.add(Calendar.DATE, 15);
		String end = sdf.format(c.getTime());
		List<ResourceObject> l = rc.findInGlobal("contract_end", end);
		for (ResourceObject r : l) {
			String task = r.getAttributeValue("task_link");
			if (task == null || task.equals("-1") || task.trim().length() == 0) {
				continue;
			}
			String[] _task = task.split(":");
			if (_task.length != 3)
				continue;
			try {
				TaskInfo ti = TaskManager.getTaskInfoByOid("ITSM", StringUtil
						.parseInt(_task[0], -1), StringUtil.parseInt(_task[1],
						-1), StringUtil.parseInt(_task[2], -1));
				if(ti == null)
					continue;
				MessageManager.sendSms("您提交的工单【工单编号：" + ti.getOid()
						+ "】，其所占资源【资源编号：" + r.getId() + "】合同结束日期为："
						+ r.getAttributeValue("contract_end")
						+ "，已不足15天。请及时处理。", ti.getCreatedBy(), "resm",
						new Date(), "system");

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		System.out.println(new Date()+"检查合同结束日期结束");
				
		System.out.println(c.getTime() + "开始检查预占资源");
		List<ResourceObject> l1 = rc.findInGlobal("status", "预占");
		for(ResourceObject r : l1){
			String lastUpdate = r.getAttributeValue("last_update_time");
			String task = r.getAttributeValue("task_link");
			if (task == null || task.equals("-1") || task.trim().length() == 0) {
				continue;
			}
			String[] _task = task.split(":");
			if (_task.length != 3)
				continue;
			try {
				Date d = DateTimeUtil.parseDate(lastUpdate, DateTimeAttribute.DATE_FORMAT);
				if(d.getTime() + 15*24*60*60*1000 > System.currentTimeMillis()){
					TaskInfo ti = TaskManager.getTaskInfoByOid("ITSM", StringUtil
							.parseInt(_task[0], -1), StringUtil.parseInt(_task[1],
							-1), StringUtil.parseInt(_task[2], -1));
					if(ti == null)
						continue;
					MessageManager.sendSms("您提交的工单【工单编号：" + ti.getOid()
							+ "】，所预占资源【资源编号：" + r.getId() + "】，预占时间已经超过15天，资源即将释放。请及时处理。", ti.getCreatedBy(), "resm",
							new Date(), "system");
					continue;
				}
				if(d.getTime() + 18*24*60*60*1000 > System.currentTimeMillis()){
					TaskInfo ti = TaskManager.getTaskInfoByOid("ITSM", StringUtil
							.parseInt(_task[0], -1), StringUtil.parseInt(_task[1],
							-1), StringUtil.parseInt(_task[2], -1));
					if(ti == null)
						continue;
					Map<String, String> m = new HashMap<String, String>();
					m.put("task_link", "-1");		
					m.put("customer_id", "-1");
					m.put("status", "空闲");
					ServiceManager.getResourceUpdateService().updateResource(r.getId(), 1, m);
					
					MessageManager.sendSms("您提交的工单【工单编号：" + ti.getOid()
							+ "】，所预占资源【资源编号：" + r.getId() + "】，预占时间已经超过18天，资源已被自动释放。", ti.getCreatedBy(), "resm",
							new Date(), "system");
				}
				
			} catch (ParseException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		System.out.println(new Date()+"检查预占资源结束");
	}
}
