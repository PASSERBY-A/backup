package com.hp.idc.resm.service;

import java.util.List;

import com.hp.idc.resm.model.Model;
import com.hp.idc.resm.model.ModelAttribute;

/**
 * ��Դģ�͸��·���
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public interface IModelUpdateService {
	/**
	 * ��� / ����ģ��
	 * 
	 * @param model
	 *            ��Դģ�Ͷ���
	 * @param attributes
	 *            ��Դģ������
	 * @param userId
	 *            �������û�id
	 * @throws Exception
	 *             ���������쳣ʱ����
	 */
	public void updateModel(Model model, List<ModelAttribute> attributes,
			int userId) throws Exception;

	/**
	 * ɾ��ģ��
	 * 
	 * @param modelId
	 *            ��Դģ��id
	 * @param userId
	 *            �������û�id
	 * @throws Exception
	 *             ���������쳣ʱ����
	 */
	public void removeModel(String modelId, int userId) throws Exception;
}
