/**
 * 
 */
package com.hp.idc.resm.ui;

/**
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 *
 */
public class TreeItem {

	/**
	 * id
	 */
	private String id;
	
	/**
	 * 显示名称
	 */
	private String name;
	
	/**
	 * 父节点id
	 */
	private String parentId;

	/**
	 * 构造函数
	 * @param id id
	 * @param name 显示名称
	 * @param parentId 父节点id
	 */
	public TreeItem(String id, String name, String parentId) {
		setId(id);
		setName(name);
		setParentId(parentId);
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
	 * 获取显示名称
	 * @return 显示名称
	 * @see #name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * 设置显示名称
	 * @param name 显示名称
	 * @see #name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 获取父节点id
	 * @return 父节点id
	 * @see #parentId
	 */
	public String getParentId() {
		return this.parentId;
	}

	/**
	 * 设置父节点id
	 * @param parentId 父节点id
	 * @see #parentId
	 */
	public void setParentId(String parentId) {
		this.parentId = (parentId != null) ? parentId : "";
	}
}
