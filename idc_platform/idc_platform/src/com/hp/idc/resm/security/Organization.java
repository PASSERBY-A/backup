/**
 * 
 */
package com.hp.idc.resm.security;

import java.util.ArrayList;
import java.util.List;

import com.hp.idc.resm.resource.ReferenceAttribute;
import com.hp.idc.resm.resource.ResourceObject;

/**
 * 组织对象
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public class Organization extends ResourceObject {

	/**
	 * 序列化id
	 */
	private static final long serialVersionUID = -6903691835068528508L;

	/**
	 * 获取组织下的所有人员
	 * @return 人员列表
	 */
	public List<Person> getPersons() {
		List<ResourceObject> objects = this.getRelationObjects("belongto", "person");
		List<Person> list = new ArrayList<Person>();
		for (ResourceObject r : objects) {
			if (r instanceof Person)
				list.add((Person)r);
		}
		return list;
	}
	
	/**
	 * 获取上级组织
	 * @return 上级组织，没有时，返回null
	 */
	public Organization getParentOrganization() {
		ReferenceAttribute ab = (ReferenceAttribute)this.getAttribute("parent_org");
		//return (Organization)ab.getValueAsObject();
		return (Organization)ab.getValueOfResouceObject();
	}
	
	/**
	 * 判断是否是某个组织的子组织
	 * @param orgId 组织id
	 * @return true=是，false=否
	 */
	public boolean isChildOf(int orgId) {
		for (Organization m = this; m != null; m = m.getParentOrganization()) {
			if (orgId == m.id)
				return true;
		}
		return false;
	}
	
	/**
	 * 判断是否是某些组织的子组织
	 * @param orgIds 组织id列表
	 * @return true=是，false=否
	 */
	public boolean isChildOf(int[] orgIds) {
		for (Organization m = this; m != null; m = m.getParentOrganization()) {
			for (int orgId : orgIds) {
				if (orgId == m.id)
					return true;
			}
		}
		return false;
	}
}
