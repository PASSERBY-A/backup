package com.hp.idc.resm.service;

import java.util.List;
import java.util.Map;

import com.hp.idc.resm.resource.AttributeBase;
import com.hp.idc.resm.resource.ResourceObject;

/**
 * ��Դ���������½ӿ�
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public interface IResourceUpdateService {

	/**
	 * ͬ����Դ����ϸ����
	 * 
	 * @param id
	 *            ��Դid
	 */
	public void syncResource(int id);

	/**
	 * ������Դ
	 * 
	 * @param ro
	 *            ��Դ����
	 * @param userId
	 *            �������û�id
	 * @throws Exception
	 *             �������쳣ʱ����
	 */
	public void updateResource(ResourceObject ro, int userId) throws Exception;

	/**
	 * ������Դ����
	 * 
	 * @param id
	 *            ��Դ����id
	 * @param list
	 *            �����б�
	 * @param userId
	 *            �������û�id
	 * @throws Exception
	 *             �������쳣ʱ����
	 */
	public void updateResource(int id, List<AttributeBase> list, int userId)
			throws Exception;

	/**
	 * �����Դ����
	 * 
	 * @param modelId
	 *            ��Դģ��id
	 * @param list
	 *            �����б�
	 * @param userId
	 *            �������û�id
	 * @return ��Դid
	 * @throws Exception
	 *             �������쳣ʱ����
	 */
	public int addResource(String modelId, List<AttributeBase> list, int userId)
			throws Exception;

	/**
	 * �����Դ����
	 * 
	 * @param modelId
	 *            ��Դģ��id
	 * @param attributes
	 *            ���ԣ�����id->����ֵ
	 * @param userId
	 *            �������û�id
	 * @return ��Դid
	 * @throws Exception
	 *             �������쳣ʱ����
	 */
	public int addResource(String modelId, Map<String, String> attributes,
			int userId) throws Exception;

	/**
	 * ɾ����Դ����Դɾ����������ݿ����Ƴ���¼��ֻ�ǽ�enabled�ֶδ��ϱ�ǡ�
	 * 
	 * @param id
	 *            ��Դid
	 * @param userId
	 *            �������û�id
	 * @throws Exception
	 *             �������쳣ʱ����
	 */
	public void deleteResource(int id, int userId) throws Exception;

	/**
	 * ������Դ״̬
	 * 
	 * @param id
	 *            ��Դid
	 * @param status
	 *            ��Դ�µ�״̬
	 * @param custormerId
	 * 				�ͻ����
	 * @param orderId
	 * 			�������
	 * @param userId
	 *            �������û�id
	 * @throws Exception
	 */
	public void updateResource(int id, int userId, Map<String,String> attributes)
			throws Exception;
}
