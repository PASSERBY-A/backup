package com.hp.idc.common.core.dao.jpa;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.QueryParameterException;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.hp.idc.common.core.bo.Page;
import com.hp.idc.common.core.dao.GenericDao;
import com.hp.idc.common.core.entity.Removable;
import com.hp.idc.common.util.reflection.ReflectionUtils;
/**
 * GenericDaoJpa.java
 * 
 * GenericDao接口抽象实现类
 * 继承该类以实现对实体类的持久化操作
 * @author <a href="mailto:si-qi.liang@hp.com">Liang, Si-Qi</a>
 *
 * @param <T>
 * @param <PK>
 */
@Transactional
public abstract class GenericDaoJpa<T, PK extends Serializable> implements GenericDao<T, PK> {
	
	public static final String ORDER_ASC  = "asc";
	public static final String ORDER_DESC = "desc";
	
	@PersistenceContext
	private EntityManager entityManager;
    protected final Log log = LogFactory.getLog(getClass());
    private Class<T> persistentClass;

    /**
     * 构造方法
     */
    public GenericDaoJpa(){}
    /**
     * 构造方法
     * @param persistentClass
     */
    public GenericDaoJpa(final Class<T> persistentClass) {
        this.persistentClass = persistentClass;
    }
    
	/*
	 * (non-Javadoc)
	 * @see com.hp.idc.core.dao.GenericDao#get(java.io.Serializable)
	 */
    @Override
    @Transactional(readOnly=true,propagation=Propagation.NOT_SUPPORTED) 
    public T get(PK id) {
        T entity = (T) getEntityManager().find(this.persistentClass, id);
        if (entity == null) {
            log.warn("没有找到Id为 " +id+ "的'"+ this.persistentClass + "' 对象 ");
            throw new ObjectRetrievalFailureException(this.persistentClass, id);
        }
        return entity;
    }
    /*
     * (non-Javadoc)
     * @see com.hp.idc.core.dao.GenericDao#exists(java.io.Serializable)
     */
    @Override
    @Transactional(readOnly=true,propagation=Propagation.NOT_SUPPORTED) 
    public boolean exists(PK id) {
        T entity = (T) getEntityManager().find(this.persistentClass, id);
        return entity != null;
    }

    /*
     * (non-Javadoc)
     * @see com.hp.idc.core.dao.GenericDao#save(java.lang.Object)
     */
    public T save(T object) {
        return (T) getEntityManager().merge(object);
    }

    /**
     * 删除方法
     * @param id 主键
     */
    @Override
    public void remove(PK id) {
    	if(isRemovableEntity()){
    		T object =this.get(id);
    		ReflectionUtils.invokeSetterMethod(object, "removed", 1);
    		getEntityManager().merge(object);
    	}
    	else{
    		getEntityManager().remove(this.get(id));
    	}
    }
    /**
     * 删除方法
     * @param object
     */
    @Override
    public void remove(T object){
    	if(isRemovableEntity()){
    		ReflectionUtils.invokeSetterMethod(object, "removed", 1);
    		getEntityManager().merge(object);
    	}
    	else{
    		getEntityManager().remove(object);
    	}
    }
    


    public List<T> queryResultList(JsqlBuilder sqlBuilder,Map<String, Object> paramMap,LinkedHashMap<String,String> sortMap){
    	String jSql=sqlBuilder.buildJSql(paramMap);
    	return queryResultList(jSql, paramMap, sortMap);
    }
    
    /**
	 * 
	 * @param jSql 查询列表结果
	 * @param paramValues
	 * @param sortMap
	 * @return
	 */
    @Override
    @SuppressWarnings("unchecked")
	@Transactional(readOnly=true,propagation=Propagation.NOT_SUPPORTED)  
	public List<T> queryResultList(String jSql,Map<String, Object> paramMap,LinkedHashMap<String,String> sortMap)
    {        
    	EntityManager em = getEntityManager();
    	
        Query query = em.createQuery(jSql + getSortSql(sortMap));
		setNamedParameters(query, paramMap);
		
		try
		{
			return query.getResultList();
		}catch(Exception e){
			e.printStackTrace();
			return new ArrayList<T>();
		}
    }
    
    public Page<T> queryResultPage(SqlBuilder sqlBuilder,Map<String, Object>paramMap ,LinkedHashMap<String,String> sortMap,int pageNo, int pageSize){
    	String jSql=sqlBuilder.buildJSql(paramMap);
    	String countSql=sqlBuilder.buildCountSql(paramMap);
    	return queryResultPage(jSql, countSql, paramMap, sortMap, pageNo, pageSize);
    }
    
    /**
     * 查询分页结果集
     * @param jSql 查询语句（不包含排序）
     * @param countSql 查询结果集总数语句
     * @param paramMap 查询条件
     * @param sortMap 排序条件
     * @param pageNo 页号
     * @param pageSize 分页大小
     * @return
     */
    @Override
    @SuppressWarnings("unchecked")
	@Transactional(readOnly=true,propagation=Propagation.NOT_SUPPORTED) 
	public Page<T> queryResultPage(String jSql ,String countSql , Map<String, Object> paramMap, LinkedHashMap<String,String> sortMap,int pageNo, int pageSize){
    	EntityManager em = getEntityManager();
    	String sortJPQL = getSortSql(sortMap);
		try{
			int totalCount = getTotalCount(em,countSql, paramMap);
			if (totalCount < 1) 
				return new Page<T>();
			
			if (pageNo < 1) {
				pageNo = 1;
			}
			int startIndex = Page.getStartOfAnyPage(pageNo, pageSize);
			Query q = createQuery(em,jSql + sortJPQL, paramMap);
			List<T> list= q.setFirstResult(startIndex-1).setMaxResults(pageSize).getResultList();
			
			int avaCount = (list == null) ? 0 : list.size();
			return new Page<T>(startIndex, avaCount, totalCount, pageSize, list);
		
		}catch(Exception e){
			e.printStackTrace();
			return new Page<T>();
		}
    }
    /**
     * 获取查询结果集总数
     * @param em
     * @param countSQL
     * @param values
     * @return
     */
	@Transactional(readOnly=true,propagation=Propagation.NOT_SUPPORTED) 
    protected int getTotalCount(EntityManager em,final String countSQL, Map<String,Object> paramMap)
    {
    	Query query = em.createQuery(countSQL,Long.class);
    	setNamedParameters(query,paramMap);
    	try
    	{
    		return ((Long)query.getSingleResult()).intValue();
    	}
    	catch(Exception e)
    	{
    		log.error("查询结果集总数出错！", e);
    	}
    	return 0;
    }
    
    /**
     * 生成排序语句
     * @param sortMap
     * @return
     */
    protected String getSortSql(Map<String,String> sortMap)
    {
    	if(sortMap == null || sortMap.isEmpty())
    		return "";
    	StringBuilder sb = new StringBuilder(); 
		for (Iterator<String> i = sortMap.keySet().iterator(); i.hasNext(); ) {	
			String fieldName = i.next();
			String orderType = sortMap.get(fieldName);
			if(!"".equals(sb.toString()))
				sb.append(",");	
			if(orderType == null)
			{
				sb.append( fieldName + ORDER_ASC);
				break;
			}		
			if (ORDER_ASC.equalsIgnoreCase(orderType)||ORDER_DESC.equalsIgnoreCase(orderType))
				sb.append( fieldName +" "+orderType);
			else
				sb.append( fieldName+ ORDER_ASC);
		}
		sb.insert(0," order by ");	
    	return sb.toString();
    }
    
    /**
	 * 根据查询HQL与参数列表创建Query对象.
	 * 与find()函数可进行更加灵活的操作.
	 * 
	 * @param values 数量可变的参数,按名称绑定.
	 */
	public Query createQuery(EntityManager em,final String queryString, Map<String, Object> paramMap) {
		Assert.hasText(queryString, "queryString不能为空");
		Query query = em.createQuery(queryString,persistentClass);
		setNamedParameters(query, paramMap);
		return query;
	}
	/**
	 * 为qeury对象设置参数值
	 * @param query
	 * @param paramValues
	 */
	protected void setNamedParameters(Query query, Map<String,Object> paramValues)
	{
		if(query == null || paramValues == null || paramValues.isEmpty())
			return ;
		for (Iterator<String> i = paramValues.keySet().iterator(); i.hasNext(); ) {
			String paraName = i.next();	
			try{
				if(paramValues.get(paraName) instanceof java.util.Date || paramValues.get(paraName) instanceof java.util.Calendar)
				{
					query.setParameter(paraName, (java.util.Date)paramValues.get(paraName), javax.persistence.TemporalType.TIMESTAMP);
					continue;
				}
				query.setParameter(paraName, paramValues.get(paraName));
			}catch(QueryParameterException e){
				e.printStackTrace();
				continue;
			}
		}
	}
	/**
	 * 获取计数的properties
	 * @return
	 */
	protected String getCountField(){  
        String out = "o";  
        try {  
            PropertyDescriptor[] propertyDescriptors = Introspector.getBeanInfo(persistentClass).getPropertyDescriptors();  
            for(PropertyDescriptor propertydesc : propertyDescriptors){  
                Method method = propertydesc.getReadMethod();
                if(method!=null && method.isAnnotationPresent(EmbeddedId.class)){                     
                    PropertyDescriptor[] ps = Introspector.getBeanInfo(propertydesc.getPropertyType()).getPropertyDescriptors();  
                    out = "o."+ propertydesc.getName()+ "." + (!ps[1].getName().equals("class")? ps[1].getName(): ps[0].getName());  
                    break;  
                }  
            }  
        } catch (Exception e) {  
            
        }  
        return out;
    }  
	
	/**
	 * 获取实体名称
	 * @return
	 */
	protected String getEntityName(){  
        String entityname = this.persistentClass.getSimpleName();  
        Entity entity = this.persistentClass.getAnnotation(Entity.class);  
        if(entity.name()!=null && !"".equals(entity.name())){  
            entityname = entity.name();  
        }  
        return entityname;  
    }  
	
	
	/**
	 * 判断时候是否是逻辑删除的实体
	 * @return
	 */
	private boolean isRemovableEntity(){
		boolean result = false;
	    try {
	    	result = Removable.class.isInstance(persistentClass.newInstance());
	    }
	    catch (InstantiationException e) {
	    	log.error("判断实体类型时发生实例化错误!",e);
	    }
	    catch (IllegalAccessException e) {
	    	log.error("判断实体类型时发生实例化错误!",e);
	    }
	    return result;
	}
	
	public abstract interface SqlBuilder {
		public String buildJSql(Map<String, Object> paramMap);
		public String buildCountSql(Map<String, Object> paramMap);
	}
	 
	public abstract class JsqlBuilder implements SqlBuilder{
		public String buildCountSql(Map<String, Object> paramMap){return null;}
	}
	
	public EntityManager getEntityManager() {
		return entityManager;
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

}
