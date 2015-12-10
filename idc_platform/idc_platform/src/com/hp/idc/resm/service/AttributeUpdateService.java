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
 * 资源对象更新服务
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public class AttributeUpdateService implements IAttributeUpdateService {
	/**
	 * log4j日志
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

		// TODO 权限判断

		// 判断属性是否存在
		AttributeDefine oldAttr = ServiceManager.getAttributeService()
				.getAttributeById(attr.getId());
		if (oldAttr != null) {
			RoleUtil.checkUserPermission(userId, "attribute_update", null, true);
			
			uniLog.setOperatorType(LogUtil.TYPE_UPDATE);
			uniLog.setContent("修改资源属性");
			// 判断属性是否已和模型关联
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

			// 不能通过update方法修改enabled字段
			if (!attr.isEnabled() && oldAttr.isEnabled())
				throw new Exception("请通过removeAttribute方法禁用属性。");
			if (used) {
				if (!attr.getType().equals(oldAttr.getType()))
					throw new Exception("与模型关联后，属性的类型不能修改。");
				if (attr.getLength() != oldAttr.getLength())
					throw new Exception("与模型关联后，属性的长度不能修改。");
			}
		} else {
			uniLog.setOperatorType(LogUtil.TYPE_ADD);
			uniLog.setContent("新增资源属性");
			RoleUtil.checkUserPermission(userId, "attribute_new", null, true);
		}

		// 更新数据库
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

		// 记录日志
		String userName = ResmUtil.getUserNameWithId(userId);
		String logText = userName + (oldAttr != null ? "更新" : "新增") + "资源属性："
				+ attr.toString();
		logger.info(logText);
		LogUtil.log(uniLog);
	}

	public void removeAttribute(String id, int userId) throws Exception {
		RoleUtil.checkUserPermission(userId, "attribute_delete", null, true);

		// 判断属性是否存在
		AttributeDefine attr = ServiceManager.getAttributeService()
				.getAttributeById(id);
		if (attr == null)
			throw new Exception("属性" + id + "不存在。");

		Log uniLog = new Log();
		uniLog.setTypeOid(LogUtil.OP_ATTRIBUTE);
		uniLog.setBeginTime(System.currentTimeMillis());
		uniLog.setObject(attr.getId());
		uniLog.setObjectName(attr.getName());
		uniLog.setOperator(userId);
		uniLog.setOperatorType(LogUtil.TYPE_REMOVE);
		uniLog.setContent("删除资源属性");

		
		// 判断属性是否已和模型关联
		CachedModelService cms = (CachedModelService) ServiceManager
				.getModelService();
		List<ModelAttribute> list = cms.getAttributeCache().getAll();
		for (ModelAttribute a : list) {
			if (a.getAttrId().equals(id)) {
				throw new Exception("属性" + id + "已和模型" + a.getModelId()
						+ "进行了关联，禁止删除。");
			}
		}

		// 修改属性信息
		attr = (AttributeDefine) attr.clone();
		attr.setEnabled(false);

		// 提交数据库
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

		// 更新缓存
		CachedAttributeService cas = (CachedAttributeService) ServiceManager
				.getAttributeService();
		cas.getCache().add(attr);

		// 记录日志 
		String userName = ResmUtil.getUserNameWithId(userId);
		String logText = userName + "禁用资源属性：" + attr.toString();
		logger.info(logText);
		LogUtil.log(uniLog);
	}
}
