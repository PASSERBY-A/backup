/**
 * 
 */
package com.hp.idc.resm.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 生成前台用到的xml树的工作
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public class TreeBuilder {

	/**
	 * 存放树节点id->树节点的信息
	 */
	private Map<String, TreeItem> map = new HashMap<String, TreeItem>();

	/**
	 * 存放树节点的父id->树节点列表的信息
	 */
	private Map<String, List<TreeItem>> parentMap = new HashMap<String, List<TreeItem>>();

	/**
	 * 存放所有树节点信息
	 */
	private List<TreeItem> list = new ArrayList<TreeItem>();

	/**
	 * 添加一个树节点信息
	 * 
	 * @param item
	 *            树节点信息
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
	 * 内部函数，生成树数据用
	 * 
	 * @param sb
	 *            拼接字符串用
	 * @param m
	 *            树节点信息
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
			if (this.map.get(item.getParentId()) == null) // 没有父节点
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
