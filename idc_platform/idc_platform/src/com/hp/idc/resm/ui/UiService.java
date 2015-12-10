package com.hp.idc.resm.ui;

import java.util.List;

import com.hp.idc.resm.cache.GlobalCache;

/**
 * �û��������
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public class UiService {

	/**
	 * ��ȡ�û���Ȩ�޵Ĺ���ģ���б�
	 * 
	 * @param userId
	 *            �û�ID
	 * @return �û���Ȩ�޵Ĺ���ģ���б�
	 */
	public List<Module> getModules(String userId) {
		
		List<Module> l = GlobalCache.moduleCache.getAll();
		return l;
	}

	/**
	 * ��ȡ�û���Ȩ�޵Ĳ˵��б�
	 * 
	 * @param userId
	 *            �û�ID
	 * @param moduleId
	 *            ģ��ID
	 * @return �û���Ȩ�޵Ĳ˵��б�
	 */
	public List<Menu> getMenus(String userId, int moduleId) {
		return GlobalCache.menuCache.getMenus(moduleId);
	}

}
