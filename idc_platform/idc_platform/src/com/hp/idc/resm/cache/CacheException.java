/**
 * 
 */
package com.hp.idc.resm.cache;

import java.io.IOException;

/**
 * ��������쳣
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public class CacheException extends IOException {

	/**
	 * ���л�uid
	 */
	private static final long serialVersionUID = -7543758521680321998L;

	/**
	 * ���캯��
	 * 
	 * @param msg
	 *            �쳣����
	 */
	public CacheException(String msg) {
		super(msg);
	}

}
