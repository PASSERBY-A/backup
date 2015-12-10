package com.hp.idc.portal.bean;

import java.util.Date;

import com.hp.idc.portal.util.StringUtil;

/**
 * 视图节点
 * 2011.02.17
 * @author chengqp
 *
 */
public class ViewNode {
	private String oid;
	private String name;//名称
	private String backColor;//背景色
	private String foreColor;//前景色
	private String width;//默认宽度
	private String height;//默认高度
	private String advProp;//附加属性XML
	private int creator;//创建人
	private Date createTime;//创建时间
	private String path;//模版路径
	private String type;//节点类型
	private String role;
	public String getOid() {
		return oid;
	}
	public void setOid(String oid) {
		this.oid = oid;
	}
	public String getName() {
		return StringUtil.removeNull(name);
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getBackColor() {
		return StringUtil.removeNull(backColor);
	}
	public void setBackColor(String backColor) {
		this.backColor = backColor;
	}
	public String getForeColor() {
		return StringUtil.removeNull(foreColor);
	}
	public void setForeColor(String foreColor) {
		this.foreColor = foreColor;
	}
	public String getWidth() {
		return StringUtil.removeNull(width);
	}
	public void setWidth(String width) {
		this.width = width;
	}
	public String getHeight() {
		return StringUtil.removeNull(height);
	}
	public void setHeight(String height) {
		this.height = height;
	}
	public String getAdvProp() {
		return StringUtil.removeNull(advProp);
	}
	public void setAdvProp(String advProp) {
		this.advProp = advProp;
	}
	public int getCreator() {
		return creator;
	}
	public void setCreator(int creator) {
		this.creator = creator;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getPath() {
		return StringUtil.removeNull(path);
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getType() {
		return StringUtil.removeNull(type);
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
}
