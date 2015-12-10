/**
 * 
 */
package com.hp.idc.itsm.runtime;

import java.io.File;
import java.io.FileInputStream;

/**
 * ��̬��� ClassLoader
 * @author ÷԰
 */
public class RuntimeClassLoader extends ClassLoader {

	/**
	 * ��Ŷ����ƴ��������
	 */
	protected byte[] data;
	
	/**
	 * Ĭ�Ϲ��캯��
	 */
	public RuntimeClassLoader() {
		super();
	}

	/**
	 * ���û���Ĺ��캯��
	 * @param arg0 ��������Ҫ�Ĳ���
	 */
	public RuntimeClassLoader(ClassLoader arg0) {
		super(arg0);
	}

	/**
	 * ����java.lang.ClassLoader#findClass(java.lang.String)
	 * @see java.lang.ClassLoader#findClass(java.lang.String)
	 */
	public Class findClass(String name) {
		return defineClass(name, this.data, 0, this.data.length);
	}

	/**
	 * ���ļ��м���������
	 * @param fileName �ļ���
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
	 * ���ļ��ж�ȡ����������, ���������ɵ� Class
	 * @param name ָ��������
	 * @param fileName ָ�����ļ���
	 * @return �������ɵ� Class
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
	 * ͨ�����������ݷ������ɵ� Class
	 * @param name ָ��������
	 * @param data ָ���Ķ���������
	 * @return �������ɵ� Class
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
