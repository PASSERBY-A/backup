package com.hp.idc.resm.service;

import com.hp.idc.resm.msgct.ResmMessageListener;
import com.hp.idc.context.util.ContextUtil;

/**
 * ��Դ�����ṩ�ķ���Ĺ�����
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public class ServiceManager {

	/**
	 * ��Դģ�ͷ���
	 */
	private static IModelService modelService = null;

	/**
	 * ��Դģ�͸��·���
	 */
	private static IModelUpdateService modelUpdateService = null;

	/**
	 * ��Դ�������
	 */
	private static IResourceService resourceService = null;

	/**
	 * ��Դ������·���
	 */
	private static IResourceUpdateService resourceUpdateService = null;

	/**
	 * ��Դ���Է���
	 */
	private static IAttributeService attributeService = null;

	/**
	 * ��Դ���Ը��·���
	 */
	private static IAttributeUpdateService attributeUpdateService = null;

	/**
	 * ������ϵ����
	 */
	private static IRelationService relationService = null;

	/**
	 * ������ϵ���·���
	 */
	private static IRelationUpdateService relationUpdateService = null;

	/**
	 * Ȩ�޷���
	 */
	private static IRoleService roleService = null;

	/**
	 * ��Ϣ�м������
	 */
	private static ResmMessageListener messageListener = null;

	/**
	 * ��ʼ��
	 */
	public void init() {
		if (ServiceManager.messageListener == null) {
			return;
		}
		ServiceManager.messageListener.startListener();
	}

	/**
	 * ��ȡ��Դģ�ͷ���
	 * 
	 * @return ��Դģ�ͷ���
	 */
	public static IModelService getModelService() {
		if (modelService == null)
			return (IModelService) ContextUtil.getBean("modelService");
		return modelService;
	}

	/**
	 * ������Դģ�ͷ��񣬴˷�������bean��ʼ��ʱ��ϵͳ�Զ����ã����÷������
	 * 
	 * @param modelService
	 *            ��Դģ�ͷ���
	 */
	public void setModelService(IModelService modelService) {
		ServiceManager.modelService = modelService;
	}

	/**
	 * ��ȡ��Դģ�͸��·���
	 * 
	 * @return ��Դģ�͸��·���
	 */
	public static IModelUpdateService getModelUpdateService() {
		if (modelUpdateService == null)
			return (IModelUpdateService) ContextUtil
					.getBean("modelUpdateService");
		return modelUpdateService;
	}

	/**
	 * ������Դģ�͸��·��񣬴˷�������bean��ʼ��ʱ��ϵͳ�Զ����ã����÷������
	 * 
	 * @param modelUpdateService
	 *            ��Դģ�͸��·���
	 */
	public void setModelUpdateService(IModelUpdateService modelUpdateService) {
		ServiceManager.modelUpdateService = modelUpdateService;
	}

	/**
	 * ��ȡ��Դ�������
	 * 
	 * @return ��Դ�������
	 */
	public static IResourceService getResourceService() {
		if (resourceService == null)
			return (IResourceService) ContextUtil.getBean("resourceService");
		return resourceService;
	}

	/**
	 * ������Դ������񣬴˷�������bean��ʼ��ʱ��ϵͳ�Զ����ã����÷������
	 * 
	 * @param resourceService
	 *            ��Դ�������
	 */
	public void setResourceService(IResourceService resourceService) {
		ServiceManager.resourceService = resourceService;
	}

	/**
	 * ��ȡ��Դ������·���
	 * 
	 * @return ��Դ������·���
	 */
	public static IResourceUpdateService getResourceUpdateService() {
		if (resourceUpdateService == null)
			return (IResourceUpdateService) ContextUtil
					.getBean("resourceUpdateService");
		return resourceUpdateService;
	}

	/**
	 * ������Դ������·��񣬴˷�������bean��ʼ��ʱ��ϵͳ�Զ����ã����÷������
	 * 
	 * @param resourceUpdateService
	 *            ��Դ������·���
	 */
	public void setResourceUpdateService(
			IResourceUpdateService resourceUpdateService) {
		ServiceManager.resourceUpdateService = resourceUpdateService;
	}

	/**
	 * ��ȡ��Դ���Է���
	 * 
	 * @return ��Դ���Է���
	 */
	public static IAttributeService getAttributeService() {
		if (attributeService == null)
			return (IAttributeService) ContextUtil.getBean("attributeService");
		return attributeService;
	}

	/**
	 * ������Դ���Է��񣬴˷�������bean��ʼ��ʱ��ϵͳ�Զ����ã����÷������
	 * 
	 * @param attributeService
	 *            ��Դ���Է���
	 */
	public void setAttributeService(IAttributeService attributeService) {
		ServiceManager.attributeService = attributeService;
	}

	/**
	 * ��ȡ��Դ���Ը��·���
	 * 
	 * @return ��Դ���Ը��·���
	 */
	public static IAttributeUpdateService getAttributeUpdateService() {
		if (attributeUpdateService == null)
			return (IAttributeUpdateService) ContextUtil
					.getBean("attributeUpdateService");
		return attributeUpdateService;
	}

	/**
	 * ������Դ���Ը��·��񣬴˷�������bean��ʼ��ʱ��ϵͳ�Զ����ã����÷������
	 * 
	 * @param attributeUpdateService
	 *            ��Դ���Ը��·���
	 */
	public void setAttributeUpdateService(
			IAttributeUpdateService attributeUpdateService) {
		ServiceManager.attributeUpdateService = attributeUpdateService;
	}

	/**
	 * ��ȡ������ϵ����
	 * 
	 * @return ������ϵ����
	 */
	public static IRelationService getRelationService() {
		if (relationService == null)
			return (IRelationService) ContextUtil.getBean("relationService");
		return relationService;
	}

	/**
	 * ���ù�����ϵ���񣬴˷�������bean��ʼ��ʱ��ϵͳ�Զ����ã����÷������
	 * 
	 * @param relationService
	 *            ������ϵ����
	 */
	public void setRelationService(IRelationService relationService) {
		ServiceManager.relationService = relationService;
	}

	/**
	 * ��ȡ������ϵ���·���
	 * 
	 * @return ������ϵ���·���
	 */
	public static IRelationUpdateService getRelationUpdateService() {
		if (relationUpdateService == null)
			return (IRelationUpdateService) ContextUtil
					.getBean("relationUpdateService");
		return relationUpdateService;
	}

	/**
	 * ���ù�����ϵ���·��񣬴˷�������bean��ʼ��ʱ��ϵͳ�Զ����ã����÷������
	 * 
	 * @param relationUpdateService
	 *            ������ϵ���·���
	 */
	public void setRelationUpdateService(
			IRelationUpdateService relationUpdateService) {
		ServiceManager.relationUpdateService = relationUpdateService;
	}

	/**
	 * ��ȡȨ�޷���
	 * 
	 * @return Ȩ�޷���
	 */
	public static IRoleService getRoleService() {
		if (roleService == null)
			return (IRoleService) ContextUtil.getBean("roleService");
		return roleService;
	}

	/**
	 * ����Ȩ�޷��񣬴˷�������bean��ʼ��ʱ��ϵͳ�Զ����ã����÷������
	 * 
	 * @param roleService
	 *            Ȩ�޷���
	 */
	public void setRoleService(IRoleService roleService) {
		ServiceManager.roleService = roleService;
	}

	/**
	 * ��ȡ��Ϣ�м������
	 * 
	 * @return ��Ϣ�м������
	 */
	public static ResmMessageListener getMessageListener() {
		if (messageListener == null)
			return (ResmMessageListener) ContextUtil
					.getBean("resmMessageListener");
		return messageListener;
	}

	/**
	 * ������Ϣ�м�����񣬴˷�������bean��ʼ��ʱ��ϵͳ�Զ����ã����÷������
	 * 
	 * @param messageListener
	 *            ��Ϣ�м������
	 */
	public void setMessageListener(ResmMessageListener messageListener) {
		ServiceManager.messageListener = messageListener;
	}

}
