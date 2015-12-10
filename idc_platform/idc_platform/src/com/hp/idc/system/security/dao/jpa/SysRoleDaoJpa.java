/**   
 * @Title: SysRoleDaoJpa.java 
 * @Description: TODO
 * @date 2011-5-26 обнГ02:44:26   
 * @version 1.0  
 */
package com.hp.idc.system.security.dao.jpa;     


import org.springframework.stereotype.Repository;

import com.hp.idc.common.core.dao.jpa.GenericDaoJpa;
import com.hp.idc.system.security.dao.SysRoleDao;
import com.hp.idc.system.security.entity.SysRole;

@Repository("sysRoleDao")  
public class SysRoleDaoJpa extends GenericDaoJpa<SysRole, Long> implements SysRoleDao {
	
	public SysRoleDaoJpa() {
		super(SysRole.class);
	}
	
}
 