package com.hp.idc.resm.service;

import java.util.List;

import com.hp.idc.resm.model.Model;
import com.hp.idc.resm.model.ModelAttribute;

/**
 * ��Դģ�ͷ���
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public interface IModelService {

	/**
	 * ��ȡ����ģ��
	 * 
	 * @param userId
	 *            �����û�id
	 * 
	 * @return ����ģ���б�
	 */
	public List<Model> getAllModels(int userId);

	/**
	 * ����id��ȡģ�ͣ�����Ȩ�޿���
	 * 
	 * @param id
	 *            ģ��id
	 * @return ��Դģ�Ͷ���
	 */
	public Model getModelById(String id);

	/**
	 * ����id��ȡ��ģ�ͣ�����Ȩ�޿���
	 * 
	 * @param id
	 *            ģ��id
	 * @return ��ģ�Ͷ���
	 */
	public Model getParentModelById(String id);

	/**
	 * ��ȡ��ģ�Ͷ���һ����
	 * 
	 * @param id
	 *            ģ��id
	 * @param userId
	 *            �����û�id
	 * @return ģ���Ӷ����б�
	 */
	public List<Model> getChildModelsById(String id, int userId);

	/**
	 * ��ȡ��ģ�Ͷ��󣨶༶��
	 * 
	 * @param id
	 *            ģ��id
	 * @param recursive
	 *            �ݹ��ѯ�����Ӽ�
	 * @param userId
	 *            �����û�id
	 * @return ģ���Ӷ����б�
	 */
	public List<Model> getChildModelsById(String id, boolean recursive,
			int userId);

	/**
	 * ����ģ��id��ȡģ�͵���������
	 * 
	 * @param id
	 *            ģ��id
	 * @return ģ�͵����������б�
	 */
	public List<ModelAttribute> getModelAttributesByModelId(String id);

	/**
	 * ��ȡģ�͵�ָ������
	 * 
	 * @param modeId
	 *            ģ��id
	 * @param attrId
	 *            ����id
	 * @return ģ������
	 */
	public ModelAttribute getModelAttribute(String modeId, String attrId);

	/**
	 * ���û�Ȩ�޶��б��е�ģ�Ͷ�����й���
	 * @param list ��ģ�Ͷ����б�
	 * @param userId �û�id
	 * @param type Ȩ������
	 * @return �û��з�����Ȩ��ģ�Ͷ���
	 * @see CachedModelService#PERM_ACCESS
	 * @see CachedModelService#PERM_CREATE_OBJECT
	 */
	public List<Model> filterByUser(List<Model> list, int userId, int type);
}
