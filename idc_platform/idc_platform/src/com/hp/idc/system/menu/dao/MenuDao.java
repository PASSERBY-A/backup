/**   
 * @Title: MenuDao.java 
 * @Description: TODO
 * @date 2011-5-26 ����03:48:40   
 * @version 1.0  
 */
package com.hp.idc.system.menu.dao;     

import java.util.List;

import com.hp.idc.common.core.dao.GenericDao;
import com.hp.idc.system.menu.entity.Menu;

/**
 * ϵͳ�˵�dao�ӿ�
 * @ClassName: MenuDao
 * @Descprition: ����ϵͳ�˵��ĳ־û��ӿ�
 * @author <a href="mailto:si-qi.liang@hp.com">Liang, Si-Qi</a>
 * @version 1.0
 */
public interface MenuDao extends GenericDao<Menu, Long> {

	public List<Menu> getGroupMenu(long groupId);
	public List<Menu> getRoleMenu(long roleId);
	public List<Menu> getUserMenu(long userId);
}
 