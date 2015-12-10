package com.hp.idc.itsm.security;

import java.util.List;

import com.hp.idc.itsm.configure.FieldInfo;
import com.hp.idc.itsm.configure.FieldManager;
import com.hp.idc.itsm.inter.OrganizationInfoInterface;
import com.hp.idc.itsm.inter.PersonInfoInterface;
import com.hp.idc.itsm.inter.WorkgroupInfoInterface;

/**
 * 表示个人信息
 * 
 * @author 梅园
 * 
 */
public class PersonInfo extends PersonInfoInterface {
	
	private String mobile;
	private String email;
	private String personStatus;

	/**
	 * 判断用户是否有管理此用户的权限<br>
	 * 默认条件：用户的管理员属性或系统定义的管理员与用户ID相同或为用户本身
	 * 
	 * @see com.hp.idc.itsm.ci.CIInfo#isAdmin(java.lang.String)
	 * @param id
	 *            用户ID
	 * @return 有权限返回true, 否则返回false
	 
	public boolean isAdmin(String id) {
		if (getId().equals(id))
			return true;
		return super.isAdmin(id);
	}*/

	/**
	 * 获取用户的手机号
	 * 
	 * @return 返回用户的手机号
	 */
	public String getMobile() {
		if (this.mobile!=null)
			return this.mobile;
		String ret = getAttribute("mobile");
		return (ret == null) ? "" : ret;
	}

	/**
	 * 获取用户的电子邮件
	 * 
	 * @return 返回用户的电子邮件
	 */
	public String getEmail() {
		if (this.email!=null)
			return this.email;
		String ret = getAttribute("email");
		return (ret == null) ? "" : ret;
	}

	/**
	 * 获取用户的密码
	 * 
	 * @return 返回用户的密码
	 */
	public String getPassword() {
		String ret = getAttribute("password");
		return (ret == null) ? "" : ret;
	}
	
	/**
	 * 获取密码最后更新时间
	 * @return 返回密码最后更新时间，没有设置过密码时返回null
	 
	public Date getPasswordChangeDate() {
		List l = this.getHistoryLines("password");
		String str = null;
		if (l.size() > 0) {
			HistoryLine line = (HistoryLine)l.get(l.size() - 1);
			str = line.getDate();
		}
		if (str == null)
			return null;
		Date d = null;
		try {
			d = new SimpleDateFormat("yyyyMMddHHmmss").parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return d;
	}
*/
	/**
	 * 获取用户的状态(如出差/请假等)
	 * 
	 * @return 返回用户的状态
	 */
	public String getPersonStatus() {
		if (this.personStatus!=null)
			return this.personStatus;
		String s = getAttribute("ci_person_status");
		FieldInfo f = FieldManager.getFieldById("ITSM","ci_person_status");
		if (f != null)
			s = f.getHtmlCode(s);
		return s;
	}

	/**
	 * 获取用户的类别(如员工/联系人等)
	 * 
	 * @return 返回用户的类别
	 
	public String getPersonCategory() {
		String s = getAttribute("ci_person_category");
		FieldInfo f = FieldManager.getFieldById("ITSM","ci_person_category");
		if (f != null)
			s = f.getHtmlCode(s);
		return s;
	}
*/

	public OrganizationInfoInterface getOrganization() {
		OrganizationInfoInterface oInfo= OrganizationManager.getOrganizationByPersonId("ITSM",id);
		return oInfo;
	}

	public List getWorkgroups() {
		return WorkgroupManager.getWorkgroupsByPersonId("ITSM",id);
	}

	public boolean isInWorkgroup(String groupId, boolean includeParent) {
		WorkgroupInfoInterface workgroup = WorkgroupManager.getWorkgroupById("ITSM",groupId);
		return WorkgroupManager.personIsInWorkgroup("ITSM",id, workgroup, includeParent);
	}

	public boolean isInOrganization(String orgaId, boolean includeParent) {
		OrganizationInfoInterface oInfo = OrganizationManager.getOrganizationById("ITSM",orgaId);
		return OrganizationManager.personIsInOrganization("ITSM",id, oInfo, includeParent);
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
		
	}

	public void setPassword(String password) {
		// TODO Auto-generated method stub
		
	}

	public void setPersonStatus(String personStatus) {
		this.personStatus = personStatus;
	}
	
	
}
