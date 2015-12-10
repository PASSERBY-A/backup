package com.hp.idc.portal.bean;

import java.util.Date;

import com.hp.idc.portal.util.StringUtil;

/**
 * 菜单类
 * 2011.02.17
 * @author chengqp
 *
 */
public class Menu {
	private String oid;
	private String title;//模块名称
	private String type;//模块类型（[1,'综合运营门户'],[2,'监控管理中心'],[3,'业务管理中心'],[4,'运维管理中心'],[5,'运营分析中心']）
	private String url;//模块链接
	private String alt;//提示信息
	private String role;//权限字段
	private int creator;//创建人
	private Date createTime;//创建时间
	private int orderNo;
	
	public String getOid() {
		return oid;
	}
	public void setOid(String oid) {
		this.oid = oid;
	}
	public String getTitle() {
		return StringUtil.removeNull(title);
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getType() {
		return StringUtil.removeNull(type);
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getUrl() {
		return StringUtil.removeNull(url);
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getAlt() {
		return StringUtil.removeNull(alt);
	}
	public void setAlt(String alt) {
		this.alt = alt;
	}
	public String getRole() {
		return StringUtil.removeNull(role);
	}
	public void setRole(String role) {
		this.role = role;
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
	public void setOrderNo(int orderNo) {
		this.orderNo = orderNo;
	}
	public int getOrderNo() {
		return orderNo;
	}
}
