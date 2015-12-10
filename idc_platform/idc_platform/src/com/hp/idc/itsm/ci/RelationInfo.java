package com.hp.idc.itsm.ci;

/**
 * 表示关联关系的信息
 * 
 * @author 梅园
 * 
 */
public class RelationInfo {
	/**
	 * 存储关联对象A
	 */
	protected CIInfo objectA;

	/**
	 * 存储关联对象B
	 */
	protected CIInfo objectB;

	/**
	 * 存储关联关系类型
	 */
	protected RelationTypeInfo type;

	/**
	 * 获取关联关系的id，由 类型oid + "-" + A对象oid + "-" + B对象oid 自动生成
	 * 
	 * @return 返回关联关系的id
	 */
	public String getId() {
		return "" + this.type.getOid() + "-" + this.objectA.getOid() + "-"
				+ this.objectB.getOid();
	}

	/**
	 * 获取关联关系的描述信息
	 * 
	 * @return 返回关联关系的描述信息
	 */
	public String getCaption() {
		return this.objectA.getName() + this.type.getCaption(this.objectB);
	}

	/**
	 * 获取关联对象A
	 * 
	 * @return 返回关联对象A
	 */
	public CIInfo getObjectA() {
		return this.objectA;
	}

	/**
	 * 设置关联对象A
	 * 
	 * @param objectA
	 *            关联对象A
	 */
	public void setObjectA(CIInfo objectA) {
		this.objectA = objectA;
	}

	/**
	 * 获取关联对象A
	 * 
	 * @return 返回关联对象B
	 */
	public CIInfo getObjectB() {
		return this.objectB;
	}

	/**
	 * 设置关联对象B
	 * 
	 * @param objectB
	 *            关联对象B
	 */
	public void setObjectB(CIInfo objectB) {
		this.objectB = objectB;
	}

	/**
	 * 获取关联关系类型
	 * 
	 * @return 返回关联关系类型
	 */
	public RelationTypeInfo getType() {
		return this.type;
	}

	/**
	 * 设置关联关系类型
	 * 
	 * @param type
	 *            关联关系类型
	 */
	public void setType(RelationTypeInfo type) {
		this.type = type;
	}

}
