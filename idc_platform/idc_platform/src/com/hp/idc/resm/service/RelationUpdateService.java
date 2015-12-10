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
 * 关联关系更新服务
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public class RelationUpdateService implements IRelationUpdateService {
	/**
	 * log4j日志
	 */
	private static Logger logger = Logger
			.getLogger(RelationUpdateService.class);

	public void updateResourceRelation(int id,
			List<ResourceRelation> relationList, int userId) throws Exception {
		// TODO 权限

		// 输入参数检查
		Map<Integer, Integer> numMap = new HashMap<Integer, Integer>();
		for (ResourceRelation r : relationList) {
			if (r.getItemId() == r.getItemId2())
				throw new Exception("不能和自己建立关系。");
			if (r.getItemId() != id && r.getItemId2() != id)
				throw new Exception("输入参数错误:id不为关联中的一个");
			if (r.getItemId() <= 0 || r.getItemId2() < 0)
				throw new Exception("输入参数错误:关联id非法");
			ModelRelation mr = r.getModelRelation();
			if (mr == null)
				throw new Exception("非法的关联关系：所属模型间未建立关系");
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
					throw new Exception("超出关联关系限制范围：" + mr.toString());
			}
		}
		logger.info("用户" + userId + "更新资源关联关系: " + id);

		List<Log> logList = new ArrayList<Log>();
		Log uniLog = new Log();
		uniLog.setTypeOid(LogUtil.OP_RELATION);
		uniLog.setBeginTime(System.currentTimeMillis());
		uniLog.setObjectName("资源对象关系");
		uniLog.setOperator(userId);
		uniLog.setObject("" + id);

		Connection conn = DbUtil.getConnection();

		CachedRelationService crs = (CachedRelationService) ServiceManager
				.getRelationService();

		List<ResourceRelation> removeList = new ArrayList<ResourceRelation>();
		try {
			conn.setAutoCommit(false);
			List<ResourceRelation> list = crs.getRelationsByResourceId(id);

			// 针对原有关系一一检查
			for (int i = 0; i < list.size(); i++) {
				ResourceRelation a = list.get(i);
				ResourceRelation b = null;
				// 查找是否在新的列表中
				for (int j = 0; j < relationList.size(); j++) {
					if (a.equals(relationList.get(j))) {
						b = relationList.remove(j);
						break;
					}
				}
				if (b == null) {
					// 不在新的列表中，需要删除
					logger.info("已删除关系:" + a.toString());
					a.removeFromDatabase(conn);
					removeList.add(a);

					uniLog = (Log) uniLog.clone();
					uniLog.setOperatorType(LogUtil.TYPE_REMOVE);
					uniLog.setContent("删除资源关联");
					uniLog.setExtendInfo(a.getLogExtendInfo());
					logList.add(uniLog);
				}
			}
			for (ResourceRelation a : relationList) {
				// 列表中多余的需要新增
				if (a.getItemId() == id || a.getItemId2() == id) {
					// 为新增项生成新的id
					int newId = DbUtil.getSequence(ResourceRelation.class
							.getName());
					a.setId(newId);
					logger.info("关系已新增:" + a.toString());
					a.syncToDatabase(conn);

					uniLog = (Log) uniLog.clone();
					uniLog.setOperatorType(LogUtil.TYPE_REMOVE);
					uniLog.setContent("新增资源关联");
					uniLog.setExtendInfo(a.getLogExtendInfo());
					logList.add(uniLog);
				}
			}
			conn.commit();
		} catch (Exception e) {
			logger.error("修改资源" + id + "的关联关系时出错。");
			try {
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
		ResourceRelationCache c = crs.getResourceRelationCache();
		for (ResourceRelation r : removeList)
			c.remove(r);
		for (ResourceRelation a : relationList) {
			// 列表中多余的需要新增
			if (a.getItemId() == id || a.getItemId2() == id) {
				c.add(a);
			}
		}
		logger.info("修改资源" + id + "的关联关系成功。");
		LogUtil.log(logList);
	}

	public void addResourceRelation(ResourceRelation relation, int userId)
			throws Exception {
		List<ResourceRelation> list = ServiceManager.getRelationService()
				.getRelationsByResourceId(relation.getItemId());
		for (ResourceRelation r : list) {
			if (r.equals(relation)) { // 无需新增
				return;
			}
		}
		list.add(relation);
		updateResourceRelation(relation.getItemId(), list, userId);
	}

	public void addModelRelation(ModelRelation relation, int userId)
			throws Exception {
		RoleUtil.checkUserPermission(userId, "model_relation_new", null, true);

		// 生成id
		int id = DbUtil.getSequence(ModelRelation.class.getName());
		relation.setId(id);

		Log uniLog = new Log();
		uniLog.setTypeOid(LogUtil.OP_RELATION);
		uniLog.setBeginTime(System.currentTimeMillis());
		uniLog.setObject("" + relation.getId());
		uniLog.setObjectName("模型关系");
		uniLog.setOperator(userId);
		uniLog.setOperatorType(LogUtil.TYPE_ADD);
		uniLog.setContent("新增模型关联");
		uniLog.setExtendInfo(relation.getLogExtendInfo());

		logger.info("用户" + userId + "新模型关联：" + relation.toString());
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
		uniLog.setObjectName("模型关系");
		uniLog.setOperator(userId);
		uniLog.setOperatorType(LogUtil.TYPE_REMOVE);
		uniLog.setContent("删除模型关联");
		uniLog.setExtendInfo(mr.getLogExtendInfo());

		List<ResourceRelation> list = ((CachedRelationService)ServiceManager.getRelationService()).getResourceRelationCache().getAll();
		for (ResourceRelation rr :list) {
			ModelRelation mr0 = rr.getModelRelation();
			if (mr0 != null && mr0.getId() == id)
				throw new Exception("已有资源对象(id=" + rr.getItemId() + ")使用了这个关联关系，不能删除。");
		}

		logger.info("用户" + userId + "删除模型关联：" + mr.toString());
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
