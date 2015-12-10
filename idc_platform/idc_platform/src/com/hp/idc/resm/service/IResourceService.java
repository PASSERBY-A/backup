package com.hp.idc.resm.service;

import java.util.List;
import java.util.Map;

import com.hp.idc.resm.resource.AttributeBase;
import com.hp.idc.resm.resource.ResourceObject;
import com.hp.idc.resm.ui.PageInfo;
import com.hp.idc.resm.ui.PageQueryInfo;

/**
 * ��Դ�������ӿ�
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public interface IResourceService {

	/**
	 * ����Դid��ȡ��Դ��Ϣ������Ȩ�޿���
	 * 
	 * @param id
	 *            ��Դid
	 * @return ��Դ����
	 */
	public ResourceObject getResourceById(int id);
	
	/**
	 * ����Դid��ȡ�����Դ��Ϣ������Ȩ�޿���
	 * @param id ��","�ָ���id�ַ���
	 * @return �����Դ��Ϣ
	 */
	public List<ResourceObject> getResourcesById(String id);
	
	/**
	 * ��ȡ��Դ���е����ԣ���Ҫ��flex���ã�����Ӧ��ֱ��ͨ����Դ����ķ�������ȡ������ֵ
	 * @param id ��Դid
	 * @return ��Դ���е����ԣ���ģ���ж���ģ�
	 */
	public List<AttributeBase> getResourceAttributes(int id);

	/**
	 * ��ȡ������Դ��Ϣ
	 * 
	 * @param userId �����û�id
	 * @return ������Դ����
	 */
	public List<ResourceObject> getAllResources(int userId);

	/**
	 * ������Դģ��id��ȡ������Դ
	 * 
	 * @param id
	 *            ģ��id
	 * @param userId �����û�id
	 * @return ��Դ�����б�
	 */
	public List<ResourceObject> getResourcesByModelId(String id, int userId);

	/**
	 * ������Դģ��id��ȡ������Դ
	 * 
	 * @param id
	 *            ģ��id
	 * @param includeChilds
	 *            �Ƿ������ģ���µĶ���
	 * @param userId �����û�id
	 * @return ��Դ�����б�
	 */
	public List<ResourceObject> getResourcesByModelId(String id,
			boolean includeChilds, int userId);

	/**
	 * ���ݱ��ʽ������Դ
	 * 
	 * @param expr
	 *            ���ʽ����ʽ��javaд����${attrId}��ʾ����ֵ������
	 *            ��${attrId}.compareTo("") > 0 && ${attrId} == "abc"֮���д��
	 * @param userId �����û�id
	 * @return ƥ�����Դ�б�
	 * @throws Exception
	 *             ���ʽִ��ʧ��ʱ����
	 */
	public List<ResourceObject> findResource(String expr, int userId) throws Exception;
	
	/**
	 * ���ݱ��ʽ������Դ
	 * @param ll ��Ҫ���ҵ���Դ����
	 * @param expr
	 *            ���ʽ����ʽ��javaд����${attrId}��ʾ����ֵ������
	 *            ��${attrId}.compareTo("") > 0 && ${attrId} == "abc"֮���д��
	 * @param userId �����û�id
	 * @return ƥ�����Դ�б�
	 * @throws Exception
	 *             ���ʽִ��ʧ��ʱ����
	 */
	public List<ResourceObject> findResource(List<ResourceObject> ll,String expr, int userId) throws Exception;
	
	/**
	 * ��������ֵ��Ψһ����������Դ���󣬲���Ȩ�޿���
	 * @param attrId ����id
	 * @param value ����ֵ
	 * @return ��Դ�����Ҳ���ʱ����null
	 * @throws Exception ����ʧ�ܣ����ҵ��Ķ���Ψһʱ����
	 */
	public ResourceObject getResourceByAttribute(String attrId, String value) throws Exception;
	
	/**
	 * ��ȡ��Դ�����б�
	 * @param modelId ģ��id
	 * @param str ģ�������ַ���
	 * @param queryInfo ��ѯ��Ϣ
	 * @param userId �����û�id
	 * @return ��Դ�����б�
	 */
	public PageInfo<ResourceObject> listResource(String modelId, String str, PageQueryInfo queryInfo, int userId);
	
	/**
	 * ��ȡ��Դ�����б�
	 * @param modelId ģ��id
	 * @param recursion �Ƿ�ݹ��ѯ
	 * @param str ģ�������ַ���
	 * @param queryInfo ��ѯ��Ϣ
	 * @param userId �����û�id
	 * @return ��Դ�����б�
	 */
	public PageInfo<ResourceObject> listResource(String modelId, boolean recursion, String str, PageQueryInfo queryInfo, int userId);
	
	/**
	 * ��ȡ��Դ�����б�
	 * @param modelId ģ��id
	 * @param recursion �Ƿ�ݹ��ѯ
	 * @param paramMap ������� {attributeId ==> attributeValue}
	 * @param queryInfo ��ѯ��Ϣ
	 * @param userId �����û�id
	 * @return ��Դ�����б�
	 */
	public PageInfo<ResourceObject> listResource(String modelId, boolean recursion, Map<String,Object> paramMap, PageQueryInfo queryInfo, int userId);

	/**
	 * ���û�Ȩ�޶��б��е���Դ������й���
	 * 
	 * @param list
	 *            ��Դ�����б�
	 * @param userId
	 *            �û�id
	 * @return �û��з�����Ȩ����Դ����
	 */
	public List<ResourceObject> filterByUser(List<ResourceObject> list,
			int userId);
	
	/**
	 * ���û�Ȩ�޶��б��е���Դ������й���
	 * 
	 * @param idArray
	 *            ��Դ����id����
	 * @param userId
	 *            �û�id
	 * @return �û��з�����Ȩ����Դ����
	 */
	public int[] filterByUser(int[] idArray,
			int userId);
	
	/**
	 * ����û�����Դ����ķ���Ȩ��
	 * 
	 * @param id
	 *            ��Դ����id
	 * @param userId
	 *            �û�id
	 * @return true=���Է��ʣ�false=�����Է���
	 */
	public boolean hasAccessPermission(int id,
			int userId);

	/**
	 * 	/**
	 * ��ȡָ��ģ����, ָ�����ʽ����Դ�б�
	 * @param modelId ָ��ģ��ID
	 * @param exp ���ʽ
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public List<ResourceObject> getResourceByAttribute(String modelId, String exp, int userId) throws Exception;
}
