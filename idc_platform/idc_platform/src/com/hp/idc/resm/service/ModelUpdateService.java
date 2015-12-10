package com.hp.idc.resm.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.hp.idc.resm.cache.ModelAttributeCache;
import com.hp.idc.resm.model.Model;
import com.hp.idc.resm.model.ModelAttribute;
import com.hp.idc.resm.resource.ResourceObject;
import com.hp.idc.resm.security.Person;
import com.hp.idc.resm.util.DbUtil;
import com.hp.idc.resm.util.LogUtil;
import com.hp.idc.resm.util.ResmUtil;
import com.hp.idc.resm.util.RoleUtil;
import com.hp.idc.unitvelog.Log;

/**
 * ��Դģ�͸��·���
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public class ModelUpdateService implements IModelUpdateService {
	/**
	 * log4j��־
	 */
	private static Logger logger = Logger.getLogger(ModelUpdateService.class);

	/**
	 * ���ݱ�ǰ׺
	 */
	public static final String TABLE_PREFIX = "RESM_ITEM_";

	/**
	 * �ֶ�ǰ׺
	 */
	public static final String FIELD_PREFIX = "FLD_";

	/**
	 * ����ģ�Ͷ�Ӧ�����ݿ��еı���
	 * 
	 * @param model
	 *            ��Դģ�Ͷ���
	 * @return ���ݿ��еı���
	 */
	private String getTableName(Model model) {
		return (TABLE_PREFIX + model.getId()).toUpperCase();
	}

	/**
	 * ɾ��ģ����ϸ��
	 * 
	 * @param model
	 *            ģ�Ͷ���
	 * @throws Exception
	 *             ���ݿ��쳣ʱ����
	 */
	public void dropTable(Model model) throws Exception {
		logger.info("ɾ�����ݱ�" + getTableName(model) + "��");
		DbUtil.execute("drop table " + getTableName(model));
	}

	/**
	 * ������Դģ�ͱ�
	 * 
	 * @param model
	 *            ��Դģ�Ͷ���
	 * @throws Exception
	 *             �������쳣ʱ����
	 */
	public void createTable(Model model) throws Exception {
		// Ŀ¼���岻�������ݱ�
		if (model.isDirectoryOnly())
			return;

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			conn = DbUtil.getConnection();
			stmt = conn
					.prepareStatement("select * from tabs where table_name=upper(?)");
			stmt.setString(1, getTableName(model));
			rs = stmt.executeQuery();
			if (rs.next()) {
				logger.info("���ݱ�" + getTableName(model) + "�Ѵ��ڣ����贴����");
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return;
		} finally {
			DbUtil.free(conn, stmt, rs);
		}

		logger.info("�������ݱ�" + getTableName(model) + "��");
		String createSql = "create table " + getTableName(model) + " ("
				+ "id number," + "modelid varchar2(64)," + "flag number,"
				+ "synctime date)";
		DbUtil.execute(createSql);
		logger.info("�������ݱ�" + getTableName(model) + " �ɹ���");
		List<ModelAttribute> list = model.getAttributes();
		
		for (int i = 0; i < list.size(); i++) {
			modifyTable(model, list.get(i), true, false, false);
		}
	}

	/**
	 * �޸���ϸ��ı�ṹ
	 * 
	 * @param model
	 *            ��Դģ�Ͷ���
	 * @param attr
	 *            ��Դģ������
	 * @param add
	 *            true=����, false=ɾ��
	 * @param includeChilds
	 *            ��ͬ����һ��
	 * @param childsOnly
	 *            ֻ�������
	 * @throws Exception
	 *             �������쳣ʱ����
	 */
	private void modifyTable(Model model, ModelAttribute attr, boolean add,
			boolean includeChilds, boolean childsOnly) throws Exception {
		if (attr.getAttrId().equals("id"))
			return;
		List<Model> list = new ArrayList<Model>();
		// ������Ҫ�����ģ�Ͷ���
		if (!model.isDirectoryOnly() && !childsOnly)
			list.add(model);
		if (includeChilds) {
			List<Model> list2 = ServiceManager.getModelService()
					.getChildModelsById(model.getId(), Person.ADMIN_ID);
			for (int i = 0; i < list2.size(); i++) {
				if (!list2.get(i).isDirectoryOnly())
					list.add(list2.get(i));
			}
		}

		for (int i = 0; i < list.size(); i++) {
			Model m = list.get(i);
			logger.info("�޸�" + m.getId() + "�Ľṹ��" + attr.getAttrId());
			// ���ɱ����sql���
			String sql = null;
			if (add) {
				String t = attr.getDatabaseField();
				if (t != null) {
					sql = "alter table " + getTableName(m) + " add "
							+ FIELD_PREFIX + attr.getAttrId().toUpperCase()
							+ " " + t + ";";
				} else {
					logger.info("�ֶ�" + m.getId() + "���ܼ�¼����ϸ��");
				}
			} else
				sql = "alter table " + getTableName(m) + " drop column "
						+ FIELD_PREFIX + attr.getAttrId().toUpperCase() + ";";
			if (sql != null) {
				logger.info(sql);
				DbUtil.execute(sql);
			}
		}
	}

	public void updateModel(Model model, List<ModelAttribute> attributes,
			int userId) throws Exception {
		
		CachedModelService cms = (CachedModelService) ServiceManager
				.getModelService();
		List<Log> logList = new ArrayList<Log>();
		Log uniLog = new Log();
		uniLog.setTypeOid(LogUtil.OP_MODEL);
		uniLog.setBeginTime(System.currentTimeMillis());
		uniLog.setObject(model.getId());
		uniLog.setObjectName(model.getName());
		uniLog.setOperator(userId);
		uniLog.setExtendInfo(model.getLogExtendInfo());
		logList.add(uniLog);

		// �ж�ģ���Ƿ����
		Model oldModel = cms.getModelById(model.getId());
		if (oldModel != null) {
			RoleUtil.checkUserPermission(userId, "model_update", null, true);
			uniLog.setOperatorType(LogUtil.TYPE_UPDATE);
			uniLog.setContent("�޸���Դģ��");

			// ����ͨ��update�����޸�enabled�ֶ�
			if (!oldModel.isEnabled() && oldModel.isEnabled())
				throw new Exception("��ͨ��removeModel�����������ԡ�");
			if (!oldModel.getParentId().equals(model.getParentId()))
				throw new Exception("���Եĸ����Ͳ����޸ġ�");
		} else {
			RoleUtil.checkUserPermission(userId, "model_new", null, true);
			uniLog.setOperatorType(LogUtil.TYPE_ADD);
			uniLog.setContent("�����Դģ��");
		}

		// ������ʱ���б��ڸ�����ɺ��������»���
		List<ModelAttribute> removeList = new ArrayList<ModelAttribute>();
		List<ModelAttribute> updateList = new ArrayList<ModelAttribute>();

		// �������ݿ�
		Connection conn = DbUtil.getConnection();
		try {
			conn.setAutoCommit(false);
			model.syncToDatabase(conn);
			//���»���
			cms.getCache().add(model);
			
			createTable(model);
			List<ModelAttribute> list = cms.getAttributeCache()
					.getAttributesById(model.getId());
			
			// ���ԭ������һһ���
			for (int i = 0; i < list.size(); i++) {
				ModelAttribute a = list.get(i);
				ModelAttribute b = null;
				// �����Ƿ����µ��б���
				for (int j = 0; j < attributes.size(); j++) {
					if (a.getAttrId().equals(attributes.get(j).getAttrId())) {
						b = attributes.remove(j);
						break;
					}
				}
				if (b == null) {
					// �����µ��б��У���Ҫɾ��
					logger.info(model.getId() + ",���� " + a.getAttrId()
							+ " ��ɾ����");
					a.removeFromDatabase(conn);
					modifyTable(model, a, false, a.isInheritable(), false);
					removeList.add(a);

					uniLog = (Log) uniLog.clone();
					uniLog.setTypeOid(LogUtil.OP_MODEL_ATTRIBUTE);
					uniLog.setOperatorType(LogUtil.TYPE_REMOVE);
					uniLog.setContent("ɾ��ģ�����Թ���");
					uniLog.setExtendInfo(a.getLogExtendInfo());
					logList.add(uniLog);
				} else {
					// ��Ҫ���µ�
					if (b.equals(a)) {
						logger.info(model.getId() + ",���� " + b.getAttrId()
								+ " �ޱ仯��");
					} else {
						logger.info(model.getId() + ",���� " + b.getAttrId()
								+ " ��Ҫ���¡�");
						b.syncToDatabase(conn);
						updateList.add(b);
						if (b.isInheritable() != a.isInheritable()) {
							if (b.isInheritable())
								modifyTable(model, a, true, true, true);
							else
								modifyTable(model, a, false, true, true);
						}

						uniLog = (Log) uniLog.clone();
						uniLog.setTypeOid(LogUtil.OP_MODEL_ATTRIBUTE);
						uniLog.setOperatorType(LogUtil.TYPE_UPDATE);
						uniLog.setContent("����ģ�����Թ���");
						uniLog.setExtendInfo(a.getLogExtendInfo());
						logList.add(uniLog);
					}
				}
			}
			for (int j = 0; j < attributes.size(); j++) {
				ModelAttribute a = attributes.get(j);
				// �б��ж������Ҫ����
				if (model.getId().equals(a.getModelId())) {
					logger.info(model.getId() + ",���� " + a.getAttrId()
							+ " ��������");
					a.syncToDatabase(conn);
					updateList.add(a);
					modifyTable(model, a, true, a.isInheritable(), false);

					uniLog = (Log) uniLog.clone();
					uniLog.setTypeOid(LogUtil.OP_MODEL_ATTRIBUTE);
					uniLog.setOperatorType(LogUtil.TYPE_ADD);
					uniLog.setContent("����ģ������");
					uniLog.setExtendInfo(a.getLogExtendInfo());
					logList.add(uniLog);
				}
			}
			conn.commit();
		} catch (Exception e) {
			logger.error("�޸�ģ��" + model.getId() + "ʱ����");
			try {
				cms.getCache().remove(model);
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
		ModelAttributeCache c = cms.getAttributeCache();
		for (ModelAttribute r : removeList)
			c.remove(r);
		for (ModelAttribute a : updateList)
			c.add(a);

		// ��¼��־
		String userName = ResmUtil.getUserNameWithId(userId);
		String logText = userName + "�޸�ģ�ͳɹ�:" + model.toString();
		logger.info(logText);
		LogUtil.log(logList);
	}

	public void removeModel(String modelId, int userId) throws Exception {
		RoleUtil.checkUserPermission(userId, "model_delete", null, true);

		// �ж�ģ���Ƿ����
		CachedModelService cms = (CachedModelService) ServiceManager
				.getModelService();
		Model m = cms.getModelById(modelId);
		if (m == null)
			throw new Exception("ģ��" + modelId + "�����ڡ�");

		// ���ģ�����Ƿ��ж���
		List<ResourceObject> resList = ServiceManager.getResourceService()
				.getResourcesByModelId(m.getId(), Person.ADMIN_ID);
		if (resList.size() > 0) {
			throw new Exception("ģ�����Ѿ������˶��󣬲���ɾ��");
		}

		// ���ģ�����Ƿ�����ģ��
		List<Model> childs = cms.getChildModelsById(m.getId(), true, Person.ADMIN_ID);
		for (Model c : childs) {
			if (c.isEnabled())
				throw new Exception("ģ��������ģ�ͣ�����ɾ��");
		}

		// ���ģ�͹���
		if (ServiceManager.getRelationService()
				.getModelRelationsByModelId(m.getId()).size() > 0) {
			throw new Exception("��ģ��������ģ���й�����ϵ������ɾ��");
		}

		Log uniLog = new Log();
		uniLog.setTypeOid(LogUtil.OP_MODEL);
		uniLog.setBeginTime(System.currentTimeMillis());
		uniLog.setObject(m.getId());
		uniLog.setObjectName(m.getName());
		uniLog.setOperator(userId);
		uniLog.setOperatorType(LogUtil.TYPE_REMOVE);
		uniLog.setContent("ɾ����Դģ��");

		// �޸�ģ����Ϣ
		m = (Model) m.clone();
		m.setEnabled(false);

		// �ύ���ݿ�
		Connection conn = DbUtil.getConnection();
		try {
			conn.setAutoCommit(false);
			m.syncToDatabase(conn);
			conn.commit();
		} catch (Exception e) {
			e.printStackTrace();
			LogUtil.logError(uniLog);
			throw e;
		} finally {
			DbUtil.free(conn, null, null);
		}

		// ���»���
		cms.getCache().add(m);

		// ��¼��־
		String userName = ResmUtil.getUserNameWithId(userId);
		String logText = userName + "ɾ��ģ�ͳɹ�:" + modelId;
		logger.info(logText);
		LogUtil.log(uniLog);
	}
}
