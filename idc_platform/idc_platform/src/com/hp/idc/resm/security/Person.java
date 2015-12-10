/**
 * 
 */
package com.hp.idc.resm.security;

import java.util.ArrayList;
import java.util.List;

import com.hp.idc.resm.resource.AttributeBase;
import com.hp.idc.resm.resource.ResourceObject;
import com.hp.idc.resm.service.ServiceManager;
import com.hp.idc.resm.util.StringUtil;

/**
 * ��Ա����
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public class Person extends ResourceObject {

	/**
	 * ���л�id
	 */
	private static final long serialVersionUID = 3093800783403855142L;
	
	/**
	 * ϵͳ����Ա��id
	 */
	public static final int ADMIN_ID = 1;

	/**
	 * ��ȡ��Ա��¼id
	 * 
	 * @return ��¼id
	 */
	public String getLoginId() {
		return this.getAttributeValue("loginid");
	}

	/**
	 * ��ȡ��Ա����
	 * 
	 * @return ����(MD5)
	 */
	public String getPassword() {
		return this.getAttributeValue("password");
	}
	
	/**
	 * ��ȡ��Ա������֯
	 * @return ��Ա������֯����
	 */
	public Organization getOrganization() {
		List<ResourceObject> objects = this.getRelationObjects("belongto", "organization");
		if (objects.size() > 0)
			return (Organization)objects.get(0);
		return null;
	}

	/**
	 * �޸�����
	 * 
	 * @param old
	 *            ԭ����
	 * @param newPassword
	 *            ������
	 * @param userId
	 *            �û�id
	 * @return �ɹ�����null, ʧ�ܷ��ش���ԭ��
	 */
	public String modifyPassword(String old, String newPassword, int userId) {
		String pwd = this.getAttributeValue("password");
		if (pwd != null && pwd.length() > 0) { // ���ù�����
			String md5 = StringUtil.MD5(old);
			if (!pwd.equals(md5)) {
				return "ԭ���벻ƥ��";
			}
		}
		if (newPassword.equals(old))
			return "�������벻����ͬ";
		
		Person p;
		try {
			p = (Person)clone();
			AttributeBase ab = this.createAttribute("password");
			ab.setText(StringUtil.MD5(newPassword));
			List<AttributeBase> list = new ArrayList<AttributeBase>();
			list.add(ab);
			this.update(list, userId, false);
			ServiceManager.getResourceUpdateService().updateResource(p, userId);
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
		return null;
	}
	
	@Override
	public List<AttributeBase> getHeader() {
		return getHeader(new String[] { "id", "name", "email",
				"mobile", "organization" });
	}
	
	@Override
	public boolean isMatch(String str) {
		if (super.isMatch(str))
			return true;
		if (str.length() >= 4) {
			String mobile = this.getAttributeValue("mobile");
			if (mobile != null && mobile.endsWith(str))
				return true;
		}
		String email = this.getAttributeValue("email");
		if (email != null && email.startsWith(str + "@")) {
			return true;
		}
		Organization org = this.getOrganization();
		if (org != null && org.getName().equals(str))
			return true;
		return false;
	}
	
	@Override
	public String getMatchDescription() {
		return "ID��ͬ��������ģ��ƥ�䣬�����ֻ�����βƥ�䣨����4λ����������@ǰ��ƥ�䣬���߲�����ͬ";
	}
}
