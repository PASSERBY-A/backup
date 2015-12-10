package com.hp.idc.kbm.bo;

import java.io.Serializable;
import java.util.List;

public class TreeNode implements Serializable {
	/**
	 * 知识类别TreeNode
	 */
	private static final long serialVersionUID = 1L;

	private long id;

	private String text;

	private String des;
	private long parent;
	private List<TreeNode> children;
	private String iconCls;
	private boolean leaf;
	private int baseType;

	public int getBaseType() {

		return baseType;

	}

	public void setBaseType(int baseType) {
		this.baseType = baseType;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getDes() {
		return des;
	}

	public void setDes(String des) {
		this.des = des;
	}

	// public String getParent() {
	// return parent;
	// }
	//
	// public void setParent(Object parent) {
	// if (parent.getClass() == String.class) {
	// this.parent = parent.toString();
	// } else {
	// this.parent = String.valueOf(parent);
	// }
	// }

	public List<TreeNode> getChildren() {
		return children;
	}

	public long getParent() {
		return parent;
	}

	public void setParent(long parent) {
		this.parent = parent;
	}

	public void setChildren(List<TreeNode> children) {
		this.children = children;
	}

	public String getIconCls() {
		return iconCls;
	}

	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}

	public boolean isLeaf() {
		return leaf;
	}

	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}

}
