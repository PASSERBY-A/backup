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
 * GenericDao�ӿڳ���ʵ����
 * �̳и�����ʵ�ֶ�ʵ����ĳ־û�����
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
     * ���췽��
     */
    public GenericDaoJpa(){}
    /**
     * ���췽��
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
            log.warn("û���ҵ�IdΪ " +id+ "��'"+ this.persistentClass + "' ���� ");
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
     * ɾ������
     * @param id ����
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
     * ɾ������
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
	 * @param jSql ��ѯ�б���
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
     * ��ѯ��ҳ�����
     * @param jSql ��ѯ��䣨����������
     * @param countSql ��ѯ������������
     * @param paramMap ��ѯ����
     * @param sortMap ��������
     * @param pageNo ҳ��
     * @param pageSize ��ҳ��С
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
     * ��ȡ��ѯ���������
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
    		log.error("��ѯ�������������", e);
    	}
    	return 0;
    }
    
    /**
     * �����������
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
	 * ���ݲ�ѯHQL������б���Query����.
	 * ��find()�����ɽ��и������Ĳ���.
	 * 
	 * @param values �����ɱ�Ĳ���,�����ư�.
	 */
	public Query createQuery(EntityManager em,final String queryString, Map<String, Object> paramMap) {
		Assert.hasText(queryString, "queryString����Ϊ��");
		Query query = em.createQuery(queryString,persistentClass);
		setNamedParameters(query, paramMap);
		return query;
	}
	/**
	 * Ϊqeury�������ò���ֵ
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
	 * ��ȡ������properties
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
	 * ��ȡʵ������
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
	 * �ж�ʱ���Ƿ����߼�ɾ����ʵ��
	 * @return
	 */
	private boolean isRemovableEntity(){
		boolean result = false;
	    try {
	    	result = Removable.class.isInstance(persistentClass.newInstance());
	    }
	    catch (InstantiationException e) {
	    	log.error("�ж�ʵ������ʱ����ʵ��������!",e);
	    }
	    catch (IllegalAccessException e) {
	    	log.error("�ж�ʵ������ʱ����ʵ��������!",e);
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
