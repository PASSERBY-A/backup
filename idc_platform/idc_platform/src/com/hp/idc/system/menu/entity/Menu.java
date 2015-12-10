/**   
 * @Title: Menu.java 
 * @Description: TODO
 * @date 2011-5-26 下午03:01:39   
 * @version 1.0  
 */
package com.hp.idc.system.menu.entity;     

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import com.hp.idc.common.core.bo.AbstractBaseBO;
import com.hp.idc.system.security.bo.SysResource;
/**
 * 系统菜单类
 * @ClassName: Menu
 * @Descprition: 系统菜单
 * @author <a href="mailto:si-qi.liang@hp.com">Liang, Si-Qi</a>
 * @version 1.0
 */
public class Menu extends AbstractBaseBO implements Serializable, SysResource {
	
	/**
	 * serial version id 
	 */
	private static final long serialVersionUID = 8372501473281851909L;
	/**
	 * resource type of menu
	 */
	public static final String TYPE = "MENU";
	
	/**
	 * fields
	 */
	//菜单ID
	private long id;
	//菜单名称
	private String menuName;
	//描述
	private String description;
	//显示顺序
	private int displayOrder;
	//链接地址
	private String linkPath;
	//上级菜单
	private Menu parentMenu;
	/**
	 * 主键
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator="seq_menu_id")
    @SequenceGenerator(name="seq_menu_id",sequenceName="seq_menu_id")
    @Column(name="menu_id")
	@Override
	public long getId() {
		// TODO Auto-generated method stub
		return id;
	}
	public void setId(long id){
		this.id=id;
	}
	
	@Column(name="menu_name",length=32)
	public String getMenuName() {
		return menuName;
	}
	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}
	@Column(name="description",length=100)
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	@Column(name="display_order")
	public int getDisplayOrder() {
		return displayOrder;
	}
	public void setDisplayOrder(int displayOrder) {
		this.displayOrder = displayOrder;
	}
	@Column(name="link_path",length=200)
	public String getLinkPath() {
		return linkPath;
	}
	public void setLinkPath(String linkPath) {
		this.linkPath = linkPath;
	}
	@ManyToOne(cascade = {CascadeType.MERGE,CascadeType.REFRESH }, optional = true)
	@JoinColumn(name="menu_id")
	public Menu getParentMenu() {
		return parentMenu;
	}
	public void setParentMenu(Menu parentMenu) {
		this.parentMenu = parentMenu;
	}

	
	/*
	 * (non-Javadoc)
	 * @see com.hp.idc.system.security.bo.SysResource#getResourceId()
	 */
	@Override
	public String getResourceId() {
		return String.valueOf(getId());
	}
	/*
	 * (non-Javadoc)
	 * @see com.hp.idc.system.security.bo.SysResource#getParentResource()
	 */
	@Override
	public SysResource getParentResource() {
		return parentMenu;
	}
	/*
	 * (non-Javadoc)
	 * @see com.hp.idc.system.security.bo.SysResource#getResourceName()
	 */
	@Override
	public String getResourceName() {
		return menuName;
	}
	/*
	 * (non-Javadoc)
	 * @see com.hp.idc.system.security.bo.SysResource#getResourceType()
	 */
	@Override
	public String getResourceType() {
		// TODO Auto-generated method stub
		return "MENU";
	}

}
 