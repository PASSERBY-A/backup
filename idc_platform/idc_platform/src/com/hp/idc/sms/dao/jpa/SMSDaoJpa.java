package com.hp.idc.sms.dao.jpa;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import com.hp.idc.common.core.dao.jpa.GenericDaoJpa;
import com.hp.idc.sms.dao.SMSDao;
import com.hp.idc.sms.entity.SMSMessageEntity;

@Repository("SMSDao")
public class SMSDaoJpa extends GenericDaoJpa<SMSMessageEntity, Long> implements
		SMSDao {

	public SMSDaoJpa() {
		super(SMSMessageEntity.class);
	}
	
	/**
	 * 查询需要的发送的短信
	 * 
	 * @return
	 */
	public List<SMSMessageEntity> querySMS4Send() {
		EntityManager em = getEntityManager();
		List<SMSMessageEntity> SMSList = new ArrayList<SMSMessageEntity>();
		try {
			String jSql = "select o from SMSMessageEntity o where o.msgSms = 1";
			SMSList = em.createQuery(jSql, SMSMessageEntity.class).getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SMSList;
	}

}
