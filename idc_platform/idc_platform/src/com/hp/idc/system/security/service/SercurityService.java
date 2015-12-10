/**   
 * @Title: SercurityService.java 
 * @Description: TODO
 * @date 2011-5-26 下午02:54:46   
 * @version 1.0  
 */
package com.hp.idc.system.security.service;     

import java.util.LinkedHashMap;
import java.util.Map;

import com.hp.idc.common.core.bo.Page;
import com.hp.idc.system.security.bo.SysResource;
import com.hp.idc.system.security.entity.SysUser;

/**
 * 系统安全服务service接口
 * @ClassName: SercurityService
 * @Descprition: TODO
 * @author <a href="mailto:si-qi.liang@hp.com">Liang, Si-Qi</a>
 * @version 1.0
 */
public interface SercurityService {
	
	public boolean valiLoginUser(String loginName, String password);
	
	public boolean hasPermission(SysUser user, SysResource sysResource);
	
	public Page<SysUser> querySysUserList(Map<String,Object> paramMap,
			LinkedHashMap<String,String>sortMap,int pageNo, int pageSize);

}
 