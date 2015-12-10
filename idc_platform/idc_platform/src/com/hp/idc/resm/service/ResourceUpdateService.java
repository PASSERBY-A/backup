package com.hp.idc.resm.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.hp.idc.resm.model.AttributeDefine;
import com.hp.idc.resm.model.Model;
import com.hp.idc.resm.model.ModelAttribute;
import com.hp.idc.resm.resource.AttributeBase;
import com.hp.idc.resm.resource.ResourceObject;
import com.hp.idc.resm.util.DbUtil;
import com.hp.idc.resm.util.LogUtil;
import com.hp.idc.resm.util.StringUtil;
import com.hp.idc.unitvelog.Log;

/**
 * 资源对象更新服务
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public class ResourceUpdateService implements IResourceUpdateService {
	/**
	 * log4j日志
	 */
	private static Logger logger = Logger
			.getLogger(ResourceUpdateService.class);

	/**
	 * 更新资源对象到数据库、缓存，并发出同步通知
	 * 
	 * @param ro
	 *            资源对象
	 * @throws Exception
	 *             异常时发生
	 */
	private void updateResource(ResourceObject ro) throws Exception {
		Model model = ro.getModel();
		if (model.isDirectoryOnly()) {
			throw new Exception("模型“" + model.getName() + "”下不允许新建资源对象。");
		}
		Connection conn = DbUtil.getConnection();
		try {
			conn.setAutoCommit(false);
			ro.syncToDatabase(conn);
			conn.commit();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			DbUtil.free(conn, null, null);
		}
		CachedResourceService crs = (CachedResourceService) ServiceManager
				.getResourceService();
		crs.getCache().add(ro);

		// 添加同步事件
		ServiceManager.getMessageListener()
				.publishObjectSyncMessage(ro.getId());
	}

	public void updateResource(ResourceObject ro, int userId) throws Exception {
		// TODO 权限

		Model m = ro.getModel();
		if (m == null || !m.isInstance(ro)) {
			throw new Exception("非法的模型");
		}
		int id = ro.getId();
		if (id == -1) {
			ro.setId(DbUtil.getSequence(ResourceObject.class.getName()));
		}
		Log uniLog = new Log();
		uniLog.setTypeOid(LogUtil.OP_RESOURCE);
		uniLog.setBeginTime(System.currentTimeMillis());
		uniLog.setObject("" + ro.getId());
		uniLog.setObjectName(ro.getName());
		uniLog.setOperator(userId);
		if (id == -1) {
			uniLog.setOperatorType(LogUtil.TYPE_ADD);
			uniLog.setContent("新增资源对象");
		} else {
			uniLog.setOperatorType(LogUtil.TYPE_UPDATE);
			uniLog.setContent("更新资源对象");
		}
		try {
			updateResource(ro);
		} catch (Exception e) {
			e.printStackTrace();
			LogUtil.logError(uniLog);
			throw e;
		}
		LogUtil.log(uniLog);
	}

	/**
	 * 更新数据集
	 * 
	 * @param obj
	 *            资源对象
	 * @param conn
	 *            数据库连接
	 * @param rs
	 *            数据集
	 * @throws Exception
	 *             操作出现异常时发生
	 */
	private void updateResultSet(ResourceObject obj, Connection conn,
			ResultSet rs) throws Exception {
		rs.updateInt("id", obj.getId());
		rs.updateInt("flag", 0);
		rs.updateString("modelid", obj.getModelId());
		rs.updateTimestamp("synctime",
				new Timestamp(System.currentTimeMillis()));
		Model model = ServiceManager.getModelService().getModelById(
				obj.getModelId());
		List<ModelAttribute> list = model.getAttributes();
		logger.info("添加表: "+(ModelUpdateService.TABLE_PREFIX + model.getId()).toUpperCase()+"明细");
		for (int i = 0; i < list.size(); i++) {
			ModelAttribute ma = list.get(i);
			if ("id".equals(ma.getAttrId()))
				continue;
			String t = ma.getDatabaseField();
			if (t != null) {
				String _id = ModelUpdateService.FIELD_PREFIX + ma.getAttrId();
				if (t.startsWith("varchar2(")) {
					String _value = obj.getAttributeValue(ma.getAttrId());
					logger.info(_id + "=======>" + _value);
					rs.updateString(_id, _value);
				} else if(t.startsWith("number")){
					int _value = StringUtil.parseInt(obj.getAttributeValue(ma.getAttrId()), 0);
					logger.info(_id + "=======>" + _value);
					rs.updateInt(_id, _value);
				}
				else {
					throw new Exception("系统错误，需要修改代码: " + t);
				}
			}
		}
		logger.info("明细添加结束!");
	}

	public void syncResource(int id) {
		logger.debug("同步资源 id = " + id);
		ResourceObject obj = ServiceManager.getResourceService()
				.getResourceById(id);
		if (obj == null)
			return;
		Connection conn;
		try {
			conn = DbUtil.getConnection();
		} catch (Exception e1) {
			e1.printStackTrace();
			return;
		}
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.prepareStatement("select t.* from "
					+ ModelUpdateService.TABLE_PREFIX + obj.getModelId()
					+ " t where " + "id=?", ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_UPDATABLE);
			stmt.setString(1, obj.getPrimaryKey());
			rs = stmt.executeQuery();
			if (rs.next()) {
				if(obj.isEnabled()){
					updateResultSet(obj, conn, rs);
					rs.updateRow();
				} else {
					rs.deleteRow();
				}
			} else {
				rs.moveToInsertRow();
				updateResultSet(obj, conn, rs);
				rs.insertRow();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DbUtil.free(conn, stmt, rs);
		}
	}

	public void deleteResource(int id, int userId) throws Exception {
		// TODO 权限

		ResourceObject ro = ServiceManager.getResourceService()
				.getResourceById(id);
		if (ro == null)
			return;

		Log uniLog = new Log();
		uniLog.setTypeOid(LogUtil.OP_RESOURCE);
		uniLog.setBeginTime(System.currentTimeMillis());
		uniLog.setObject("" + ro.getId());
		uniLog.setObjectName(ro.getName());
		uniLog.setOperator(userId);
		uniLog.setOperatorType(LogUtil.TYPE_REMOVE);
		
		if(ro.isEnabled()){
			ro = (ResourceObject) ro.clone();
			ro.setEnabled(false);
			uniLog.setContent("删除资源对象");
			try {
				updateResource(ro);
			} catch (Exception e) {
				e.printStackTrace();
				LogUtil.logError(uniLog);
				throw e;
			}
		} else {
			uniLog.setContent("彻底删除资源对象");
			Connection conn = DbUtil.getConnection();
			try {
				conn.setAutoCommit(false);
				ro.removeFromDatabase(conn);
				conn.commit();
				
				CachedResourceService crs = (CachedResourceService) ServiceManager
				.getResourceService();
				crs.getCache().remove(ro);
				
			} catch (Exception e) {
				e.printStackTrace();
				LogUtil.logError(uniLog);
				throw e;
			}
		}
		LogUtil.log(uniLog);
	}

	public void updateResource(int id, List<AttributeBase> list, int userId)
			throws Exception {
		ResourceObject res = ServiceManager.getResourceService()
				.getResourceById(id);
		if (res == null)
			throw new Exception("找不到资源");

		res = (ResourceObject) res.clone();
		if (!res.update(list, userId) && res.isEnabled()) // 无需更新
			return;
		//恢复被删除资源
		if(!res.isEnabled())
			res.setEnabled(true);

		updateResource(res, userId);
	}

	public int addResource(String modelId, List<AttributeBase> list, int userId)
			throws Exception {
		// TODO 权限
		
		Model m = ServiceManager.getModelService().getModelById(modelId);
		if (m == null)
			throw new Exception("参数不正确");

		ResourceObject res = m.createObject();
		int newId = DbUtil.getSequence(ResourceObject.class.getName());
		res.setId(newId);
		res.setEnabled(true);
		res.setData("<ResourceObject/>");
		res.update(list, userId);
		
		updateResource(res, userId);
		return newId;
	}

	public int addResource(String modelId, Map<String, String> attributes,
			int userId) throws Exception {
		Model model = ServiceManager.getModelService().getModelById(modelId);
		if (model == null)
			throw new Exception("参数不正确");
		List<AttributeBase> list = new ArrayList<AttributeBase>();
		for (String key : attributes.keySet()) {
			String val = attributes.get(key);
			AttributeDefine ad = ServiceManager.getAttributeService()
					.getAttributeById(key);
			if (ad == null) {
				throw new Exception("资源创建失败：" + key + "不是有效的资源属性。");
			}
			AttributeBase ab = ad.createInstance(modelId);
			if (ab == null)
				throw new Exception("资源创建失败：" + key + "不是模型" + modelId + "的属性。");
			// TODO 各个类型的Attribute的setText方法逻辑需要完善
			ab.setText(val); 
			list.add(ab);
		}
		return addResource(modelId, list, userId);
	}
	
	public void updateResource(int id, int userId, Map<String, String> attributes) throws Exception{
		ResourceObject res = ServiceManager.getResourceService().getResourceById(id);
		if (res == null) {
			throw new Exception("编号为:"+id+"资源不存在");
		}
		List<AttributeBase> _l = new ArrayList<AttributeBase>();
		for(Map.Entry<String, String> entry : attributes.entrySet()){
			AttributeDefine ad = ServiceManager.getAttributeService().getAttributeById(entry.getKey());
			if(ad == null){
				throw new Exception("资源更改失败：" + entry.getKey() + "不是有效的资源属性。");
			}
			AttributeBase ab = ad.createInstance(res.getModelId());
			if (ab == null) {
				throw new Exception("资源更改失败：" + entry.getKey() + "不是模型" + res.getModelId() + "的属性。");
			}
			ab.setText(entry.getValue());
			_l.add(ab);
		}
		updateResource(id, _l, userId);
	}
}
