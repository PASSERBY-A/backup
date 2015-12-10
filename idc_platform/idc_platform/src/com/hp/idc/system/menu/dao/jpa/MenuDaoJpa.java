/**   
 * @Title: MenuDaoJpa.java 
 * @Description: TODO
 * @date 2011-5-26 03:50:23pm   
 * @version 1.0  
 */
package com.hp.idc.system.menu.dao.jpa;     

import java.util.List;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.hp.idc.common.core.dao.jpa.GenericDaoJpa;
import com.hp.idc.system.menu.dao.MenuDao;
import com.hp.idc.system.menu.entity.Menu;
  
public class MenuDaoJpa extends GenericDaoJpa<Menu, Long> implements MenuDao {
	

	
	/*
	 * (non-Javadoc)
	 * @see com.hp.idc.system.menu.dao.MenuDao#getGroupMenu(long)
	 */
	@Override
	@Transactional(readOnly=true,propagation=Propagation.NOT_SUPPORTED)
	public List<Menu> getGroupMenu(long groupId){
		return null;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.hp.idc.system.menu.dao.MenuDao#getRoleMenu(long)
	 */
	@Override
	@Transactional(readOnly=true,propagation=Propagation.NOT_SUPPORTED)
	public List<Menu> getRoleMenu(long roleId){
		return null;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.hp.idc.system.menu.dao.MenuDao#getUserMenu(long)
	 */
	@Override
	@Transactional(readOnly=true,propagation=Propagation.NOT_SUPPORTED)
	public List<Menu> getUserMenu(long userId){
		return null;
	}
}
 