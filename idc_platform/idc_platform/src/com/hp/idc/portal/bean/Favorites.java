package com.hp.idc.portal.bean;

/**
 * 收藏夹
 * 2011.02.17
 * @author chengqp
 */
public class Favorites {
	private int userId;//用户名
	private String menuIds;//模块别名
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getMenuIds() {
		return menuIds;
	}
	public void setMenuIds(String menuIds) {
		this.menuIds = menuIds;
	}
}
