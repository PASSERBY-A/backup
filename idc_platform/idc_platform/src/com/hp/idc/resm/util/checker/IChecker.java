package com.hp.idc.resm.util.checker;

/**
 * �������ӿ�
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public interface IChecker {

	/**
	 * ����ֵ�Ƿ�Ϸ�
	 * 
	 * @param value
	 *            ��ֵ
	 * @return �Ƿ���ԭ���������null�����ʾ����ͨ��
	 */
	public String isValid(String attrName, String value);
}
