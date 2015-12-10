package com.hp.idc.itsm.authorization;

import java.util.ArrayList;
import java.util.List;

public class MenuInfo {
	
	
	/**
	 * �˵�ID
	 */
	private String id = "";
	
	/**
	 * �˵���ʾ��
	 */
	private String name = "";
	
	/**
	 * ���ں���ı�ע����
	 */
	private String displayText = "";
	
	/**
	 * ��ID
	 */
	private String parentId = "";
	
	/**
	 * ��ʾ����˳��
	 */
	private int displayIndex = 0;
	
	/**
	 * �Ƿ���Ҷ��
	 */
	private boolean leaf = false;
	
	/**
	 * ���ӵ�ַ ��leaf=true��Ч��
	 */
	private String href = "";
	
	/**
	 * ���hrefΪ�գ�ִ�д�������javascript��leaf=true��Ч��
	 */
	private String script = "";
	
	/**
	 * Ĭ��Ȩ�޿��ƴ�,��ʽ�����μ�RuleManager
	 */
	private String ruleStr = "";
	
	/**
	 * �Ƿ������ڲ˵����ý���༭�˲˵�
	 * ����ע������ģ��Ͳ�����༭
	 */
	private boolean canEdit = true;
	
	/**
	 * �ӽڵ�
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
