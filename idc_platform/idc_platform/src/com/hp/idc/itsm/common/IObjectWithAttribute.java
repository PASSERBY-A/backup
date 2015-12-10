package com.hp.idc.itsm.common;

/**
 * 表示带属性的对象接口
 * 
 * @author 梅园
 * 
 */
public interface IObjectWithAttribute {

	/**
	 * 根据 id 查询对象的属性
	 * 
	 * @param id
	 *            查询标识
	 * @return 属性值，找不到时返回 null
	 */
	public String getAttribute(String id);
}
