package com.hp.idc.sms;

import java.io.IOException;
import java.net.URL;

import com.huawei.smproxy.util.Cfg;
import com.huawei.smproxy.util.Resource;

/**
 * 提供系统运行环境信息。
 */
public class Env {

	/** 配置读写类。 */
	static Cfg config;

	/** 资源文件读写类。 */
	static Resource resource;

	/**
	 * 取得配置读写类。
	 */
	public static Cfg getConfig() {

		// 如果未初始化，则说明系统正处在安装过程中，则配置的读取
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
	 * 取得资源读取类。
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
