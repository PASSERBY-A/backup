package com.hp.idc.itsm.common;

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
public class TreeObject extends ITSMInfo {

	/**
	 * �洢������� oid(��parentId���Զ���һ)
	 */
	protected int parentOid = -1;
	
	/**
	 * �洢������� id(��parentOid���Զ���һ)
	 */
	protected String parentId = "";

	/**
	 * �洢���е��Ӷ���
	 */
	protected List subItems = new ArrayList();

	/**
	 * ָ�򱾶���ĸ�����
	 */
	protected TreeObject parent = null;

	/**
	 * ��ȡ���ָ����Ĳ㼶�������ʾ����
	 * 
	 * @return ���ش��ָ����Ĳ㼶�������ʾ���ƣ��硰�й�/����/�Ͼ���
	 */
	public String getDisplayName() {
		if (this.parent == null)
			return this.name;
		return this.parent.getDisplayName() + "/" + this.name;
	}

	/**
	 * ��ȡ������� oid
	 * 
	 * @return ���ظ������ oid
	 */
	public int getParentOid() {
		return this.parentOid;
	}

	/**
	 * ���ø������ oid
	 * 
	 * @param parentOid
	 *            ������� oid
	 */
	public void setParentOid(int parentOid) {
		this.parentOid = parentOid;
	}

	/**
	 * ��ȡ������ĸ�����
	 * 
	 * @return ���ر�����ĸ�����
	 */
	public TreeObject getParent() {
		return this.parent;
	}

	/**
	 * ���ñ�����ĸ�����
	 * 
	 * @param parent
	 *            ������ĸ�����
	 */
	public void setParent(TreeObject parent) {
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
	
	public void addSubItems(TreeObject to){
		to.setParent(this);
		to.setParentOid(this.getOid());
		this.subItems.add(to);
	}
	
	public TreeObject addSubItems(int oid,String name){
		TreeObject to = new TreeObject();
		to.setName(name);
		to.setOid(oid);
		to.setParent(this);
		to.setParentOid(this.getOid());
		this.subItems.add(to);
		return to;
	}

	/**
	 * ���� id ��ѯ���������<br>
	 * TreeObject ��������԰�����<br>
	 * oid: ����oid<br>
	 * parentOid: ������oid<br>
	 * name: ��������
	 * 
	 * @param id
	 *            ��ѯ��ʶ
	 * @return ����ֵ���Ҳ���ʱ���� null
	 */
	public String getAttribute(String id) {
		if (id == null)
			return null;
		if (id.equals("oid"))
			return "" + this.oid;
		if (id.equals("parentOid"))
			return "" + this.parentOid;
		if (id.equals("name"))
			return this.name;
		return null;
	}

	/**
	 * ��������ʱ����ʾ���ڵ��Ƿ���Ե��(�г����Ӷ���) Ĭ��Ϊ���Ӷ���ʱ���Ե��
	 * 
	 * @return ���ر��ڵ��Ƿ���Ե��
	 */
	public boolean isClickable() {
		return (this.subItems.size() == 0);
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
			TreeObject l = (TreeObject) this.subItems.get(i);
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
			TreeObject l = (TreeObject) this.subItems.get(i);
			l.outputExpand(prefix + "_" + i, w);
		}
	}

	public String getParentId() {
		return parentId == null ? "" : parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	
	
	

}
