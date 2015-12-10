package com.hp.idc.resm.service;

import com.hp.idc.resm.model.AttributeDefine;

/**
 * ��Դ���Է���
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public interface IAttributeUpdateService {

	/**
	 * ��� / ������Դ���Զ���
	 * 
	 * @param attr
	 *            ��Դ����
	 * @param userId
	 *            �������û�id
	 * @throws Exception
	 *             ���������쳣ʱ����
	 */
	public void updateAttribute(AttributeDefine attr, int userId)
			throws Exception;

	/**
	 * ������Դ���ԡ��Ѿ�����ģ�͵����Խ�ֹ����ɾ����
	 * 
	 * @param id
	 *            ��Դ����id
	 * @param userId
	 *            �����û�id
	 * @throws Exception
	 *             ���������쳣���ֹɾ��ʱ������
	 */
	public void removeAttribute(String id, int userId) throws Exception;
}
