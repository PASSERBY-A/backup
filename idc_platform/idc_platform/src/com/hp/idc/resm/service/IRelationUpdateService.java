/**
 * 
 */
package com.hp.idc.resm.service;

import java.util.List;

import com.hp.idc.resm.model.ModelRelation;
import com.hp.idc.resm.resource.ResourceRelation;

/**
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public interface IRelationUpdateService {

	/**
	 * ������Դ�Ĺ�����ϵ��
	 * 
	 * @param id
	 *            ��Դ�����id
	 * @param relationList
	 *            ��Դ�����ȫ��������ϵ�������б��еĹ�����ϵ�����Ƴ���
	 * @param userId
	 *            �����û���id
	 * @throws Exception
	 *             �������쳣ʱ����
	 */
	public void updateResourceRelation(int id,
			List<ResourceRelation> relationList, int userId) throws Exception;

	/**
	 * ������Դ�Ĺ�����ϵ��
	 * 
	 * @param relation
	 *            ��Դ����Ĺ�����ϵ
	 * @param userId
	 *            �����û���id
	 * @throws Exception
	 *             �������쳣ʱ����
	 */
	public void addResourceRelation(
			ResourceRelation relation, int userId) throws Exception;
	
	/**
	 * ����ģ�͹�����ϵ
	 * 
	 * @param relation
	 *            ������ϵ����
	 * @param userId
	 *            �����û���id
	 * @throws Exception
	 *             �������쳣ʱ����
	 */
	public void addModelRelation(ModelRelation relation, int userId)
			throws Exception;

	/**
	 * ɾ��ģ�͹�����ϵ
	 * 
	 * @param id
	 *            ������ϵid
	 * @param userId
	 *            �����û���id
	 * @throws Exception
	 *             �������쳣ʱ������������ɾ��������ϵ���ж����Ѿ������˹�����ϵ��
	 */
	public void removeModelRelation(int id, int userId) throws Exception;
}
