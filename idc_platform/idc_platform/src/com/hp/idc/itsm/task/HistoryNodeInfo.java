package com.hp.idc.itsm.task;

import java.util.ArrayList;
import java.util.List;

public class HistoryNodeInfo{

	private String personName = "";
	private String nodeDesc = "";
	private int taskOid = -1;
	private int taskDataId = -1;
	private boolean isRollback = false;
	
	private boolean isBranchEnd = false;
	
	private boolean isEditing = false;
	
	private int status;
	
	private HistoryNodeInfo parentInfo;
	
	private List childs = new ArrayList();
	
	/**
	 * 第几列
	 */
	private int line_level = -1;
	
	/**
	 * 第几行
	 */
	private int line_index = -1;
	
	private List parent = new ArrayList();
	
	private int x;
	private int y;

	public String getPersonName() {
		return personName;
	}

	public void setPersonName(String personName) {
		this.personName = personName;
	}

	public String getNodeDesc() {
		return nodeDesc;
	}

	public void setNodeDesc(String nodeDesc) {
		this.nodeDesc = nodeDesc;
	}

	public int getTaskOid() {
		return taskOid;
	}

	public void setTaskOid(int taskOid) {
		this.taskOid = taskOid;
	}

	public int getTaskDataId() {
		return taskDataId;
	}

	public void setTaskDataId(int taskDataId) {
		this.taskDataId = taskDataId;
	}

	public int getLine_level() {
		return line_level;
	}

	public void setLine_level(int line_level) {
		this.line_level = line_level;
	}

	public int getLine_index() {
		return line_index;
	}

	public void setLine_index(int line_index) {
		this.line_index = line_index;
	}

	public boolean isRollback() {
		return isRollback;
	}

	public void setRollback(boolean isRollback) {
		this.isRollback = isRollback;
	}
	
	
	public String getVMLCode() {
		StringBuffer ret = new StringBuffer();
		
		return ret.toString();
	}

	public List getParent() {
		return parent;
	}

	public void addParent(String pOid) {
		this.parent.add(pOid);
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public boolean isBranchEnd() {
		return isBranchEnd;
	}

	public void setBranchEnd(boolean isBranchEnd) {
		this.isBranchEnd = isBranchEnd;
	}

	public boolean isEditing() {
		return isEditing;
	}

	public void setEditing(boolean isEditing) {
		this.isEditing = isEditing;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public HistoryNodeInfo getParentInfo() {
		return parentInfo;
	}

	public void setParentInfo(HistoryNodeInfo parentInfo) {
		this.parentInfo = parentInfo;
	}

	public List getChilds() {
		return childs;
	}

	public void setChilds(List childs) {
		this.childs = childs;
	}
}
