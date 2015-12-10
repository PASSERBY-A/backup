/**
 * 
 */
package com.hp.idc.resm.resource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hp.idc.resm.cache.CacheException;
import com.hp.idc.resm.cache.CacheableObject;
import com.hp.idc.resm.model.ModelRelation;
import com.hp.idc.resm.service.ServiceManager;
import com.hp.idc.resm.util.ICompareKeyGetter;

/**
 * 资源对象关联关系
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public class ResourceRelation extends CacheableObject {

	/**
	 * 序列化UID
	 */
	private static final long serialVersionUID = 8228257520761423959L;

	/**
	 * id
	 */
	private int id;

	/**
	 * 资源对象id
	 */
	private int itemId;

	/**
	 * 关联的资源对象id
	 */
	private int itemId2;

	/**
	 * 关联关系id
	 */
	private String relationId;
	
	/**
	 * 说明
	 */
	private String remark;

	@Override
	public Map<String, String> getLogExtendInfo() {
		Map<String, String> m = new HashMap<String, String>();
		m.put("objectid1", "" + this.itemId);
		m.put("id", "" + this.id);
		m.put("objectid2", "" + this.itemId2);
		m.put("relationid", this.relationId);
		return m;
	}
	
	/**
	 * 获取对应的模型关系定义
	 * @return 对应的模型关系定义，无匹配时返回null
	 */
	public ModelRelation getModelRelation() {
		ResourceObject obj1 = ServiceManager.getResourceService().getResourceById(this.itemId);
		ResourceObject obj2 = ServiceManager.getResourceService().getResourceById(this.itemId2);
		if (obj1 == null || obj2 == null)
			return null;
		List<ModelRelation> list = ServiceManager.getRelationService().getAllModelRelations();
		for (ModelRelation mr : list) {
			if (mr.getRelationId().equals(this.relationId)
					&& obj1.getModel().isChildOf(mr.getModelId())
					&& obj2.getModel().isChildOf(mr.getModelId2()))
				return mr;
		}
		return null;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof ResourceRelation))
			return false;
		ResourceRelation r = (ResourceRelation) obj;
		if (r.itemId == this.itemId && r.itemId2 == this.itemId2
				&& r.relationId.equals(this.relationId))
			return true;
		return false;
	}

	@Override
	public String toString() {
		return "id=" + this.id + ",itemId=" + this.itemId + ",relationId="
				+ this.relationId + ",itemId2=" + this.itemId2;
	}

	/**
	 * 获取id
	 * 
	 * @return id
	 * @see #id
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * 设置id
	 * 
	 * @param id
	 *            id
	 * @throws CacheException
	 *             对象已被缓存时发生
	 * @see #id
	 */
	public void setId(int id) throws CacheException {
		checkSet();
		this.id = id;
	}

	/**
	 * 获取说明
	 * 
	 * @return 说明
	 * @see #remark
	 */
	public String getRemark() {
		return this.remark;
	}

	/**
	 * 设置说明
	 * 
	 * @param remark
	 *            说明
	 * @throws CacheException
	 *             对象已被缓存时发生
	 * @see #remark
	 */
	public void setRemark(String remark) throws CacheException {
		checkSet();
		this.remark = remark;
	}
	
	/**
	 * 获取资源对象id
	 * 
	 * @return 资源对象id
	 * @see #itemId
	 */
	public int getItemId() {
		return this.itemId;
	}

	/**
	 * 设置资源对象id
	 * 
	 * @param itemId
	 *            资源对象id
	 * @throws CacheException
	 *             对象已被缓存时发生
	 * @see #itemId
	 */
	public void setItemId(int itemId) throws CacheException {
		checkSet();
		this.itemId = itemId;
	}

	/**
	 * 获取关联的资源对象id
	 * 
	 * @return 关联的资源对象id
	 * @see #itemId2
	 */
	public int getItemId2() {
		return this.itemId2;
	}

	/**
	 * 设置关联的资源对象id
	 * 
	 * @param itemId2
	 *            关联的资源对象id
	 * @throws CacheException
	 *             对象已被缓存时发生
	 * @see #itemId2
	 */
	public void setItemId2(int itemId2) throws CacheException {
		checkSet();
		this.itemId2 = itemId2;
	}

	/**
	 * 获取关联关系id
	 * 
	 * @return 关联关系id
	 * @see #relationId
	 */
	public String getRelationId() {
		return this.relationId;
	}

	/**
	 * 设置关联关系id
	 * 
	 * @param relationId
	 *            关联关系id
	 * @throws CacheException
	 *             对象已被缓存时发生
	 * @see #relationId
	 */
	public void setRelationId(String relationId) throws CacheException {
		checkSet();
		this.relationId = relationId;
	}

	@Override
	public String getPrimaryKey() {
		return "" + this.id;
	}

	@Override
	public String getDatabaseTable() {
		return "resm_relation";
	}

	@Override
	public String getPrimaryKeyField() {
		return "id";
	}

	@Override
	public void updateResultSet(Connection conn, ResultSet rs) throws Exception {
		rs.updateInt("id", this.id);
		rs.updateInt("itemid", this.itemId);
		rs.updateInt("itemid2", this.itemId2);
		rs.updateString("relationid", this.relationId);
		rs.updateString("remark", this.remark);
	}

	@Override
	public void readFromResultSet(ResultSet rs) throws Exception {
		setId(rs.getInt("id"));
		setItemId(rs.getInt("itemid"));
		setRelationId(rs.getString("relationid"));
		setItemId2(rs.getInt("itemid2"));
		setRemark(rs.getString("remark"));
	}

	/**
	 * 生成资源对象的itemId
	 * 
	 * @author 梅园
	 * 
	 */
	public static class ResourceRelationItemIdGetter implements
			ICompareKeyGetter<Integer, ResourceRelation> {

		public Integer getCompareKey(ResourceRelation obj) {
			return obj.getItemId();
		}

	}

	/**
	 * 生成资源对象的itemId2
	 * 
	 * @author 梅园
	 * 
	 */
	public static class ResourceRelationItemId2Getter implements
			ICompareKeyGetter<Integer, ResourceRelation> {

		public Integer getCompareKey(ResourceRelation obj) {
			return obj.getItemId2();
		}

	}
}
