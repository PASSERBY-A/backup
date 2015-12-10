package com.hp.idc.resm.service;

import java.util.List;

import com.hp.idc.resm.model.AttributeDefine;
import com.hp.idc.resm.model.AttributeType;
import com.hp.idc.resm.model.Code;
import com.hp.idc.resm.model.ModelAttribute;

/**
 * ��Դ���Է���
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public interface IAttributeService {

	/**
	 * ����id��ȡ���Զ���
	 * 
	 * @param id
	 *            ����id
	 * @return ���Զ���
	 */
	public AttributeDefine getAttributeById(String id);

	/**
	 * ��ȡ��������
	 * 
	 * @return ���������б�
	 */
	public List<AttributeDefine> getAllAttributes();

	/**
	 * ��ȡ����ά��
	 * 
	 * @return ����ά�������б�
	 */
	public List<AttributeDefine> getAllDimensions();

	/**
	 * ��ȡģ���µ�ά��
	 * 
	 * @param modelId
	 *            ģ��id
	 * @return ά�������б�
	 */
	public List<ModelAttribute> getDimensionsByModelId(String modelId);

	/**
	 * ��ȡ���е���������
	 * 
	 * @return ���е����������б�
	 */
	public List<AttributeType> getAllAttributeTypes();

	/**
	 * ����id��ȡ��Դ��������
	 * 
	 * @param id
	 *            ��Դ��������id
	 * @return ��Դ��������
	 */
	public AttributeType getAttributeTypeById(String id);

	/**
	 * ��ȡ����parentModelId����������
	 * 
	 * @param parentModelId
	 *            ģ��ID
	 * @return �����б�
	 */
	public List<AttributeDefine> getAttributesForAdd(String parentModelId);
	
	/**
	 * ��ȡ���д���
	 * @return �����б�
	 */
	public List<Code> getAllCodes();

}
