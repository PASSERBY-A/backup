package com.hp.idc.sms.dao;

import java.util.List;

import com.hp.idc.common.core.dao.GenericDao;
import com.hp.idc.sms.entity.SMSMessageEntity;

public interface SMSDao extends GenericDao<SMSMessageEntity, Long> {
	public List<SMSMessageEntity> querySMS4Send();
}
