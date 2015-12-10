package com.hp.idc.resm.service;

import java.util.List;

import com.hp.idc.resm.model.ModelRelation;
import com.hp.idc.resm.model.RelationDefine;
import com.hp.idc.resm.resource.ResourceRelation;

/**
 * ������ϵ����
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public interface IRelationService {

	/**
	 * ��ȡ��ϵ����
	 * 
	 * @param id
	 *            ��ϵid
	 * @return ��ϵ����
	 */
	public RelationDefine getRelationDefineById(String id);

	/**
	 * ��ȡ���й�ϵ����
	 * 
	 * @return ���й�ϵ����
	 */
	public List<RelationDefine> getAllRelationDefines();

	/**
	 * ��ȡģ�͹�ϵ
	 * 
	 * @param id
	 *            ģ�͹�ϵid
	 * @return ģ�͹�ϵ����
	 */
	public ModelRelation getModelRelationById(int id);

	/**
	 * ��ȡָ��ģ�͵����й�ϵ
	 * 
	 * @param id
	 *            ģ��id
	 * @return ģ�͹�ϵ�б�
	 */
	public List<ModelRelation> getModelRelationsByModelId(String id);

	/**
	 * ��ȡ����ģ�͹�ϵ
	 * 
	 * @return ����ģ�͹�ϵ
	 */
	public List<ModelRelation> getAllModelRelations();

	/**
	 * ��ȡ��Դ����Ĺ�����ϵ
	 * 
	 * @param id
	 *            ��Դ����id
	 * 
	 * @return ��Դ����Ĺ�����ϵ
	 */
	public List<ResourceRelation> getRelationsByResourceId(int id);

}
