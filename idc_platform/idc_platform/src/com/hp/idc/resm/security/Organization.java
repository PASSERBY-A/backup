/**
 * 
 */
package com.hp.idc.resm.security;

import java.util.ArrayList;
import java.util.List;

import com.hp.idc.resm.resource.ReferenceAttribute;
import com.hp.idc.resm.resource.ResourceObject;

/**
 * ��֯����
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public class Organization extends ResourceObject {

	/**
	 * ���л�id
	 */
	private static final long serialVersionUID = -6903691835068528508L;

	/**
	 * ��ȡ��֯�µ�������Ա
	 * @return ��Ա�б�
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
	 * ��ȡ�ϼ���֯
	 * @return �ϼ���֯��û��ʱ������null
	 */
	public Organization getParentOrganization() {
		ReferenceAttribute ab = (ReferenceAttribute)this.getAttribute("parent_org");
		//return (Organization)ab.getValueAsObject();
		return (Organization)ab.getValueOfResouceObject();
	}
	
	/**
	 * �ж��Ƿ���ĳ����֯������֯
	 * @param orgId ��֯id
	 * @return true=�ǣ�false=��
	 */
	public boolean isChildOf(int orgId) {
		for (Organization m = this; m != null; m = m.getParentOrganization()) {
			if (orgId == m.id)
				return true;
		}
		return false;
	}
	
	/**
	 * �ж��Ƿ���ĳЩ��֯������֯
	 * @param orgIds ��֯id�б�
	 * @return true=�ǣ�false=��
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
