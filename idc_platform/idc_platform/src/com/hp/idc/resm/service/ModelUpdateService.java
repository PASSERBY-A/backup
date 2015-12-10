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
 * 资源模型更新服务
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public class ModelUpdateService implements IModelUpdateService {
	/**
	 * log4j日志
	 */
	private static Logger logger = Logger.getLogger(ModelUpdateService.class);

	/**
	 * 数据表前缀
	 */
	public static final String TABLE_PREFIX = "RESM_ITEM_";

	/**
	 * 字段前缀
	 */
	public static final String FIELD_PREFIX = "FLD_";

	/**
	 * 返回模型对应在数据库中的表名
	 * 
	 * @param model
	 *            资源模型对象
	 * @return 数据库中的表名
	 */
	private String getTableName(Model model) {
		return (TABLE_PREFIX + model.getId()).toUpperCase();
	}

	/**
	 * 删除模型明细表
	 * 
	 * @param model
	 *            模型对象
	 * @throws Exception
	 *             数据库异常时发生
	 */
	public void dropTable(Model model) throws Exception {
		logger.info("删除数据表：" + getTableName(model) + "。");
		DbUtil.execute("drop table " + getTableName(model));
	}

	/**
	 * 创建资源模型表
	 * 
	 * @param model
	 *            资源模型对象
	 * @throws Exception
	 *             操作有异常时发生
	 */
	public void createTable(Model model) throws Exception {
		// 目录定义不创建数据表
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
				logger.info("数据表：" + getTableName(model) + "已存在，无需创建。");
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return;
		} finally {
			DbUtil.free(conn, stmt, rs);
		}

		logger.info("创建数据表：" + getTableName(model) + "。");
		String createSql = "create table " + getTableName(model) + " ("
				+ "id number," + "modelid varchar2(64)," + "flag number,"
				+ "synctime date)";
		DbUtil.execute(createSql);
		logger.info("创建数据表：" + getTableName(model) + " 成功。");
		List<ModelAttribute> list = model.getAttributes();
		
		for (int i = 0; i < list.size(); i++) {
			modifyTable(model, list.get(i), true, false, false);
		}
	}

	/**
	 * 修改明细表的表结构
	 * 
	 * @param model
	 *            资源模型对象
	 * @param attr
	 *            资源模型属性
	 * @param add
	 *            true=增加, false=删除
	 * @param includeChilds
	 *            连同子项一起
	 * @param childsOnly
	 *            只针对子项
	 * @throws Exception
	 *             操作有异常时发生
	 */
	private void modifyTable(Model model, ModelAttribute attr, boolean add,
			boolean includeChilds, boolean childsOnly) throws Exception {
		if (attr.getAttrId().equals("id"))
			return;
		List<Model> list = new ArrayList<Model>();
		// 增加需要变更的模型对象
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
			logger.info("修改" + m.getId() + "的结构：" + attr.getAttrId());
			// 生成变更的sql语句
			String sql = null;
			if (add) {
				String t = attr.getDatabaseField();
				if (t != null) {
					sql = "alter table " + getTableName(m) + " add "
							+ FIELD_PREFIX + attr.getAttrId().toUpperCase()
							+ " " + t + ";";
				} else {
					logger.info("字段" + m.getId() + "不能记录到明细表");
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

		// 判断模型是否存在
		Model oldModel = cms.getModelById(model.getId());
		if (oldModel != null) {
			RoleUtil.checkUserPermission(userId, "model_update", null, true);
			uniLog.setOperatorType(LogUtil.TYPE_UPDATE);
			uniLog.setContent("修改资源模型");

			// 不能通过update方法修改enabled字段
			if (!oldModel.isEnabled() && oldModel.isEnabled())
				throw new Exception("请通过removeModel方法禁用属性。");
			if (!oldModel.getParentId().equals(model.getParentId()))
				throw new Exception("属性的父类型不能修改。");
		} else {
			RoleUtil.checkUserPermission(userId, "model_new", null, true);
			uniLog.setOperatorType(LogUtil.TYPE_ADD);
			uniLog.setContent("添加资源模型");
		}

		// 建立临时的列表，在更新完成后，用来更新缓存
		List<ModelAttribute> removeList = new ArrayList<ModelAttribute>();
		List<ModelAttribute> updateList = new ArrayList<ModelAttribute>();

		// 更新数据库
		Connection conn = DbUtil.getConnection();
		try {
			conn.setAutoCommit(false);
			model.syncToDatabase(conn);
			//更新缓存
			cms.getCache().add(model);
			
			createTable(model);
			List<ModelAttribute> list = cms.getAttributeCache()
					.getAttributesById(model.getId());
			
			// 针对原有属性一一检查
			for (int i = 0; i < list.size(); i++) {
				ModelAttribute a = list.get(i);
				ModelAttribute b = null;
				// 查找是否在新的列表中
				for (int j = 0; j < attributes.size(); j++) {
					if (a.getAttrId().equals(attributes.get(j).getAttrId())) {
						b = attributes.remove(j);
						break;
					}
				}
				if (b == null) {
					// 不在新的列表中，需要删除
					logger.info(model.getId() + ",属性 " + a.getAttrId()
							+ " 已删除。");
					a.removeFromDatabase(conn);
					modifyTable(model, a, false, a.isInheritable(), false);
					removeList.add(a);

					uniLog = (Log) uniLog.clone();
					uniLog.setTypeOid(LogUtil.OP_MODEL_ATTRIBUTE);
					uniLog.setOperatorType(LogUtil.TYPE_REMOVE);
					uniLog.setContent("删除模型属性关联");
					uniLog.setExtendInfo(a.getLogExtendInfo());
					logList.add(uniLog);
				} else {
					// 需要更新的
					if (b.equals(a)) {
						logger.info(model.getId() + ",属性 " + b.getAttrId()
								+ " 无变化。");
					} else {
						logger.info(model.getId() + ",属性 " + b.getAttrId()
								+ " 需要更新。");
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
						uniLog.setContent("更新模型属性关联");
						uniLog.setExtendInfo(a.getLogExtendInfo());
						logList.add(uniLog);
					}
				}
			}
			for (int j = 0; j < attributes.size(); j++) {
				ModelAttribute a = attributes.get(j);
				// 列表中多余的需要新增
				if (model.getId().equals(a.getModelId())) {
					logger.info(model.getId() + ",属性 " + a.getAttrId()
							+ " 已新增。");
					a.syncToDatabase(conn);
					updateList.add(a);
					modifyTable(model, a, true, a.isInheritable(), false);

					uniLog = (Log) uniLog.clone();
					uniLog.setTypeOid(LogUtil.OP_MODEL_ATTRIBUTE);
					uniLog.setOperatorType(LogUtil.TYPE_ADD);
					uniLog.setContent("关联模型属性");
					uniLog.setExtendInfo(a.getLogExtendInfo());
					logList.add(uniLog);
				}
			}
			conn.commit();
		} catch (Exception e) {
			logger.error("修改模型" + model.getId() + "时出错。");
			try {
				cms.getCache().remove(model);
				conn.rollback();
			} catch (Exception e0) {
				// 此异常不做处理
			}
			e.printStackTrace();
			LogUtil.logError(logList);
			throw e;
		} finally {
			DbUtil.free(conn, null, null);
		}
		// 更新缓存
		ModelAttributeCache c = cms.getAttributeCache();
		for (ModelAttribute r : removeList)
			c.remove(r);
		for (ModelAttribute a : updateList)
			c.add(a);

		// 记录日志
		String userName = ResmUtil.getUserNameWithId(userId);
		String logText = userName + "修改模型成功:" + model.toString();
		logger.info(logText);
		LogUtil.log(logList);
	}

	public void removeModel(String modelId, int userId) throws Exception {
		RoleUtil.checkUserPermission(userId, "model_delete", null, true);

		// 判断模型是否存在
		CachedModelService cms = (CachedModelService) ServiceManager
				.getModelService();
		Model m = cms.getModelById(modelId);
		if (m == null)
			throw new Exception("模型" + modelId + "不存在。");

		// 检查模型下是否有对象
		List<ResourceObject> resList = ServiceManager.getResourceService()
				.getResourcesByModelId(m.getId(), Person.ADMIN_ID);
		if (resList.size() > 0) {
			throw new Exception("模型下已经建立了对象，不能删除");
		}

		// 检查模型下是否有子模型
		List<Model> childs = cms.getChildModelsById(m.getId(), true, Person.ADMIN_ID);
		for (Model c : childs) {
			if (c.isEnabled())
				throw new Exception("模型下有子模型，不能删除");
		}

		// 检查模型关联
		if (ServiceManager.getRelationService()
				.getModelRelationsByModelId(m.getId()).size() > 0) {
			throw new Exception("此模型与其他模型有关联关系，不能删除");
		}

		Log uniLog = new Log();
		uniLog.setTypeOid(LogUtil.OP_MODEL);
		uniLog.setBeginTime(System.currentTimeMillis());
		uniLog.setObject(m.getId());
		uniLog.setObjectName(m.getName());
		uniLog.setOperator(userId);
		uniLog.setOperatorType(LogUtil.TYPE_REMOVE);
		uniLog.setContent("删除资源模型");

		// 修改模型信息
		m = (Model) m.clone();
		m.setEnabled(false);

		// 提交数据库
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

		// 更新缓存
		cms.getCache().add(m);

		// 记录日志
		String userName = ResmUtil.getUserNameWithId(userId);
		String logText = userName + "删除模型成功:" + modelId;
		logger.info(logText);
		LogUtil.log(uniLog);
	}
}
