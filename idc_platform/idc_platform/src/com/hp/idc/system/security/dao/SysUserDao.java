/**   
 * @{#} SampleDao.java Create on 2011-5-25 下午05:36:53   
 *   
 */
package com.hp.idc.system.security.dao;   

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.hp.idc.common.core.bo.Page;
import com.hp.idc.common.core.dao.GenericDao;
import com.hp.idc.system.security.entity.SysUser;
  
/**   
 * @author <a href="mailto:si-qi.liang@hp.com">Liang, Si-Qi</a>  
 * @version 1.0   
 */

public interface SysUserDao extends GenericDao<SysUser, Long> {
	
	public List<SysUser> queryResultList (Map<String, Object> paramMap,LinkedHashMap<String,String> sortMap);
	
	public List<SysUser> queryResultList2 (Map<String, Object> paramMap,LinkedHashMap<String,String> sortMap);
	
	public Page<SysUser> queryResultPage(Map<String, Object> paramMap, LinkedHashMap<String,String> sortMap,int pageNo,int pageSize);
	
	public Page<SysUser> queryResultPage2(Map<String, Object> paramMap, LinkedHashMap<String,String> sortMap,int pageNo,int pageSize);
	
}
 