/**   
 * @Title: SysGroup.java 
 * @Description: TODO
 * @date 2011-5-26 上午11:07:53   
 * @version 1.0  
 */
package com.hp.idc.system.security.entity;     

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.hp.idc.common.core.entity.RemovableEntity;


/**   
 * @Title: 系统群组
 * @author <a href="mailto:si-qi.liang@hp.com">Liang, Si-Qi</a>  
 * @version 1.0   
 */
@Entity(name = "SysGroup")
@Table(name = "SYS_GROUP")
public class SysGroup extends RemovableEntity {

	/**
	 * serial version
	 */
	private static final long serialVersionUID = -9126943097405077686L;
	
	//群组ID
	private long id;  
	//群组名称
	private String groupName;
	//描述
	private String description;
	//对应角色
	private Set<SysRole> roles = new HashSet<SysRole>();
	//对应用户
	private Set<SysUser> users = new HashSet<SysUser>();
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator="seq_sys_group_id")
    @SequenceGenerator(name="seq_sys_group_id",sequenceName="seq_sys_group_id")
	@Column(name="group_id")
	@Override
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	@Column(name="group_name", length=32,nullable=false)
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	@Column(name="description", length=100)
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 *   获取系统群组拥有的系统角色
	 * @Title:getRoles
	 * @Desciption: 多多关联，SysGroup作为控制端
	 * @return
	 */
	@ManyToMany(cascade = CascadeType.REMOVE)
	@JoinTable(name = "SYS_GROUP_ROLE", joinColumns = {@JoinColumn(name = "group_id",nullable = false)}, 
			inverseJoinColumns = @JoinColumn(name = "role_id",nullable = false))
	public Set<SysRole> getRoles() {
		return roles;
	}
	
	public void setRoles(Set<SysRole> roles) {
		this.roles = roles;
	}

	/**
	 * 为系统群组增加角色
	 * @Title:addRole
	 * @Desciption:TODO
	 * @param role
	 */
	public void addRole(SysRole role) {  
        this.roles.add(role);  
    }  
	/**
	 * 为系统群组删除角色
	 * @Title:removeRole
	 * @Desciption:TODO
	 * @param role
	 */
    public void removeRole(SysRole role) {  
        this.roles.remove(role);  
    } 
	/**
	 *   获取系统群组拥有的系统用户
	 * @Title:getRoles
	 * @Desciption: 多多关联，SysGroup作为控制端
	 * @return
	 */
	@ManyToMany(cascade = CascadeType.REMOVE)
	@JoinTable(name = "SYS_GROUP_USER", joinColumns = {@JoinColumn(name = "group_id",nullable = false)}, 
			inverseJoinColumns = @JoinColumn(name = "user_id",nullable = false))
	public Set<SysUser> getUsers() {
		return users;
	}
	public void setUsers(Set<SysUser> users) {
		this.users = users;
	}
	/**
	 * 为系统群组增加系统用户
	 * @Title:addUser
	 * @Desciption:TODO
	 * @param user
	 */
	public void addUser(SysUser user) {  
        this.users.add(user);  
    }  
	/**
	 * 从系统群组中移除用户
	 * @Title:removeUser
	 * @Desciption:TODO
	 * @param user
	 */
    public void removeUser(SysUser user) {  
        this.users.remove(user);  
    } 

}
 