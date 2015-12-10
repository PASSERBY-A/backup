package com.hp.idc.resm.cache;



/**
 * ȫ�ֻ��棬�����޸ģ�׼��ȥ�������
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public class GlobalCache {
	/**
	 * ͼ�껺��
	 */
	public static IconCache iconCache = null;
	
	/**
	 * ģ�黺��
	 */
	public static ModuleCache moduleCache = null;
	
	/**
	 * �˵�����
	 */
	public static MenuCache menuCache = null;

	
	/**
	 * ��ʼ��
	 */
	public void init() {
		try {
			iconCache = new IconCache();
			iconCache.initCache();

			moduleCache = new ModuleCache();
			moduleCache.initCache();

			menuCache = new MenuCache();
			menuCache.initCache();
			

	
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
