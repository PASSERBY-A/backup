/**
 * 
 */
package com.hp.idc.resm.expression;

import java.io.Serializable;

import com.hp.idc.resm.resource.ResourceObject;
import com.hp.idc.resm.security.Person;

/**
 * 根据人员获取所属部门
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 *
 */
public class PersonOrganizationExpression implements IAttributeExpression, Serializable {

	/**
	 * 序列化id
	 */
	private static final long serialVersionUID = 7326456061988541582L;

	public Object getAttribute(ResourceObject object) {
		if (object instanceof Person) {
			return ((Person) object).getOrganization();
		}
		return null;
	}
}
