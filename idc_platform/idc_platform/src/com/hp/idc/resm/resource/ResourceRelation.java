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
 * ��Դ���������ϵ
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public class ResourceRelation extends CacheableObject {

	/**
	 * ���л�UID
	 */
	private static final long serialVersionUID = 8228257520761423959L;

	/**
	 * id
	 */
	private int id;

	/**
	 * ��Դ����id
	 */
	private int itemId;

	/**
	 * ��������Դ����id
	 */
	private int itemId2;

	/**
	 * ������ϵid
	 */
	private String relationId;
	
	/**
	 * ˵��
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
	 * ��ȡ��Ӧ��ģ�͹�ϵ����
	 * @return ��Ӧ��ģ�͹�ϵ���壬��ƥ��ʱ����null
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
	 * ��ȡid
	 * 
	 * @return id
	 * @see #id
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * ����id
	 * 
	 * @param id
	 *            id
	 * @throws CacheException
	 *             �����ѱ�����ʱ����
	 * @see #id
	 */
	public void setId(int id) throws CacheException {
		checkSet();
		this.id = id;
	}

	/**
	 * ��ȡ˵��
	 * 
	 * @return ˵��
	 * @see #remark
	 */
	public String getRemark() {
		return this.remark;
	}

	/**
	 * ����˵��
	 * 
	 * @param remark
	 *            ˵��
	 * @throws CacheException
	 *             �����ѱ�����ʱ����
	 * @see #remark
	 */
	public void setRemark(String remark) throws CacheException {
		checkSet();
		this.remark = remark;
	}
	
	/**
	 * ��ȡ��Դ����id
	 * 
	 * @return ��Դ����id
	 * @see #itemId
	 */
	public int getItemId() {
		return this.itemId;
	}

	/**
	 * ������Դ����id
	 * 
	 * @param itemId
	 *            ��Դ����id
	 * @throws CacheException
	 *             �����ѱ�����ʱ����
	 * @see #itemId
	 */
	public void setItemId(int itemId) throws CacheException {
		checkSet();
		this.itemId = itemId;
	}

	/**
	 * ��ȡ��������Դ����id
	 * 
	 * @return ��������Դ����id
	 * @see #itemId2
	 */
	public int getItemId2() {
		return this.itemId2;
	}

	/**
	 * ���ù�������Դ����id
	 * 
	 * @param itemId2
	 *            ��������Դ����id
	 * @throws CacheException
	 *             �����ѱ�����ʱ����
	 * @see #itemId2
	 */
	public void setItemId2(int itemId2) throws CacheException {
		checkSet();
		this.itemId2 = itemId2;
	}

	/**
	 * ��ȡ������ϵid
	 * 
	 * @return ������ϵid
	 * @see #relationId
	 */
	public String getRelationId() {
		return this.relationId;
	}

	/**
	 * ���ù�����ϵid
	 * 
	 * @param relationId
	 *            ������ϵid
	 * @throws CacheException
	 *             �����ѱ�����ʱ����
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
	 * ������Դ�����itemId
	 * 
	 * @author ÷԰
	 * 
	 */
	public static class ResourceRelationItemIdGetter implements
			ICompareKeyGetter<Integer, ResourceRelation> {

		public Integer getCompareKey(ResourceRelation obj) {
			return obj.getItemId();
		}

	}

	/**
	 * ������Դ�����itemId2
	 * 
	 * @author ÷԰
	 * 
	 */
	public static class ResourceRelationItemId2Getter implements
			ICompareKeyGetter<Integer, ResourceRelation> {

		public Integer getCompareKey(ResourceRelation obj) {
			return obj.getItemId2();
		}

	}
}
