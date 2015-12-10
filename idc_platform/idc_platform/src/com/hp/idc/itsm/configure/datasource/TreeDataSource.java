package com.hp.idc.itsm.configure.datasource;

import java.util.List;
import java.util.Stack;

import com.hp.idc.itsm.configure.FieldDataSource;

/**
 * 用于程序建立TreeFieldInfo的时候的数据源
 * @author <a href="mailto:lihz@lianchuang.com">FluteD</a>
 *
 */
public class TreeDataSource extends FieldDataSource{
	
	protected TreeNode root = new TreeNode();
	
	public TreeNode setRoot(TreeNode rt){
		root = rt;
		return root;
	}
	
	public TreeNode addItem(TreeNode subto){
		root.addSubItems(subto);
		return subto;
	}
	
	public TreeNode addItem(String id, String name){
		return root.addSubItems(id,name);
	}
	
	public String getTreeDataString(TreeNode treeNode){
		StringBuffer ret = new StringBuffer();
		ret.append("{");
		ret.append("'id':'"+treeNode.getOid()+"',");
		ret.append("_click: "+treeNode.isClickable()+",");
		ret.append("'text':'"+treeNode.getName()+"'");
		List children = treeNode.getSubItems();
		if (children.size()>0){
			ret.append(",children:[");
			boolean hasOne = false;
			for(int i = 0; i < children.size(); i++){
				TreeNode to = (TreeNode)children.get(i);
				if (hasOne)
					ret.append(",");
				ret.append(getTreeDataString(to));
				hasOne = true;
			}
			ret.append("]");
		}
		ret.append("}");
		
		return ret.toString();
	}
	
	public String getData(String filter, String style) {
		String ret = "";
		boolean hasOne = false;
		List children = root.getSubItems();
		for(int i = 0; i < children.size(); i++){
			TreeNode to = (TreeNode)children.get(i);
			if (hasOne)
				ret +=",";
			ret += getTreeDataString(to);
			hasOne = true;
		}
		return ret;
	}
	
	public void load(String data) {
		
	}
	
	public List getKeys(String filter) {
		return null;
	}
	
	public String getDisplayText(String id) {
		if (id == null)
			return "";
		try {
			Stack s = new Stack();
			List l = this.root.getSubItems();
			for (int i = 0; i < l.size(); i++){
				s.push(l.get(i));
			}
			while (s.size()>0){
				TreeNode tn = (TreeNode)s.pop();
				if (tn.getOid().equals(id))
					return tn.getDisplayName();
				List child = tn.getSubItems();
				if (child!=null && child.size()>0){
					for (int i = 0; i < child.size(); i++){
						s.push(child.get(i));
					}
				}
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		return "";
	}
}
