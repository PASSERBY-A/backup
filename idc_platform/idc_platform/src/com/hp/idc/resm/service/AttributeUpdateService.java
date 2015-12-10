package com.hp.idc.resm.service;

import java.sql.Connection;
import java.util.List;

import org.apache.log4j.Logger;

import com.hp.idc.resm.model.AttributeDefine;
import com.hp.idc.resm.model.ModelAttribute;
import com.hp.idc.resm.util.DbUtil;
import com.hp.idc.resm.util.LogUtil;
import com.hp.idc.resm.util.ResmUtil;
import com.hp.idc.resm.util.RoleUtil;
import com.hp.idc.unitvelog.Log;

/**
 * ��Դ������·���
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public class AttributeUpdateService implements IAttributeUpdateService {
	/**
	 * log4j��־
	 */
	private static Logger logger = Logger
			.getLogger(AttributeUpdateService.class);

	public void updateAttribute(AttributeDefine attr, int userId)
			throws Exception {
		Log uniLog = new Log();
		uniLog.setTypeOid(LogUtil.OP_ATTRIBUTE);
		uniLog.setBeginTime(System.currentTimeMillis());
		uniLog.setObject(attr.getId());
		uniLog.setObjectName(attr.getName());
		uniLog.setOperator(userId);
		uniLog.setExtendInfo(attr.getLogExtendInfo());

		// TODO Ȩ���ж�

		// �ж������Ƿ����
		AttributeDefine oldAttr = ServiceManager.getAttributeService()
				.getAttributeById(attr.getId());
		if (oldAttr != null) {
			RoleUtil.checkUserPermission(userId, "attribute_update", null, true);
			
			uniLog.setOperatorType(LogUtil.TYPE_UPDATE);
			uniLog.setContent("�޸���Դ����");
			// �ж������Ƿ��Ѻ�ģ�͹���
			boolean used = false;
			CachedModelService cms = (CachedModelService) ServiceManager
					.getModelService();
			List<ModelAttribute> list = cms.getAttributeCache().getAll();
			for (ModelAttribute a : list) {
				if (a.getAttrId().equals(oldAttr.getId())) {
					used = true;
					break;
				}
			}

			// ����ͨ��update�����޸�enabled�ֶ�
			if (!attr.isEnabled() && oldAttr.isEnabled())
				throw new Exception("��ͨ��removeAttribute�����������ԡ�");
			if (used) {
				if (!attr.getType().equals(oldAttr.getType()))
					throw new Exception("��ģ�͹��������Ե����Ͳ����޸ġ�");
				if (attr.getLength() != oldAttr.getLength())
					throw new Exception("��ģ�͹��������Եĳ��Ȳ����޸ġ�");
			}
		} else {
			uniLog.setOperatorType(LogUtil.TYPE_ADD);
			uniLog.setContent("������Դ����");
			RoleUtil.checkUserPermission(userId, "attribute_new", null, true);
		}

		// �������ݿ�
		Connection conn = DbUtil.getConnection();
		try {
			conn.setAutoCommit(false);
			attr.syncToDatabase(conn);
			conn.commit();
		} catch (Exception e) {
			e.printStackTrace();
			LogUtil.logError(uniLog);
			throw e;
		} finally {
			DbUtil.free(conn, null, null);
		}
		CachedAttributeService cas = (CachedAttributeService) ServiceManager
				.getAttributeService();
		cas.getCache().add(attr);

		// ��¼��־
		String userName = ResmUtil.getUserNameWithId(userId);
		String logText = userName + (oldAttr != null ? "����" : "����") + "��Դ���ԣ�"
				+ attr.toString();
		logger.info(logText);
		LogUtil.log(uniLog);
	}

	public void removeAttribute(String id, int userId) throws Exception {
		RoleUtil.checkUserPermission(userId, "attribute_delete", null, true);

		// �ж������Ƿ����
		AttributeDefine attr = ServiceManager.getAttributeService()
				.getAttributeById(id);
		if (attr == null)
			throw new Exception("����" + id + "�����ڡ�");

		Log uniLog = new Log();
		uniLog.setTypeOid(LogUtil.OP_ATTRIBUTE);
		uniLog.setBeginTime(System.currentTimeMillis());
		uniLog.setObject(attr.getId());
		uniLog.setObjectName(attr.getName());
		uniLog.setOperator(userId);
		uniLog.setOperatorType(LogUtil.TYPE_REMOVE);
		uniLog.setContent("ɾ����Դ����");

		
		// �ж������Ƿ��Ѻ�ģ�͹���
		CachedModelService cms = (CachedModelService) ServiceManager
				.getModelService();
		List<ModelAttribute> list = cms.getAttributeCache().getAll();
		for (ModelAttribute a : list) {
			if (a.getAttrId().equals(id)) {
				throw new Exception("����" + id + "�Ѻ�ģ��" + a.getModelId()
						+ "�����˹�������ֹɾ����");
			}
		}

		// �޸�������Ϣ
		attr = (AttributeDefine) attr.clone();
		attr.setEnabled(false);

		// �ύ���ݿ�
		Connection conn = DbUtil.getConnection();
		try {
			conn.setAutoCommit(false);
			attr.syncToDatabase(conn);
			conn.commit();
		} catch (Exception e) {
			e.printStackTrace();
			LogUtil.logError(uniLog);
			throw e;
		} finally {
			DbUtil.free(conn, null, null);
		}

		// ���»���
		CachedAttributeService cas = (CachedAttributeService) ServiceManager
				.getAttributeService();
		cas.getCache().add(attr);

		// ��¼��־ 
		String userName = ResmUtil.getUserNameWithId(userId);
		String logText = userName + "������Դ���ԣ�" + attr.toString();
		logger.info(logText);
		LogUtil.log(uniLog);
	}
}
