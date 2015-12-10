package com.hp.idc.itsm.configure.datasource;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

/**
 * 表示树状的对象
 * 
 * @author 梅园
 * 
 */
public class TreeNode{
	/**
	 * 存储对象的名称
	 */
	protected String name;

	/**
	 * 存储对象的 oid
	 */
	protected String oid = "";
	
	/**
	 * 是否可点
	 */
	protected boolean clickable = true;

	/**
	 * 存储父对象的 oid
	 */
	protected String parentOid = "";

	/**
	 * 存储所有的子对象
	 */
	protected List subItems = new ArrayList();

	/**
	 * 指向本对象的父对象
	 */
	protected TreeNode parent = null;
	
	public TreeNode(){
		this.oid = "";
		this.name = "";
		this.parentOid = "";
	}

	/**
	 * 获取带分隔符的层级对象的显示名称
	 * 
	 * @return 返回带分隔符的层级对象的显示名称，如“中国/江苏/南京”
	 */
	public String getDisplayName() {
		if (this.parent == null)
			return this.name==null?"":this.name;
		return this.parent.getDisplayName() + "/" + this.name;
	}

	/**
	 * 获取对象的名称
	 * 
	 * @return 返回对象的名称
	 */
	public String getName() {
		return this.name == null?"":this.name;
	}

	/**
	 * 设置对象的名称
	 * 
	 * @param name
	 *            对象的名称
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 获取对象的 oid
	 * 
	 * @return 返回对象的 oid
	 */
	public String getOid() {
		return this.oid == null?"":this.oid;
	}

	/**
	 * 设置对象的 oid
	 * 
	 * @param oid
	 *            对象的 oid
	 */
	public void setOid(String oid) {
		this.oid = oid;
	}

	/**
	 * 获取父对象的 oid
	 * 
	 * @return 返回父对象的 oid
	 */
	public String getParentOid() {
		return this.parentOid;
	}

	/**
	 * 设置父对象的 oid
	 * 
	 * @param parentOid
	 *            父对象的 oid
	 */
	public void setParentOid(String parentOid) {
		this.parentOid = parentOid;
	}

	/**
	 * 获取本对象的父对象
	 * 
	 * @return 返回本对象的父对象
	 */
	public TreeNode getParent() {
		return this.parent;
	}

	/**
	 * 设置本对象的父对象
	 * 
	 * @param parent
	 *            本对象的父对象
	 */
	public void setParent(TreeNode parent) {
		this.parent = parent;
	}

	/**
	 * 获取本对象的所有子对象
	 * 
	 * @return 返回本对象的所有子对象
	 */
	public List getSubItems() {
		return this.subItems;
	}
	
	public void addSubItems(TreeNode to){
		to.setParent(this);
		to.setParentOid(this.getOid());
		this.subItems.add(to);
	}
	
	public TreeNode addSubItems(String oid,String name){
		TreeNode to = new TreeNode();
		to.setName(name);
		to.setOid(oid);
		to.setParent(this);
		to.setParentOid(this.getOid());
		this.subItems.add(to);
		return to;
	}

	/**
	 * 在生成树时，表示本节点是否可以点击(有超链接动作) 默认为无子对象时可以点击
	 * 
	 * @return 返回本节点是否可以点击
	 */
	public boolean isClickable() {
		if(clickable)
			return clickable;
		else
			return (this.subItems.size() == 0);
	}

	public void setClickable(boolean clickable) {
		this.clickable = clickable;
	}

	/**
	 * 输出 javascript 树
	 * 
	 * @param prefix
	 *            js变量前缀，不能为空
	 * @param w
	 *            输出对象，如jsp中的out
	 * @param href
	 *            超链接，href中的/oid/会被替换为对象的oid属性
	 * @throws IOException
	 */
	public void output(String prefix, Writer w, String href) throws IOException {
		output(prefix, w, href, false);
	};

	/**
	 * 输出 javascript 树
	 * 
	 * @param prefix
	 *            js变量前缀，不能为空
	 * @param w
	 *            输出对象，如jsp中的out
	 * @param href
	 *            超链接，href中的/oid/会被替换为对象的oid属性
	 * @param alwayClickable
	 *            始终可以点击
	 * @throws IOException
	 */
	public void output(String prefix, Writer w, String href,
			boolean alwayClickable) throws IOException {
		w.write("var " + prefix + " = new Ext.tree.TreeNode({\n" + "text: '"
				+ getName() + "',\n");
		if (alwayClickable || isClickable())
			w.write("href: '" + href.replaceAll("/oid/", "" + getOid())
					+ "',\n");
		w.write("draggable:false" + "});\n");
		for (int i = 0; i < this.subItems.size(); i++) {
			TreeNode l = (TreeNode) this.subItems.get(i);
			l.output(prefix + "_" + i, w, href, alwayClickable);
			w.write(prefix + ".appendChild(" + prefix + "_" + i + ");\n");
		}
	};

	/**
	 * 展开 javascript 树的所有节点
	 * 
	 * @param prefix
	 *            js变量前缀，不能为空
	 * @param w
	 *            输出对象，如jsp中的out
	 * @throws IOException
	 */
	public void outputExpand(String prefix, Writer w) throws IOException {
		if (this.subItems.size() > 0)
			w.write(prefix + ".expand();\n");
		for (int i = 0; i < this.subItems.size(); i++) {
			TreeNode l = (TreeNode) this.subItems.get(i);
			l.outputExpand(prefix + "_" + i, w);
		}
	}
	
	

}
