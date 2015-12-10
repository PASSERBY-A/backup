/**   
 * @Title: SysGroup.java 
 * @Description: TODO
 * @date 2011-5-26 ����11:07:53   
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
 * @Title: ϵͳȺ��
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
	
	//Ⱥ��ID
	private long id;  
	//Ⱥ������
	private String groupName;
	//����
	private String description;
	//��Ӧ��ɫ
	private Set<SysRole> roles = new HashSet<SysRole>();
	//��Ӧ�û�
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
	 *   ��ȡϵͳȺ��ӵ�е�ϵͳ��ɫ
	 * @Title:getRoles
	 * @Desciption: ��������SysGroup��Ϊ���ƶ�
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
	 * ΪϵͳȺ�����ӽ�ɫ
	 * @Title:addRole
	 * @Desciption:TODO
	 * @param role
	 */
	public void addRole(SysRole role) {  
        this.roles.add(role);  
    }  
	/**
	 * ΪϵͳȺ��ɾ����ɫ
	 * @Title:removeRole
	 * @Desciption:TODO
	 * @param role
	 */
    public void removeRole(SysRole role) {  
        this.roles.remove(role);  
    } 
	/**
	 *   ��ȡϵͳȺ��ӵ�е�ϵͳ�û�
	 * @Title:getRoles
	 * @Desciption: ��������SysGroup��Ϊ���ƶ�
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
	 * ΪϵͳȺ������ϵͳ�û�
	 * @Title:addUser
	 * @Desciption:TODO
	 * @param user
	 */
	public void addUser(SysUser user) {  
        this.users.add(user);  
    }  
	/**
	 * ��ϵͳȺ�����Ƴ��û�
	 * @Title:removeUser
	 * @Desciption:TODO
	 * @param user
	 */
    public void removeUser(SysUser user) {  
        this.users.remove(user);  
    } 

}
 