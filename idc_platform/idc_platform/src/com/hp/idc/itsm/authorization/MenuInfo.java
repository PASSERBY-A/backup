package com.hp.idc.itsm.authorization;

import java.util.ArrayList;
import java.util.List;

public class MenuInfo {
	
	
	/**
	 * 菜单ID
	 */
	private String id = "";
	
	/**
	 * 菜单显示名
	 */
	private String name = "";
	
	/**
	 * 窗口后面的备注描述
	 */
	private String displayText = "";
	
	/**
	 * 父ID
	 */
	private String parentId = "";
	
	/**
	 * 显示优先顺序
	 */
	private int displayIndex = 0;
	
	/**
	 * 是否是叶子
	 */
	private boolean leaf = false;
	
	/**
	 * 链接地址 （leaf=true有效）
	 */
	private String href = "";
	
	/**
	 * 如果href为空，执行此描述的javascript（leaf=true有效）
	 */
	private String script = "";
	
	/**
	 * 默认权限控制串,格式描述参见RuleManager
	 */
	private String ruleStr = "";
	
	/**
	 * 是否允许在菜单配置界面编辑此菜单
	 * 比如注入进来的，就不允许编辑
	 */
	private boolean canEdit = true;
	
	/**
	 * 子节点
	 */
	private List subMenus = new ArrayList();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List getSubMenus() {
		return subMenus;
	}

	public void setSubMenus(List subMenus) {
		this.subMenus = subMenus;
	}
	
	public void addSubMenus(MenuInfo menu){
		if (this.subMenus == null)
			this.subMenus = new ArrayList();
		this.subMenus.add(menu);
	}

	public String getParentId() {
		return parentId == null?"":parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public int getDisplayIndex() {
		return displayIndex;
	}

	public void setDisplayIndex(int displayIndex) {
		this.displayIndex = displayIndex;
	}

	public String getRuleStr() {
		return ruleStr;
	}

	public void setRuleStr(String ruleStr) {
		this.ruleStr = ruleStr;
	}

	public String getPath() {
		MenuInfo temp_ = this;
		String path = "";
		while (temp_!=null){
			path += "/"+temp_.getId();
			if (!temp_.getParentId().equals(""))
				temp_ = MenuManager.getMenuInfo(temp_.getParentId());
			else
				temp_ = null;
			
		}
		return path;
	}

	public String getDisplayText() {
		return displayText == null? "":displayText;
	}

	public void setDisplayText(String displayText) {
		this.displayText = displayText;
	}

	public boolean isLeaf() {
		return leaf;
	}

	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}

	public String getHref() {
		return href == null? "" : href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public String getScript() {
		return script == null?"":script;
	}

	public void setScript(String script) {
		this.script = script;
	}
	
	public MenuInfo getClone(){
		MenuInfo mi = new MenuInfo();
		mi.setId(this.id);
		mi.setName(this.name);
		mi.setDisplayIndex(this.displayIndex);
		mi.setDisplayText(this.displayText);
		mi.setHref(this.href);
		mi.setLeaf(this.leaf);
		mi.setParentId(this.parentId);
		mi.setRuleStr(this.ruleStr);
		mi.setScript(this.script);
		mi.setCanEdit(this.canEdit);
		mi.setSubMenus(this.subMenus);
		return mi;
	}

	public boolean isCanEdit() {
		return canEdit;
	}

	public void setCanEdit(boolean canEdit) {
		this.canEdit = canEdit;
	}
}
