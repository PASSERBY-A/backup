package com.hp.idc.portal.bean;

/**
 * �û��趨�˵�������
 * @author chengqp
 *
 */
public class MenuParams {
	private int userId;		//�û�ID
	private int menuId;		//�˵�ID
	private String params;	//�����б�
	
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
