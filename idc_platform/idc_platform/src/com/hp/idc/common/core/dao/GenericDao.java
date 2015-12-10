package com.hp.idc.common.core.dao;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.hp.idc.common.core.bo.Page;

/**
 * GenericDao
 * 持久化接口
 * 继承该接口并实现持久化操作
 * 
 * @author <a href="mailto:si-qi.liang@hp.com">Liang, Si-Qi</a>
 *
 * @param <T> 实体类型
 * @param <PK> 主键类型
 */
public interface GenericDao <T, PK extends Serializable> {


    /**
     * 根据主键获取实例对象 
     * ObjectRetrievalFailureException Runtime Exception is thrown if
     * nothing is found.
     *
     * @param id 主键
     * @return a populated object
     * @see org.springframework.orm.ObjectRetrievalFailureException
     */
    public T get(PK id);

    /**
     * 检查主键为id的，类型为T的对象是否存在
     * @param id the id of the entity
     * @return - true if it exists, false if it doesn't
     */
    boolean exists(PK id);

    /**
     * 保存对象到数据库，插入或者更新
     * @param object the object to save
     * @return the persisted object
     */
    public T save(T object);

    /**
     * 删除对象
     * @param id 删除对象的主键值
     */
    public void remove(PK id);
    
    /**
     * 删除对象
     * @param object 删除的对象
     */
	public void remove(T object);
	
	/**
	 * 根据条件，查询对象的结果集列表
	 * @param paramMap 查询条件集
	 * @param sortMap 排序条件集
	 * @return
	 */
	public List<T> queryResultList(String jsql,Map<String,Object> paramMap, LinkedHashMap<String,String> sortMap);
	
	/**
	 * 根据条件，查询对象的分页结果集
	 * @param paramMap 查询条件集
	 * @param sortMap 排序条件集
	 * @param pageNo 页号
	 * @param pageSize 分页大小
	 * @return
	 */
	public Page<T> queryResultPage(String jSql, String countSql, Map<String,Object> paramMap, 
			LinkedHashMap<String,String> sortMap,int pageNo, int pageSize);

}