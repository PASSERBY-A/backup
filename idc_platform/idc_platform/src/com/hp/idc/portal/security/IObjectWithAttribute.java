package com.hp.idc.portal.security;

/**
 * ��ʾ�����ԵĶ���ӿ�
 * 
 * @author ÷԰
 * 
 */
public interface IObjectWithAttribute {

	/**
	 * ���� id ��ѯ���������
	 * 
	 * @param id
	 *            ��ѯ��ʶ
	 * @return ����ֵ���Ҳ���ʱ���� null
	 */
	public String getAttribute(String id);
}
