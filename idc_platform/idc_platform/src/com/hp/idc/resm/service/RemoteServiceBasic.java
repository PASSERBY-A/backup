/**
 * 
 */
package com.hp.idc.resm.service;

import java.net.MalformedURLException;

import com.caucho.hessian.client.HessianProxyFactory;

/**
 * 远程服务调用模板类
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * @param <T>
 *            模板类参数，远程对象类型
 * 
 */
public class RemoteServiceBasic<T> {

	/**
	 * 获取远程对象的地址，必须在派生类中实现此函数
	 * 
	 * @return 远程对象的地址
	 */
	protected String getRemoteServiceURL() {
		return "";
	}

	/**
	 * 获取远程对象
	 * 
	 * @return 远程对象
	 */
	@SuppressWarnings("unchecked")
	protected T getRemoteService() {
		String url = getRemoteServiceURL();
		HessianProxyFactory factory = new HessianProxyFactory();
		T basic = null;
		try {
			basic = (T) factory.create(url);
			// basic = (T) factory.create(basic.class, url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return basic;
	}
}
