/**
 * 
 */
package com.hp.idc.resm.resource.rule;

import java.util.List;

import org.dom4j.Element;

import com.hp.idc.resm.resource.AttributeBase;
import com.hp.idc.resm.resource.ResourceObject;

/**
 * 定义资源更新和验证的接口
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public interface IResourceRule {

	/**
	 * 更新资源时调用
	 * 
	 * @param object
	 *            资源对象
	 * @param attributes
	 *            有变化的属性
	 * @param el
	 *            更新的数据节点
	 * @param userId
	 *            更新用户id
	 * @throws Exception
	 *             有异常时发生
	 */
	public void update(ResourceObject object, List<AttributeBase> attributes,
			Element el, int userId) throws Exception;

	/**
	 * 对资源进行数据较验
	 * 
	 * @param object
	 *            资源对象
	 * @throws Exception
	 *             验证不通过时发生
	 */
	public void valid(ResourceObject object) throws Exception;

}
