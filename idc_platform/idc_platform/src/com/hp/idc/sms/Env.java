package com.hp.idc.sms;

import java.io.IOException;
import java.net.URL;

import com.huawei.smproxy.util.Cfg;
import com.huawei.smproxy.util.Resource;

/**
 * �ṩϵͳ���л�����Ϣ��
 */
public class Env {

	/** ���ö�д�ࡣ */
	static Cfg config;

	/** ��Դ�ļ���д�ࡣ */
	static Resource resource;

	/**
	 * ȡ�����ö�д�ࡣ
	 */
	public static Cfg getConfig() {

		// ���δ��ʼ������˵��ϵͳ�����ڰ�װ�����У������õĶ�ȡ
		if (config == null) {
			try {
				URL url = Env.class.getClassLoader().getResource("config.xml");
				config = new Cfg(url.toString());
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return config;
	}

	/**
	 * ȡ����Դ��ȡ�ࡣ
	 */
	public static Resource getResource() {
		if (resource == null) {
			try {
				resource = new Resource("resource");
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return resource;
	}

	public static void main(String[] args) {
		getConfig();
		System.out.println(config == null ? "null": config.toString());
	}
}
