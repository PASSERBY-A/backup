package com.hp.idc.resm.service;

import com.hp.idc.resm.msgct.ResmMessageListener;
import com.hp.idc.context.util.ContextUtil;

/**
 * 资源管理提供的服务的管理类
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public class ServiceManager {

	/**
	 * 资源模型服务
	 */
	private static IModelService modelService = null;

	/**
	 * 资源模型更新服务
	 */
	private static IModelUpdateService modelUpdateService = null;

	/**
	 * 资源对象服务
	 */
	private static IResourceService resourceService = null;

	/**
	 * 资源对象更新服务
	 */
	private static IResourceUpdateService resourceUpdateService = null;

	/**
	 * 资源属性服务
	 */
	private static IAttributeService attributeService = null;

	/**
	 * 资源属性更新服务
	 */
	private static IAttributeUpdateService attributeUpdateService = null;

	/**
	 * 关联关系服务
	 */
	private static IRelationService relationService = null;

	/**
	 * 关联关系更新服务
	 */
	private static IRelationUpdateService relationUpdateService = null;

	/**
	 * 权限服务
	 */
	private static IRoleService roleService = null;

	/**
	 * 消息中间件服务
	 */
	private static ResmMessageListener messageListener = null;

	/**
	 * 初始化
	 */
	public void init() {
		if (ServiceManager.messageListener == null) {
			return;
		}
		ServiceManager.messageListener.startListener();
	}

	/**
	 * 获取资源模型服务
	 * 
	 * @return 资源模型服务
	 */
	public static IModelService getModelService() {
		if (modelService == null)
			return (IModelService) ContextUtil.getBean("modelService");
		return modelService;
	}

	/**
	 * 设置资源模型服务，此方法会在bean初始化时由系统自动调用，设置服务对象
	 * 
	 * @param modelService
	 *            资源模型服务
	 */
	public void setModelService(IModelService modelService) {
		ServiceManager.modelService = modelService;
	}

	/**
	 * 获取资源模型更新服务
	 * 
	 * @return 资源模型更新服务
	 */
	public static IModelUpdateService getModelUpdateService() {
		if (modelUpdateService == null)
			return (IModelUpdateService) ContextUtil
					.getBean("modelUpdateService");
		return modelUpdateService;
	}

	/**
	 * 设置资源模型更新服务，此方法会在bean初始化时由系统自动调用，设置服务对象
	 * 
	 * @param modelUpdateService
	 *            资源模型更新服务
	 */
	public void setModelUpdateService(IModelUpdateService modelUpdateService) {
		ServiceManager.modelUpdateService = modelUpdateService;
	}

	/**
	 * 获取资源对象服务
	 * 
	 * @return 资源对象服务
	 */
	public static IResourceService getResourceService() {
		if (resourceService == null)
			return (IResourceService) ContextUtil.getBean("resourceService");
		return resourceService;
	}

	/**
	 * 设置资源对象服务，此方法会在bean初始化时由系统自动调用，设置服务对象
	 * 
	 * @param resourceService
	 *            资源对象服务
	 */
	public void setResourceService(IResourceService resourceService) {
		ServiceManager.resourceService = resourceService;
	}

	/**
	 * 获取资源对象更新服务
	 * 
	 * @return 资源对象更新服务
	 */
	public static IResourceUpdateService getResourceUpdateService() {
		if (resourceUpdateService == null)
			return (IResourceUpdateService) ContextUtil
					.getBean("resourceUpdateService");
		return resourceUpdateService;
	}

	/**
	 * 设置资源对象更新服务，此方法会在bean初始化时由系统自动调用，设置服务对象
	 * 
	 * @param resourceUpdateService
	 *            资源对象更新服务
	 */
	public void setResourceUpdateService(
			IResourceUpdateService resourceUpdateService) {
		ServiceManager.resourceUpdateService = resourceUpdateService;
	}

	/**
	 * 获取资源属性服务
	 * 
	 * @return 资源属性服务
	 */
	public static IAttributeService getAttributeService() {
		if (attributeService == null)
			return (IAttributeService) ContextUtil.getBean("attributeService");
		return attributeService;
	}

	/**
	 * 设置资源属性服务，此方法会在bean初始化时由系统自动调用，设置服务对象
	 * 
	 * @param attributeService
	 *            资源属性服务
	 */
	public void setAttributeService(IAttributeService attributeService) {
		ServiceManager.attributeService = attributeService;
	}

	/**
	 * 获取资源属性更新服务
	 * 
	 * @return 资源属性更新服务
	 */
	public static IAttributeUpdateService getAttributeUpdateService() {
		if (attributeUpdateService == null)
			return (IAttributeUpdateService) ContextUtil
					.getBean("attributeUpdateService");
		return attributeUpdateService;
	}

	/**
	 * 设置资源属性更新服务，此方法会在bean初始化时由系统自动调用，设置服务对象
	 * 
	 * @param attributeUpdateService
	 *            资源属性更新服务
	 */
	public void setAttributeUpdateService(
			IAttributeUpdateService attributeUpdateService) {
		ServiceManager.attributeUpdateService = attributeUpdateService;
	}

	/**
	 * 获取关联关系服务
	 * 
	 * @return 关联关系服务
	 */
	public static IRelationService getRelationService() {
		if (relationService == null)
			return (IRelationService) ContextUtil.getBean("relationService");
		return relationService;
	}

	/**
	 * 设置关联关系服务，此方法会在bean初始化时由系统自动调用，设置服务对象
	 * 
	 * @param relationService
	 *            关联关系服务
	 */
	public void setRelationService(IRelationService relationService) {
		ServiceManager.relationService = relationService;
	}

	/**
	 * 获取关联关系更新服务
	 * 
	 * @return 关联关系更新服务
	 */
	public static IRelationUpdateService getRelationUpdateService() {
		if (relationUpdateService == null)
			return (IRelationUpdateService) ContextUtil
					.getBean("relationUpdateService");
		return relationUpdateService;
	}

	/**
	 * 设置关联关系更新服务，此方法会在bean初始化时由系统自动调用，设置服务对象
	 * 
	 * @param relationUpdateService
	 *            关联关系更新服务
	 */
	public void setRelationUpdateService(
			IRelationUpdateService relationUpdateService) {
		ServiceManager.relationUpdateService = relationUpdateService;
	}

	/**
	 * 获取权限服务
	 * 
	 * @return 权限服务
	 */
	public static IRoleService getRoleService() {
		if (roleService == null)
			return (IRoleService) ContextUtil.getBean("roleService");
		return roleService;
	}

	/**
	 * 设置权限服务，此方法会在bean初始化时由系统自动调用，设置服务对象
	 * 
	 * @param roleService
	 *            权限服务
	 */
	public void setRoleService(IRoleService roleService) {
		ServiceManager.roleService = roleService;
	}

	/**
	 * 获取消息中间件服务
	 * 
	 * @return 消息中间件服务
	 */
	public static ResmMessageListener getMessageListener() {
		if (messageListener == null)
			return (ResmMessageListener) ContextUtil
					.getBean("resmMessageListener");
		return messageListener;
	}

	/**
	 * 设置消息中间件服务，此方法会在bean初始化时由系统自动调用，设置服务对象
	 * 
	 * @param messageListener
	 *            消息中间件服务
	 */
	public void setMessageListener(ResmMessageListener messageListener) {
		ServiceManager.messageListener = messageListener;
	}

}
