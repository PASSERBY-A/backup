package com.hp.idc.resm.model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.hp.idc.resm.cache.CacheException;
import com.hp.idc.resm.cache.CacheableObject;
import com.hp.idc.resm.util.ICompareKeyGetter;

/**
 * 资源代码
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public class Code extends CacheableObject {

	/**
	 * 序列化UID
	 */
	private static final long serialVersionUID = -4778198918104489610L;

	/**
	 * 主键
	 */
	private int oid;

	/**
	 * 代码ID
	 */
	private String id;

	/**
	 * 代码名称
	 */
	private String name;

	/**
	 * 代码说明
	 */
	private String remark;

	/**
	 * 父代码oid
	 */
	private int parentOid;

	/**
	 * 排序
	 */
	private int order;

	/**
	 * 子代码
	 */
	private List<Code> childs = null;

	/**
	 * 获取排序
	 * 
	 * @return 排序
	 * @see #order
	 */
	public int getOrder() {
		return this.order;
	}

	/**
	 * 设置排序
	 * 
	 * @param order
	 *            排序
	 * @throws CacheException
	 *             对象已被缓存时发生
	 * @see #order
	 */
	public void setOrder(int order) throws CacheException {
		checkSet();
		this.order = order;
	}

	/**
	 * 获取oid
	 * 
	 * @return oid oid
	 * @see #oid
	 */
	public int getOid() {
		return this.oid;
	}

	/**
	 * 设置oid
	 * 
	 * @param oid
	 *            oid
	 * @throws CacheException
	 *             对象已被缓存时发生
	 * @see #oid
	 */
	public void setOid(int oid) throws CacheException {
		checkSet();
		this.oid = oid;
	}

	/**
	 * 获取父oid
	 * 
	 * @return parentOid 父oid
	 * @see #parentOid
	 */
	public int getParentOid() {
		return this.parentOid;
	}

	/**
	 * 设置父oid
	 * 
	 * @param parentOid
	 *            父oid
	 * @throws CacheException
	 *             对象已被缓存时发生
	 * @see #parentOid
	 */
	public void setParentOid(int parentOid) throws CacheException {
		checkSet();
		this.parentOid = parentOid;
	}

	/**
	 * 获取代码id
	 * 
	 * @return 代码id
	 * @see #id
	 */
	public String getId() {
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
	public void setId(String id) throws CacheException {
		checkSet();
		this.id = id;
	}

	/**
	 * 获取代码名称
	 * 
	 * @return name 代码名称
	 * @see #name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * 设置代码名称
	 * 
	 * @param name
	 *            代码名称
	 * @throws CacheException
	 *             对象已被缓存时发生
	 * @see #name
	 */
	public void setName(String name) throws CacheException {
		checkSet();
		this.name = name;
	}

	/**
	 * 获取代码说明
	 * 
	 * @return remark 代码说明
	 * @see #remark
	 */
	public String getRemark() {
		return this.remark;
	}

	/**
	 * 设置代码名称
	 * 
	 * @param remark
	 *            代码说明
	 * @throws CacheException
	 *             对象已被缓存时发生
	 * @see #remark
	 */
	public void setRemark(String remark) throws CacheException {
		checkSet();
		this.remark = remark;
	}

	/**
	 * 获取所有子代码
	 * 
	 * @return 所有子代码
	 * @see #childs
	 */
	public List<Code> getChilds() {
		return this.childs;
	}

	/**
	 * 设置子代码
	 * 
	 * @param childs
	 *            子代码
	 * @see #childs
	 */
	public void setChilds(List<Code> childs) {
		// NOTHING TO DO
	}

	/**
	 * 添加子代码
	 * 
	 * @param c
	 *            代码对象
	 */
	public void addChild(Code c) {
		if (this.childs == null)
			this.childs = new ArrayList<Code>();
		this.childs.add(c);
	}

	@Override
	public String getPrimaryKey() {
		return "" + this.oid;
	}

	@Override
	public String getDatabaseTable() {
		return "resm_code";
	}

	@Override
	public String getPrimaryKeyField() {
		return "oid";
	}

	@Override
	public void updateResultSet(Connection conn, ResultSet rs) {
		// 不作更新
	}

	@Override
	public void readFromResultSet(ResultSet rs) throws Exception {
		this.id = rs.getString("id");
		this.name = rs.getString("name");
		this.remark = rs.getString("remark");
		this.oid = rs.getInt("oid");
		this.parentOid = rs.getInt("parentoid");
		this.order = rs.getInt("itemorder");
	}

	/**
	 * 生成代码的id
	 * 
	 * @author 梅园
	 * 
	 */
	public static class CodeIdGetter implements ICompareKeyGetter<String, Code> {

		public String getCompareKey(Code obj) {
			return obj.getId();
		}
	}
}
