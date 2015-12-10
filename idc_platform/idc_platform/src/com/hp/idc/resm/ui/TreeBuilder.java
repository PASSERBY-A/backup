/**
 * 
 */
package com.hp.idc.resm.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ����ǰ̨�õ���xml���Ĺ���
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public class TreeBuilder {

	/**
	 * ������ڵ�id->���ڵ����Ϣ
	 */
	private Map<String, TreeItem> map = new HashMap<String, TreeItem>();

	/**
	 * ������ڵ�ĸ�id->���ڵ��б����Ϣ
	 */
	private Map<String, List<TreeItem>> parentMap = new HashMap<String, List<TreeItem>>();

	/**
	 * ����������ڵ���Ϣ
	 */
	private List<TreeItem> list = new ArrayList<TreeItem>();

	/**
	 * ���һ�����ڵ���Ϣ
	 * 
	 * @param item
	 *            ���ڵ���Ϣ
	 */
	public void addItem(TreeItem item) {
		this.map.put(item.getId(), item);
		this.list.add(item);
		List<TreeItem> l = this.parentMap.get(item.getParentId());
		if (l == null) {
			l = new ArrayList<TreeItem>();
			this.parentMap.put(item.getParentId(), l);
		}
		l.add(item);
	}

	/**
	 * �ڲ�������������������
	 * 
	 * @param sb
	 *            ƴ���ַ�����
	 * @param m
	 *            ���ڵ���Ϣ
	 */
	private void getTree(StringBuilder sb, TreeItem m) {
		sb.append("<node label=\"").append(m.getName()).append("\" id=\"")
				.append(m.getId());
		//sb.append("\" parentId=\"").append(m.getParentId());
		sb.append("\">\n");
		List<TreeItem> l = this.parentMap.get(m.getId());
		if (l != null) {
			for (int i = 0; i < l.size(); i++)
				getTree(sb, l.get(i));
		}
		sb.append("</node>\n");
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("<treeRoot>\n");
		for (int i = 0; i < this.list.size(); i++) {
			TreeItem item = this.list.get(i);
			if (this.map.get(item.getParentId()) == null) // û�и��ڵ�
				getTree(sb, item);
		}
		sb.append("</treeRoot>\n");
		return sb.toString();
	}

	/**
	 * test
	 * 
	 * @param args
	 *            test
	 */
	public static void main(String[] args) {
		TreeBuilder tb = new TreeBuilder();
		tb.addItem(new TreeItem("1", "a1", "2"));
		tb.addItem(new TreeItem("2", "a2", ""));
		tb.addItem(new TreeItem("3", "a3", "1"));
		tb.addItem(new TreeItem("4", "a4", "5"));
		System.out.println(tb);
	}
}
