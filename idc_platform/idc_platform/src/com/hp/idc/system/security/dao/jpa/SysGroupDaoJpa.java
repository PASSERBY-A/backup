/**   
 * @Title: SysGroupDaoJpa.java 
 * @Description: TODO
 * @date 2011-5-26 обнГ02:49:59   
 * @version 1.0  
 */
package com.hp.idc.system.security.dao.jpa;     

import java.util.Map;

import org.springframework.stereotype.Repository;

import com.hp.idc.common.core.dao.jpa.GenericDaoJpa;
import com.hp.idc.system.security.dao.SysGroupDao;
import com.hp.idc.system.security.entity.SysGroup;

@Repository("sysGroupDao")  
public class SysGroupDaoJpa extends GenericDaoJpa<SysGroup, Long> implements SysGroupDao {

	public SysGroupDaoJpa() {
		super(SysGroup.class);
	}	

}
 