package com.hp.idc.itsm.configure.datasource;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

/**
 * ��ʾ��״�Ķ���
 * 
 * @author ÷԰
 * 
 */
public class TreeNode{
	/**
	 * �洢���������
	 */
	protected String name;

	/**
	 * �洢����� oid
	 */
	protected String oid = "";
	
	/**
	 * �Ƿ�ɵ�
	 */
	protected boolean clickable = true;

	/**
	 * �洢������� oid
	 */
	protected String parentOid = "";

	/**
	 * �洢���е��Ӷ���
	 */
	protected List subItems = new ArrayList();

	/**
	 * ָ�򱾶���ĸ�����
	 */
	protected TreeNode parent = null;
	
	public TreeNode(){
		this.oid = "";
		this.name = "";
		this.parentOid = "";
	}

	/**
	 * ��ȡ���ָ����Ĳ㼶�������ʾ����
	 * 
	 * @return ���ش��ָ����Ĳ㼶�������ʾ���ƣ��硰�й�/����/�Ͼ���
	 */
	public String getDisplayName() {
		if (this.parent == null)
			return this.name==null?"":this.name;
		return this.parent.getDisplayName() + "/" + this.name;
	}

	/**
	 * ��ȡ���������
	 * 
	 * @return ���ض��������
	 */
	public String getName() {
		return this.name == null?"":this.name;
	}

	/**
	 * ���ö��������
	 * 
	 * @param name
	 *            ���������
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * ��ȡ����� oid
	 * 
	 * @return ���ض���� oid
	 */
	public String getOid() {
		return this.oid == null?"":this.oid;
	}

	/**
	 * ���ö���� oid
	 * 
	 * @param oid
	 *            ����� oid
	 */
	public void setOid(String oid) {
		this.oid = oid;
	}

	/**
	 * ��ȡ������� oid
	 * 
	 * @return ���ظ������ oid
	 */
	public String getParentOid() {
		return this.parentOid;
	}

	/**
	 * ���ø������ oid
	 * 
	 * @param parentOid
	 *            ������� oid
	 */
	public void setParentOid(String parentOid) {
		this.parentOid = parentOid;
	}

	/**
	 * ��ȡ������ĸ�����
	 * 
	 * @return ���ر�����ĸ�����
	 */
	public TreeNode getParent() {
		return this.parent;
	}

	/**
	 * ���ñ�����ĸ�����
	 * 
	 * @param parent
	 *            ������ĸ�����
	 */
	public void setParent(TreeNode parent) {
		this.parent = parent;
	}

	/**
	 * ��ȡ������������Ӷ���
	 * 
	 * @return ���ر�����������Ӷ���
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
	 * ��������ʱ����ʾ���ڵ��Ƿ���Ե��(�г����Ӷ���) Ĭ��Ϊ���Ӷ���ʱ���Ե��
	 * 
	 * @return ���ر��ڵ��Ƿ���Ե��
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
	 * ��� javascript ��
	 * 
	 * @param prefix
	 *            js����ǰ׺������Ϊ��
	 * @param w
	 *            ���������jsp�е�out
	 * @param href
	 *            �����ӣ�href�е�/oid/�ᱻ�滻Ϊ�����oid����
	 * @throws IOException
	 */
	public void output(String prefix, Writer w, String href) throws IOException {
		output(prefix, w, href, false);
	};

	/**
	 * ��� javascript ��
	 * 
	 * @param prefix
	 *            js����ǰ׺������Ϊ��
	 * @param w
	 *            ���������jsp�е�out
	 * @param href
	 *            �����ӣ�href�е�/oid/�ᱻ�滻Ϊ�����oid����
	 * @param alwayClickable
	 *            ʼ�տ��Ե��
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
	 * չ�� javascript �������нڵ�
	 * 
	 * @param prefix
	 *            js����ǰ׺������Ϊ��
	 * @param w
	 *            ���������jsp�е�out
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
