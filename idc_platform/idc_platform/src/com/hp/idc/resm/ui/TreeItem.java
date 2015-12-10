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
	 * ��ʾ����
	 */
	private String name;
	
	/**
	 * ���ڵ�id
	 */
	private String parentId;

	/**
	 * ���캯��
	 * @param id id
	 * @param name ��ʾ����
	 * @param parentId ���ڵ�id
	 */
	public TreeItem(String id, String name, String parentId) {
		setId(id);
		setName(name);
		setParentId(parentId);
	}
	
	/**
	 * ��ȡ
	 * @return id
	 * @see #id
	 */
	public String getId() {
		return this.id;
	}

	/**
	 * ����id
	 * @param id id
	 * @see #id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * ��ȡ��ʾ����
	 * @return ��ʾ����
	 * @see #name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * ������ʾ����
	 * @param name ��ʾ����
	 * @see #name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * ��ȡ���ڵ�id
	 * @return ���ڵ�id
	 * @see #parentId
	 */
	public String getParentId() {
		return this.parentId;
	}

	/**
	 * ���ø��ڵ�id
	 * @param parentId ���ڵ�id
	 * @see #parentId
	 */
	public void setParentId(String parentId) {
		this.parentId = (parentId != null) ? parentId : "";
	}
}
