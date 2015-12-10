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
 * 人员对象
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public class Person extends ResourceObject {

	/**
	 * 序列化id
	 */
	private static final long serialVersionUID = 3093800783403855142L;
	
	/**
	 * 系统管理员的id
	 */
	public static final int ADMIN_ID = 1;

	/**
	 * 获取人员登录id
	 * 
	 * @return 登录id
	 */
	public String getLoginId() {
		return this.getAttributeValue("loginid");
	}

	/**
	 * 获取人员密码
	 * 
	 * @return 密码(MD5)
	 */
	public String getPassword() {
		return this.getAttributeValue("password");
	}
	
	/**
	 * 获取人员所属组织
	 * @return 人员所属组织对象
	 */
	public Organization getOrganization() {
		List<ResourceObject> objects = this.getRelationObjects("belongto", "organization");
		if (objects.size() > 0)
			return (Organization)objects.get(0);
		return null;
	}

	/**
	 * 修改密码
	 * 
	 * @param old
	 *            原密码
	 * @param newPassword
	 *            新密码
	 * @param userId
	 *            用户id
	 * @return 成功返回null, 失败返回错误原因
	 */
	public String modifyPassword(String old, String newPassword, int userId) {
		String pwd = this.getAttributeValue("password");
		if (pwd != null && pwd.length() > 0) { // 设置过密码
			String md5 = StringUtil.MD5(old);
			if (!pwd.equals(md5)) {
				return "原密码不匹配";
			}
		}
		if (newPassword.equals(old))
			return "新老密码不能相同";
		
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
		return "ID相同，或姓名模糊匹配，或者手机按结尾匹配（至少4位），或按邮箱@前方匹配，或者部门相同";
	}
}
