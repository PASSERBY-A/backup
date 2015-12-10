/**
 * 
 */
package com.hp.idc.itsm.runtime;

import java.io.File;
import java.io.FileInputStream;

/**
 * 动态类的 ClassLoader
 * @author 梅园
 */
public class RuntimeClassLoader extends ClassLoader {

	/**
	 * 存放二进制代码的数组
	 */
	protected byte[] data;
	
	/**
	 * 默认构造函数
	 */
	public RuntimeClassLoader() {
		super();
	}

	/**
	 * 调用基类的构造函数
	 * @param arg0 基类所需要的参数
	 */
	public RuntimeClassLoader(ClassLoader arg0) {
		super(arg0);
	}

	/**
	 * 重载java.lang.ClassLoader#findClass(java.lang.String)
	 * @see java.lang.ClassLoader#findClass(java.lang.String)
	 */
	public Class findClass(String name) {
		return defineClass(name, this.data, 0, this.data.length);
	}

	/**
	 * 从文件中加载类数据
	 * @param fileName 文件名
	 */
	private void loadClassData(String fileName) {
		try {
			File file = new File(fileName);
			long len = file.length();
			this.data = new byte[(int) len];
			FileInputStream fin = new FileInputStream(file);
			fin.read(this.data);
			fin.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 从文件中读取二进制数据, 并返回生成的 Class
	 * @param name 指定的类名
	 * @param fileName 指定的文件名
	 * @return 返回生成的 Class
	 */
	public Class load(String name, String fileName) {
		loadClassData(fileName);
		Class objClass = null;
		try {
			objClass = loadClass(name, true);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return objClass;
	}

	/**
	 * 通过二进制数据返回生成的 Class
	 * @param name 指定的类名
	 * @param data 指定的二进制数据
	 * @return 返回生成的 Class
	 */
	public Class load(String name, byte[] data) {
		this.data = data;
		Class objClass = null;
		try {
			objClass = loadClass(name, true);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return objClass;
	}
}
