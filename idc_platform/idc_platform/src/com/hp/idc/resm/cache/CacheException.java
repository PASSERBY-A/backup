/**
 * 
 */
package com.hp.idc.resm.cache;

import java.io.IOException;

/**
 * 缓存操作异常
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public class CacheException extends IOException {

	/**
	 * 序列化uid
	 */
	private static final long serialVersionUID = -7543758521680321998L;

	/**
	 * 构造函数
	 * 
	 * @param msg
	 *            异常内容
	 */
	public CacheException(String msg) {
		super(msg);
	}

}
