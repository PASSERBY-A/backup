/**   
 * @Title: SysRole.java 
 * @Description: TODO
 * @date 2011-5-26 上午10:57:07   
 * @version 1.0  
 */
package com.hp.idc.system.security.entity;     

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.hp.idc.common.core.entity.RemovableEntity;

/**   
 * @Title： 系统角色
 * @author <a href="mailto:si-qi.liang@hp.com">Liang, Si-Qi</a>  
 * @version 1.0   
 */
@Entity(name = "SysRole")
@Table(name = "SYS_ROLE")
public class SysRole extends RemovableEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2144343677934742301L;

	/**
	 * static values of role types;
	 */
	public static final String DEFAULT_ROLE = "D";
	public static final String SPECIAL_ROLE = "S";
	
	//角色ID
	private long id;
	//角色名称
	private String roleName;
	//角色类型
	private String type="D";
	//描述
	private String description;
	//对应用户
	private Set<SysUser> users = new HashSet<SysUser>();
	//对应群组
	private Set<SysGroup> groups =new HashSet<SysGroup>();
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator="seq_sys_role_id")
    @SequenceGenerator(name="seq_sys_role_id",sequenceName="seq_sys_role_id")
	@Column(name="role_id")
	@Override
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
	@Column(name="role_name", length=32)
	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	@Column(name="role_type", length=32)
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	@Column(name="description", length=100)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * 获取拥有该系统角色的系统用户
	 * @Title:getUsers
	 * @Desciption:多对多关联SysUser，SysRole作为被控端
	 * @return
	 */
	@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE},mappedBy="roles", fetch = FetchType.LAZY)
	public Set<SysUser> getUsers() {
		return users;
	}

	public void setUsers(Set<SysUser> users) {
		this.users = users;
	}
	/**
	 * 获取拥有该系统角色的系统群组
	 * @Title:getGroups
	 * @Desciption:多对多关联SysGroup，SysRole作为被控端
	 * @return
	 */
	@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE},mappedBy="roles", fetch = FetchType.LAZY)
	public Set<SysGroup> getGroups() {
		return groups;
	}
	public void setGroups(Set<SysGroup> groups) {
		this.groups = groups;
	}



}
 