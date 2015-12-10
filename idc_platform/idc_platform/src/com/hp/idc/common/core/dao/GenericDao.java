package com.hp.idc.common.core.dao;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.hp.idc.common.core.bo.Page;

/**
 * GenericDao
 * �־û��ӿ�
 * �̳иýӿڲ�ʵ�ֳ־û�����
 * 
 * @author <a href="mailto:si-qi.liang@hp.com">Liang, Si-Qi</a>
 *
 * @param <T> ʵ������
 * @param <PK> ��������
 */
public interface GenericDao <T, PK extends Serializable> {


    /**
     * ����������ȡʵ������ 
     * ObjectRetrievalFailureException Runtime Exception is thrown if
     * nothing is found.
     *
     * @param id ����
     * @return a populated object
     * @see org.springframework.orm.ObjectRetrievalFailureException
     */
    public T get(PK id);

    /**
     * �������Ϊid�ģ�����ΪT�Ķ����Ƿ����
     * @param id the id of the entity
     * @return - true if it exists, false if it doesn't
     */
    boolean exists(PK id);

    /**
     * ����������ݿ⣬������߸���
     * @param object the object to save
     * @return the persisted object
     */
    public T save(T object);

    /**
     * ɾ������
     * @param id ɾ�����������ֵ
     */
    public void remove(PK id);
    
    /**
     * ɾ������
     * @param object ɾ���Ķ���
     */
	public void remove(T object);
	
	/**
	 * ������������ѯ����Ľ�����б�
	 * @param paramMap ��ѯ������
	 * @param sortMap ����������
	 * @return
	 */
	public List<T> queryResultList(String jsql,Map<String,Object> paramMap, LinkedHashMap<String,String> sortMap);
	
	/**
	 * ������������ѯ����ķ�ҳ�����
	 * @param paramMap ��ѯ������
	 * @param sortMap ����������
	 * @param pageNo ҳ��
	 * @param pageSize ��ҳ��С
	 * @return
	 */
	public Page<T> queryResultPage(String jSql, String countSql, Map<String,Object> paramMap, 
			LinkedHashMap<String,String> sortMap,int pageNo, int pageSize);

}