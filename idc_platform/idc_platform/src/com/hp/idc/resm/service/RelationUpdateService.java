package com.hp.idc.resm.service;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.hp.idc.resm.cache.ResourceRelationCache;
import com.hp.idc.resm.model.ModelRelation;
import com.hp.idc.resm.resource.ResourceRelation;
import com.hp.idc.resm.util.DbUtil;
import com.hp.idc.resm.util.LogUtil;
import com.hp.idc.resm.util.RoleUtil;
import com.hp.idc.unitvelog.Log;

/**
 * ������ϵ���·���
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public class RelationUpdateService implements IRelationUpdateService {
	/**
	 * log4j��־
	 */
	private static Logger logger = Logger
			.getLogger(RelationUpdateService.class);

	public void updateResourceRelation(int id,
			List<ResourceRelation> relationList, int userId) throws Exception {
		// TODO Ȩ��

		// ����������
		Map<Integer, Integer> numMap = new HashMap<Integer, Integer>();
		for (ResourceRelation r : relationList) {
			if (r.getItemId() == r.getItemId2())
				throw new Exception("���ܺ��Լ�������ϵ��");
			if (r.getItemId() != id && r.getItemId2() != id)
				throw new Exception("�����������:id��Ϊ�����е�һ��");
			if (r.getItemId() <= 0 || r.getItemId2() < 0)
				throw new Exception("�����������:����id�Ƿ�");
			ModelRelation mr = r.getModelRelation();
			if (mr == null)
				throw new Exception("�Ƿ��Ĺ�����ϵ������ģ�ͼ�δ������ϵ");
			Integer i = numMap.get(mr.getId());
			if (i == null)
				i = 1;
			else
				i = i + 1;
			numMap.put(mr.getId(), i);
		}
		if (numMap.size() > 0) {
			Integer[] numKey = new Integer[numMap.size()];
			numKey = numMap.keySet().toArray(numKey);
			for (Integer key : numKey) {
				Integer num = numMap.get(key);
				ModelRelation mr = ServiceManager.getRelationService().getModelRelationById(key);
				if (mr.getNum() != -1 && mr.getNum() < num)
					throw new Exception("����������ϵ���Ʒ�Χ��" + mr.toString());
			}
		}
		logger.info("�û�" + userId + "������Դ������ϵ: " + id);

		List<Log> logList = new ArrayList<Log>();
		Log uniLog = new Log();
		uniLog.setTypeOid(LogUtil.OP_RELATION);
		uniLog.setBeginTime(System.currentTimeMillis());
		uniLog.setObjectName("��Դ�����ϵ");
		uniLog.setOperator(userId);
		uniLog.setObject("" + id);

		Connection conn = DbUtil.getConnection();

		CachedRelationService crs = (CachedRelationService) ServiceManager
				.getRelationService();

		List<ResourceRelation> removeList = new ArrayList<ResourceRelation>();
		try {
			conn.setAutoCommit(false);
			List<ResourceRelation> list = crs.getRelationsByResourceId(id);

			// ���ԭ�й�ϵһһ���
			for (int i = 0; i < list.size(); i++) {
				ResourceRelation a = list.get(i);
				ResourceRelation b = null;
				// �����Ƿ����µ��б���
				for (int j = 0; j < relationList.size(); j++) {
					if (a.equals(relationList.get(j))) {
						b = relationList.remove(j);
						break;
					}
				}
				if (b == null) {
					// �����µ��б��У���Ҫɾ��
					logger.info("��ɾ����ϵ:" + a.toString());
					a.removeFromDatabase(conn);
					removeList.add(a);

					uniLog = (Log) uniLog.clone();
					uniLog.setOperatorType(LogUtil.TYPE_REMOVE);
					uniLog.setContent("ɾ����Դ����");
					uniLog.setExtendInfo(a.getLogExtendInfo());
					logList.add(uniLog);
				}
			}
			for (ResourceRelation a : relationList) {
				// �б��ж������Ҫ����
				if (a.getItemId() == id || a.getItemId2() == id) {
					// Ϊ�����������µ�id
					int newId = DbUtil.getSequence(ResourceRelation.class
							.getName());
					a.setId(newId);
					logger.info("��ϵ������:" + a.toString());
					a.syncToDatabase(conn);

					uniLog = (Log) uniLog.clone();
					uniLog.setOperatorType(LogUtil.TYPE_REMOVE);
					uniLog.setContent("������Դ����");
					uniLog.setExtendInfo(a.getLogExtendInfo());
					logList.add(uniLog);
				}
			}
			conn.commit();
		} catch (Exception e) {
			logger.error("�޸���Դ" + id + "�Ĺ�����ϵʱ����");
			try {
				conn.rollback();
			} catch (Exception e0) {
				// ���쳣��������
			}
			e.printStackTrace();
			LogUtil.logError(logList);
			throw e;
		} finally {
			DbUtil.free(conn, null, null);
		}

		// ���»���
		ResourceRelationCache c = crs.getResourceRelationCache();
		for (ResourceRelation r : removeList)
			c.remove(r);
		for (ResourceRelation a : relationList) {
			// �б��ж������Ҫ����
			if (a.getItemId() == id || a.getItemId2() == id) {
				c.add(a);
			}
		}
		logger.info("�޸���Դ" + id + "�Ĺ�����ϵ�ɹ���");
		LogUtil.log(logList);
	}

	public void addResourceRelation(ResourceRelation relation, int userId)
			throws Exception {
		List<ResourceRelation> list = ServiceManager.getRelationService()
				.getRelationsByResourceId(relation.getItemId());
		for (ResourceRelation r : list) {
			if (r.equals(relation)) { // ��������
				return;
			}
		}
		list.add(relation);
		updateResourceRelation(relation.getItemId(), list, userId);
	}

	public void addModelRelation(ModelRelation relation, int userId)
			throws Exception {
		RoleUtil.checkUserPermission(userId, "model_relation_new", null, true);

		// ����id
		int id = DbUtil.getSequence(ModelRelation.class.getName());
		relation.setId(id);

		Log uniLog = new Log();
		uniLog.setTypeOid(LogUtil.OP_RELATION);
		uniLog.setBeginTime(System.currentTimeMillis());
		uniLog.setObject("" + relation.getId());
		uniLog.setObjectName("ģ�͹�ϵ");
		uniLog.setOperator(userId);
		uniLog.setOperatorType(LogUtil.TYPE_ADD);
		uniLog.setContent("����ģ�͹���");
		uniLog.setExtendInfo(relation.getLogExtendInfo());

		logger.info("�û�" + userId + "��ģ�͹�����" + relation.toString());
		Connection conn = DbUtil.getConnection();
		try {
			conn.setAutoCommit(false);
			relation.syncToDatabase(conn);
			conn.commit();
		} catch (Exception e) {
			e.printStackTrace();
			LogUtil.logError(uniLog);
			throw e;
		} finally {
			DbUtil.free(conn, null, null);
		}
		CachedRelationService crs = (CachedRelationService) ServiceManager
				.getRelationService();
		crs.getModelRelationCache().add(relation);

		LogUtil.log(uniLog);
	}

	public void removeModelRelation(int id, int userId) throws Exception {
		RoleUtil.checkUserPermission(userId, "model_relation_remove", null, true);

		CachedRelationService crs = (CachedRelationService) ServiceManager
				.getRelationService();
		ModelRelation mr = crs.getModelRelationById(id);
		if (mr == null)
			return;

		Log uniLog = new Log();
		uniLog.setTypeOid(LogUtil.OP_RELATION);
		uniLog.setBeginTime(System.currentTimeMillis());
		uniLog.setObject("" + mr.getId());
		uniLog.setObjectName("ģ�͹�ϵ");
		uniLog.setOperator(userId);
		uniLog.setOperatorType(LogUtil.TYPE_REMOVE);
		uniLog.setContent("ɾ��ģ�͹���");
		uniLog.setExtendInfo(mr.getLogExtendInfo());

		List<ResourceRelation> list = ((CachedRelationService)ServiceManager.getRelationService()).getResourceRelationCache().getAll();
		for (ResourceRelation rr :list) {
			ModelRelation mr0 = rr.getModelRelation();
			if (mr0 != null && mr0.getId() == id)
				throw new Exception("������Դ����(id=" + rr.getItemId() + ")ʹ�������������ϵ������ɾ����");
		}

		logger.info("�û�" + userId + "ɾ��ģ�͹�����" + mr.toString());
		Connection conn = DbUtil.getConnection();
		try {
			conn.setAutoCommit(false);
			mr.removeFromDatabase(conn);
			conn.commit();
		} catch (Exception e) {
			e.printStackTrace();
			LogUtil.logError(uniLog);
			throw e;
		} finally {
			DbUtil.free(conn, null, null);
		}
		crs.getModelRelationCache().remove(mr);

		LogUtil.log(uniLog);
	}

}
