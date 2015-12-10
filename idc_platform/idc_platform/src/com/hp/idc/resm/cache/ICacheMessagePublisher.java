/**
 * 
 */
package com.hp.idc.resm.cache;

/**
 * 缓存消息发送接口
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 *
 */
public interface ICacheMessagePublisher {

	/**
	 * 发布缓存增加消息
	 * 
	 * @param cacheName
	 *            消息类型
	 * @param object
	 *            消息内容
	 */
	public void publishCacheAddMessage(String cacheName, CacheableObject object);
	
	/**
	 * 发布缓存删除消息
	 * 
	 * @param cacheName
	 *            消息类型
	 * @param object
	 *            消息内容
	 */
	public void publishCacheRemoveMessage(String cacheName,
			CacheableObject object);
}
