/**   
 * @{#} SampleEntity.java Create on 2011-5-25 ����05:38:00   
 *   
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
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.hp.idc.common.core.entity.RemovableEntity;
  
/**   
 * @Title:ϵͳ�û�
 * @author <a href="mailto:si-qi.liang@hp.com">Liang, Si-Qi</a>  
 * @version 1.0   
 */
@Entity(name = "SysUser")
@Table(name = "SYS_USER")
@NamedQueries({
	@NamedQuery(name="SYS_USER.findSysUserByLoginName", query="SELECT u FROM SysUser u WHERE u.loginName = :loginName ")
})
public class SysUser extends RemovableEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5656484812673289479L;

	public SysUser() {
		super();
	}
	/**
	 * static values of user status;
	 */
	public static final long INACTIVE = 0L;
	public static final long NORMAL = 1L;
	public static final long FORBIDDEN = -1L;
	/**
	 * data fields
	 */
	//primary key of user
	private long id; 
	//the username used to login
	private String loginName; 
	//the password used to login
	private String password;  
	//actual name of the user
	private String actualName;
	//status 
	private long status= 0;
	/**
	 * 
	 */
	private Set<SysGroup> groups=new HashSet<SysGroup>();
	private Set<SysRole> roles=new HashSet<SysRole>();
	

	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator="seq_sys_user_id")
    @SequenceGenerator(name="seq_sys_user_id",sequenceName="seq_sys_user_id")
	@Column(name="user_id")
	@Override
	public long getId() {
		return id;
	}
	public void setId(long id){
		this.id=id;
	}
	
	@Column(name="login_name", length=32,nullable=false, unique=true)
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	
	@Column(name="password", length=32)
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	@Column(name="actual_name", length=32)
	public String getActualName() {
		return actualName;
	}
	public void setActualName(String actualName) {
		this.actualName = actualName;
	}
	
	@Column(name="status")
	public long getStatus() {
		return status;
	}
	public void setStatus(long status) {
		this.status = status;
	}
	
	/**
	 * @Title:getGroups
	 * @Desciption: ��Զ����ϵͳ��ɫ��SysUser��Ϊ���ض�
	 * @return
	 */
	@ManyToMany(cascade = CascadeType.REFRESH,mappedBy="users", fetch = FetchType.LAZY)
	public Set<SysGroup> getGroups() {
		return groups;
	}
	public void setGroups(Set<SysGroup> groups) {
		this.groups = groups;
	}
	 

	/**
	 * @Title:getRoles
	 * @Desciption: ��Զ����ϵͳ��ɫ��SysUser��Ϊ���ƶ�
	 * @return
	 */
	@ManyToMany(cascade = CascadeType.REMOVE)
	@JoinTable(name = "SYS_USER_ROLE", joinColumns = {@JoinColumn(name = "user_id",nullable = false)}, 
			inverseJoinColumns = @JoinColumn(name = "role_id",nullable = false))
	public Set<SysRole> getRoles() {
		return roles;
	}
	public void setRoles(Set<SysRole> roles) {
		this.roles = roles;
	}

	/**
	 * Ϊϵͳ�û����ӽ�ɫ
	 * @Title:addRole
	 * @Desciption:TODO
	 * @param role
	 */
	public void addRole(SysRole role) {  
        this.roles.add(role);  
    }  
	/**
	 * Ϊϵͳ�û�ɾ����ɫ
	 * @Title:removeRole
	 * @Desciption:TODO
	 * @param role
	 */
    public void removeRole(SysRole role) {  
        this.roles.remove(role);  
    } 

}
 