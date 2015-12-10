package com.hp.idc.itsm.security;

import java.util.List;

import com.hp.idc.itsm.configure.FieldInfo;
import com.hp.idc.itsm.configure.FieldManager;
import com.hp.idc.itsm.inter.OrganizationInfoInterface;
import com.hp.idc.itsm.inter.PersonInfoInterface;
import com.hp.idc.itsm.inter.WorkgroupInfoInterface;

/**
 * ��ʾ������Ϣ
 * 
 * @author ÷԰
 * 
 */
public class PersonInfo extends PersonInfoInterface {
	
	private String mobile;
	private String email;
	private String personStatus;

	/**
	 * �ж��û��Ƿ��й�����û���Ȩ��<br>
	 * Ĭ���������û��Ĺ���Ա���Ի�ϵͳ����Ĺ���Ա���û�ID��ͬ��Ϊ�û�����
	 * 
	 * @see com.hp.idc.itsm.ci.CIInfo#isAdmin(java.lang.String)
	 * @param id
	 *            �û�ID
	 * @return ��Ȩ�޷���true, ���򷵻�false
	 
	public boolean isAdmin(String id) {
		if (getId().equals(id))
			return true;
		return super.isAdmin(id);
	}*/

	/**
	 * ��ȡ�û����ֻ���
	 * 
	 * @return �����û����ֻ���
	 */
	public String getMobile() {
		if (this.mobile!=null)
			return this.mobile;
		String ret = getAttribute("mobile");
		return (ret == null) ? "" : ret;
	}

	/**
	 * ��ȡ�û��ĵ����ʼ�
	 * 
	 * @return �����û��ĵ����ʼ�
	 */
	public String getEmail() {
		if (this.email!=null)
			return this.email;
		String ret = getAttribute("email");
		return (ret == null) ? "" : ret;
	}

	/**
	 * ��ȡ�û�������
	 * 
	 * @return �����û�������
	 */
	public String getPassword() {
		String ret = getAttribute("password");
		return (ret == null) ? "" : ret;
	}
	
	/**
	 * ��ȡ����������ʱ��
	 * @return ��������������ʱ�䣬û�����ù�����ʱ����null
	 
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
	 * ��ȡ�û���״̬(�����/��ٵ�)
	 * 
	 * @return �����û���״̬
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
	 * ��ȡ�û������(��Ա��/��ϵ�˵�)
	 * 
	 * @return �����û������
	 
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
