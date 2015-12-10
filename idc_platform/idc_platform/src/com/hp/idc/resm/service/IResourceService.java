package com.hp.idc.resm.service;

import java.util.List;
import java.util.Map;

import com.hp.idc.resm.resource.AttributeBase;
import com.hp.idc.resm.resource.ResourceObject;
import com.hp.idc.resm.ui.PageInfo;
import com.hp.idc.resm.ui.PageQueryInfo;

/**
 * 资源管理对象接口
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public interface IResourceService {

	/**
	 * 按资源id获取资源信息，不受权限控制
	 * 
	 * @param id
	 *            资源id
	 * @return 资源对象
	 */
	public ResourceObject getResourceById(int id);
	
	/**
	 * 按资源id获取多个资源信息，不受权限控制
	 * @param id 以","分隔的id字符串
	 * @return 多个资源信息
	 */
	public List<ResourceObject> getResourcesById(String id);
	
	/**
	 * 获取资源所有的属性，主要给flex调用，其它应用直接通过资源对象的方法即可取到属性值
	 * @param id 资源id
	 * @return 资源所有的属性（按模型中定义的）
	 */
	public List<AttributeBase> getResourceAttributes(int id);

	/**
	 * 获取所有资源信息
	 * 
	 * @param userId 操作用户id
	 * @return 所有资源对象
	 */
	public List<ResourceObject> getAllResources(int userId);

	/**
	 * 根据资源模型id获取所有资源
	 * 
	 * @param id
	 *            模型id
	 * @param userId 操作用户id
	 * @return 资源对象列表
	 */
	public List<ResourceObject> getResourcesByModelId(String id, int userId);

	/**
	 * 根据资源模型id获取所有资源
	 * 
	 * @param id
	 *            模型id
	 * @param includeChilds
	 *            是否包含子模型下的对象
	 * @param userId 操作用户id
	 * @return 资源对象列表
	 */
	public List<ResourceObject> getResourcesByModelId(String id,
			boolean includeChilds, int userId);

	/**
	 * 根据表达式查找资源
	 * 
	 * @param expr
	 *            表达式，格式类java写法，${attrId}表示属性值，可以
	 *            用${attrId}.compareTo("") > 0 && ${attrId} == "abc"之类的写法
	 * @param userId 操作用户id
	 * @return 匹配的资源列表
	 * @throws Exception
	 *             表达式执行失败时发生
	 */
	public List<ResourceObject> findResource(String expr, int userId) throws Exception;
	
	/**
	 * 根据表达式查找资源
	 * @param ll 需要查找的资源集合
	 * @param expr
	 *            表达式，格式类java写法，${attrId}表示属性值，可以
	 *            用${attrId}.compareTo("") > 0 && ${attrId} == "abc"之类的写法
	 * @param userId 操作用户id
	 * @return 匹配的资源列表
	 * @throws Exception
	 *             表达式执行失败时发生
	 */
	public List<ResourceObject> findResource(List<ResourceObject> ll,String expr, int userId) throws Exception;
	
	/**
	 * 根据属性值（唯一）来查找资源对象，不受权限控制
	 * @param attrId 属性id
	 * @param value 属性值
	 * @return 资源对象。找不到时返回null
	 * @throws Exception 查找失败，或找到的对象不唯一时发生
	 */
	public ResourceObject getResourceByAttribute(String attrId, String value) throws Exception;
	
	/**
	 * 获取资源对象列表
	 * @param modelId 模型id
	 * @param str 模糊查找字符串
	 * @param queryInfo 查询信息
	 * @param userId 操作用户id
	 * @return 资源对象列表
	 */
	public PageInfo<ResourceObject> listResource(String modelId, String str, PageQueryInfo queryInfo, int userId);
	
	/**
	 * 获取资源对象列表
	 * @param modelId 模型id
	 * @param recursion 是否递归查询
	 * @param str 模糊查找字符串
	 * @param queryInfo 查询信息
	 * @param userId 操作用户id
	 * @return 资源对象列表
	 */
	public PageInfo<ResourceObject> listResource(String modelId, boolean recursion, String str, PageQueryInfo queryInfo, int userId);
	
	/**
	 * 获取资源对象列表
	 * @param modelId 模型id
	 * @param recursion 是否递归查询
	 * @param paramMap 条件组合 {attributeId ==> attributeValue}
	 * @param queryInfo 查询信息
	 * @param userId 操作用户id
	 * @return 资源对象列表
	 */
	public PageInfo<ResourceObject> listResource(String modelId, boolean recursion, Map<String,Object> paramMap, PageQueryInfo queryInfo, int userId);

	/**
	 * 按用户权限对列表中的资源对象进行过滤
	 * 
	 * @param list
	 *            资源对象列表
	 * @param userId
	 *            用户id
	 * @return 用户有访问授权的资源对象
	 */
	public List<ResourceObject> filterByUser(List<ResourceObject> list,
			int userId);
	
	/**
	 * 按用户权限对列表中的资源对象进行过滤
	 * 
	 * @param idArray
	 *            资源对象id数组
	 * @param userId
	 *            用户id
	 * @return 用户有访问授权的资源对象
	 */
	public int[] filterByUser(int[] idArray,
			int userId);
	
	/**
	 * 检查用户对资源对象的访问权限
	 * 
	 * @param id
	 *            资源对象id
	 * @param userId
	 *            用户id
	 * @return true=可以访问，false=不可以访问
	 */
	public boolean hasAccessPermission(int id,
			int userId);

	/**
	 * 	/**
	 * 获取指定模型下, 指定表达式的资源列表
	 * @param modelId 指定模型ID
	 * @param exp 表达式
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public List<ResourceObject> getResourceByAttribute(String modelId, String exp, int userId) throws Exception;
}
