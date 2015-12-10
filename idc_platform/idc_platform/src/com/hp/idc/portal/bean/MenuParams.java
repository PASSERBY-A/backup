package com.hp.idc.portal.bean;

/**
 * 用户设定菜单参数类
 * @author chengqp
 *
 */
public class MenuParams {
	private int userId;		//用户ID
	private int menuId;		//菜单ID
	private String params;	//参数列表
	
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getMenuId() {
		return menuId;
	}
	public void setMenuId(int menuId) {
		this.menuId = menuId;
	}
	public String getParams() {
		return params;
	}
	public void setParams(String params) {
		this.params = params;
	}
}
