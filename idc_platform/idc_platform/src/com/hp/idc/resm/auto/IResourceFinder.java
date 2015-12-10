package com.hp.idc.resm.auto;

import java.util.List;

import com.hp.idc.resm.model.Model;
import com.hp.idc.resm.model.Parameter;

/**
 * �������Զ����ֲ���Ļ�����Ϣ��ÿ���������̳д���Ϣ
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public interface IResourceFinder {

	/**
	 * ��ȡ�������
	 * 
	 * @return �������
	 */
	public String getName();

	/**
	 * ��ȡ�����Ҫ�Ĳ���
	 * 
	 * @return �����Ҫ�Ĳ����б�
	 */
	public List<Parameter> getParameters();

	/**
	 * ��ȡ���Ӧ�õ���ģ��
	 * 
	 * @return ���Ӧ�õ���ģ���б�
	 */
	public List<Model> getApplyModels();

	/**
	 * ָʾ�Ƿ�����Զ�����
	 * 
	 * @return trueΪ�Զ����У�falseΪ�ֶ�����
	 */
	public boolean isAutoRun();

	/**
	 * ��ȡ�Զ�����ʱ�䶨��
	 * 
	 * @return �Զ�����ʱ�䶨��
	 */
	public String getAutoRunDefine();

	/**
	 * ������
	 */
	public void process();
}
