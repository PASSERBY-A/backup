/**
 * 
 */
package com.hp.idc.resm.expression;

import com.hp.idc.resm.resource.ResourceObject;

/**
 * ���ʽ�����Ի�ȡ���ݽӿ�
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 *
 */
public interface IAttributeExpression {

	/**
	 * ��ȡ��Դ������
	 * @param object ��Դ����
	 * @return ��Դ������
	 */
	public Object getAttribute(ResourceObject object);
}
