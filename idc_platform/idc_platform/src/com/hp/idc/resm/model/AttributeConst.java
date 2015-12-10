/**
 * 
 */
package com.hp.idc.resm.model;

import java.io.Serializable;

/**
 * 常量定义
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 *
 */
public abstract class AttributeConst implements Serializable {

	/**
	 * 序列化id
	 */
	private static final long serialVersionUID = 8565473583011587480L;
	
	/**
	 * id
	 */
	private String id;
	
	/**
	 * 名称
	 */
	private String name;
	
	/**
	 * 构造函数
	 * @param id id
	 * @param name 名称
	 */
	public AttributeConst(String id, String name) {
		this.id = id;
		this.name = name;
	}

	/**
	 * 获取
	 * @return id
	 * @see #id
	 */
	public String getId() {
		return this.id;
	}

	/**
	 * 设置id
	 * @param id id
	 * @see #id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 获取名称
	 * @return 名称
	 * @see #name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * 设置名称
	 * @param name 名称
	 * @see #name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * 获取常量值
	 * @return 常量值
	 */
	public abstract String getValue();

}
