package com.hp.idc.itsm.ci;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 表示关联关系类型 相关数据表 itsm_ci_relation_type
 * 
 * @author 梅园
 * 
 */
public class RelationTypeInfo {
	/**
	 * 存储关联关系类型的 oid
	 */
	protected int oid;

	/**
	 * 存储关联关系类型的名称
	 */
	protected String name;

	/**
	 * 存储与其它对象进行关联时的描述信息
	 */
	protected String caption;

	/**
	 * 存储关联关系类型的描述
	 */
	protected String desc;

	/**
	 * 存储关联关系类型的逆向关系类型的 oid
	 */
	protected int reverseOid;

	/**
	 * 存储关联对象A的类型
	 */
	protected int typeA;

	/**
	 * 存储关联对象B的类型
	 */
	protected int typeB;

	/**
	 * 存储关联关系的标志
	 */
	protected String flag;

	/**
	 * 表示逆向的关联关系类型
	 */
	protected RelationTypeInfo reverse = null;

	/**
	 * 对数据库返回的结果集的当前记录进行分析
	 * 
	 * @param rs
	 *            结果集
	 * @throws SQLException
	 *             数据库操作异常时引发
	 */
	protected void parse(ResultSet rs) throws SQLException {
		setOid(rs.getInt("rt_oid"));
		setName(rs.getString("rt_name"));
		setCaption(rs.getString("rt_caption"));
		setDesc(rs.getString("rt_desc"));
		setReverseOid(rs.getInt("rt_rev_oid"));
		setTypeA(rs.getInt("rt_type_a"));
		setTypeB(rs.getInt("rt_type_b"));
		setFlag(rs.getString("rt_flag"));
		// 不能和自己建立逆向关系
		if (this.reverseOid == this.oid)
			this.reverseOid = -1;
	}

	/**
	 * 获取关联对象A的类型
	 * 
	 * @return 返回关联对象A的类型
	 */
	public int getTypeA() {
		return this.typeA;
	}

	/**
	 * 设置关联对象A的类型
	 * 
	 * @param typeA
	 *            关联对象A的类型
	 */
	public void setTypeA(int typeA) {
		this.typeA = (typeA == 0) ? -1 : typeA;
	}

	/**
	 * 获取关联对象B的类型
	 * 
	 * @return 返回关联对象B的类型
	 */
	public int getTypeB() {
		return this.typeB;
	}

	/**
	 * 设置关联对象B的类型
	 * 
	 * @param typeB
	 *            关联对象B的类型
	 */
	public void setTypeB(int typeB) {
		this.typeB = (typeB == 0) ? -1 : typeB;
	}

	/**
	 * 获取与指定对象的关系的描述
	 * 
	 * @param info
	 *            指定的对象
	 * @return 返回描述信息，描述信息由本类型定义的caption中的“#”替换为指定对象的名称生成
	 */
	public String getCaption(CIInfo info) {
		String str = this.caption;
		if (info != null)
			str = this.caption.replaceAll("#", info.getName());
		return str;
	}

	/**
	 * 设置与其它对象进行关联时的描述信息
	 * 
	 * @param caption
	 *            与其它对象进行关联时的描述信息
	 * @see #getCaption(CIInfo)
	 */
	public void setCaption(String caption) {
		this.caption = caption;
	}

	/**
	 * 获取关联关系类型的描述
	 * 
	 * @return 返回关联关系类型的描述
	 */
	public String getDesc() {
		return this.desc;
	}

	/**
	 * 设置关联关系类型的描述
	 * 
	 * @param desc
	 *            关联关系类型的描述
	 */
	public void setDesc(String desc) {
		this.desc = desc;
	}

	/**
	 * 获取关联关系类型的名称
	 * 
	 * @return 返回关联关系类型的名称
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * 设置关联关系类型的名称
	 * 
	 * @param name
	 *            关联关系类型的名称
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 获取表示逆向关系的对象
	 * 
	 * @return 返回表示逆向关系的对象
	 */
	public RelationTypeInfo getReverse() {
		return this.reverse;
	}

	/**
	 * 设置表示逆向关系的对象
	 * 
	 * @param reverse
	 *            表示逆向关系的对象
	 */
	public void setReverse(RelationTypeInfo reverse) {
		this.reverse = reverse;
	}

	/**
	 * 获取关联关系类型的 oid
	 * 
	 * @return 返回关联关系类型的 oid
	 */
	public int getOid() {
		return this.oid;
	}

	/**
	 * 设置关联关系类型的 oid
	 * 
	 * @param oid
	 *            关联关系类型的 oid
	 */
	public void setOid(int oid) {
		this.oid = oid;
	}

	/**
	 * 获取逆向关系类型的 oid
	 * 
	 * @return 返回逆向关系类型的 oid
	 */
	public int getReverseOid() {
		return this.reverseOid;
	}

	/**
	 * 设置逆向关系类型的 oid
	 * 
	 * @param reverseOid
	 *            逆向关系类型的 oid
	 */
	public void setReverseOid(int reverseOid) {
		this.reverseOid = reverseOid;
	}

	/**
	 * 获取关联关系的标志
	 * 
	 * @return 返回关联关系的标志，1:1表示一对一的关系，1:n表示一对多的关系
	 */
	public String getFlag() {
		return this.flag;
	}

	/**
	 * 设置关联关系的标志
	 * 
	 * @param flag
	 *            关联关系的标志，1:1表示一对一的关系，1:n表示一对多的关系
	 */
	public void setFlag(String flag) {
		this.flag = flag;
	}
}
