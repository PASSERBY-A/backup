package com.hp.idc.resm.model;

import java.io.Serializable;

/**
 * 参数定义
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public class Parameter implements Serializable {

	/**
	 * 序列化UID
	 */
	private static final long serialVersionUID = 3132018643426193833L;

	/**
	 * 参数ID
	 */
	private String id;

	/**
	 * 参数名称
	 */
	private String name;

	/**
	 * 参数描述
	 */
	private String remark;

	/**
	 * 构造函数，初始化Parameter对象
	 * 
	 * @param id
	 *            参数id
	 * @param name
	 *            参数名称
	 * @param remark
	 *            参数说明
	 */
	public Parameter(String id, String name, String remark) {
		this.id = id;
		this.name = name;
		this.remark = remark;
	}

	/**
	 * 获取参数id
	 * 
	 * @return 参数id
	 */
	public String getId() {
		return this.id;
	}

	/**
	 * 设置参数id
	 * 
	 * @param id
	 *            参数id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 获取参数名称
	 * 
	 * @return 参数名称
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * 设置参数名称
	 * 
	 * @param name
	 *            参数名称
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 获取参数描述
	 * 
	 * @return 参数描述
	 */
	public String getRemark() {
		return this.remark;
	}

	/**
	 * 设置参数描述
	 * 
	 * @param remark
	 *            参数描述
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}
}
