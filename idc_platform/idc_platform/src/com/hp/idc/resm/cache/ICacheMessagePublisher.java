/**
 * 
 */
package com.hp.idc.resm.cache;

/**
 * ������Ϣ���ͽӿ�
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 *
 */
public interface ICacheMessagePublisher {

	/**
	 * ��������������Ϣ
	 * 
	 * @param cacheName
	 *            ��Ϣ����
	 * @param object
	 *            ��Ϣ����
	 */
	public void publishCacheAddMessage(String cacheName, CacheableObject object);
	
	/**
	 * ��������ɾ����Ϣ
	 * 
	 * @param cacheName
	 *            ��Ϣ����
	 * @param object
	 *            ��Ϣ����
	 */
	public void publishCacheRemoveMessage(String cacheName,
			CacheableObject object);
}
