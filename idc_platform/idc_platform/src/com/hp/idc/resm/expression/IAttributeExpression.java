/**
 * 
 */
package com.hp.idc.resm.expression;

import com.hp.idc.resm.resource.ResourceObject;

/**
 * 表达式型属性获取数据接口
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 *
 */
public interface IAttributeExpression {

	/**
	 * 获取资源的属性
	 * @param object 资源对象
	 * @return 资源的属性
	 */
	public Object getAttribute(ResourceObject object);
}
