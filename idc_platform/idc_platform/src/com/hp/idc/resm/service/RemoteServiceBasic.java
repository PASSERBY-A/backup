/**
 * 
 */
package com.hp.idc.resm.service;

import java.net.MalformedURLException;

import com.caucho.hessian.client.HessianProxyFactory;

/**
 * Զ�̷������ģ����
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * @param <T>
 *            ģ���������Զ�̶�������
 * 
 */
public class RemoteServiceBasic<T> {

	/**
	 * ��ȡԶ�̶���ĵ�ַ����������������ʵ�ִ˺���
	 * 
	 * @return Զ�̶���ĵ�ַ
	 */
	protected String getRemoteServiceURL() {
		return "";
	}

	/**
	 * ��ȡԶ�̶���
	 * 
	 * @return Զ�̶���
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
