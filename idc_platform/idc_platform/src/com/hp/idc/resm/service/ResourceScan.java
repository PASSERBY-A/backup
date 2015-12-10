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
 * ʹ��quartzɨ�����о��к�ͬ�������ڵ���Դ�����Ͷ��Ÿ����������ˡ�
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * @version 1.0, 10:11:24 PM Oct 30, 2011
 * 
 */

public class ResourceScan {

	public void checkContractResource() {
		Calendar c = Calendar.getInstance();
		System.out.println(c.getTime() + "��ʼ����ͬ��������");
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
				MessageManager.sendSms("���ύ�Ĺ�����������ţ�" + ti.getOid()
						+ "��������ռ��Դ����Դ��ţ�" + r.getId() + "����ͬ��������Ϊ��"
						+ r.getAttributeValue("contract_end")
						+ "���Ѳ���15�졣�뼰ʱ����", ti.getCreatedBy(), "resm",
						new Date(), "system");

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		System.out.println(new Date()+"����ͬ�������ڽ���");
				
		System.out.println(c.getTime() + "��ʼ���Ԥռ��Դ");
		List<ResourceObject> l1 = rc.findInGlobal("status", "Ԥռ");
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
					MessageManager.sendSms("���ύ�Ĺ�����������ţ�" + ti.getOid()
							+ "������Ԥռ��Դ����Դ��ţ�" + r.getId() + "����Ԥռʱ���Ѿ�����15�죬��Դ�����ͷš��뼰ʱ����", ti.getCreatedBy(), "resm",
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
					m.put("status", "����");
					ServiceManager.getResourceUpdateService().updateResource(r.getId(), 1, m);
					
					MessageManager.sendSms("���ύ�Ĺ�����������ţ�" + ti.getOid()
							+ "������Ԥռ��Դ����Դ��ţ�" + r.getId() + "����Ԥռʱ���Ѿ�����18�죬��Դ�ѱ��Զ��ͷš�", ti.getCreatedBy(), "resm",
							new Date(), "system");
				}
				
			} catch (ParseException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		System.out.println(new Date()+"���Ԥռ��Դ����");
	}
}
