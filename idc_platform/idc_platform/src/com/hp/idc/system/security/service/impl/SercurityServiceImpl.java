/**   
 * @Title: SercurityServiceImpl.java 
 * @Description: TODO
 * @date 2011-7-5 上午10:34:12   
 * @version 1.0  
 */
package com.hp.idc.system.security.service.impl;     

import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Service;

import com.hp.idc.common.core.bo.Page;
import com.hp.idc.system.security.bo.SysResource;
import com.hp.idc.system.security.dao.SysGroupDao;
import com.hp.idc.system.security.dao.SysUserDao;
import com.hp.idc.system.security.dao.jpa.SysGroupDaoJpa;
import com.hp.idc.system.security.dao.jpa.SysUserDaoJpa;
import com.hp.idc.system.security.entity.SysUser;
import com.hp.idc.system.security.service.SercurityService;

/**
 * 系统安全服务service实现类
 * @ClassName: SercurityServiceImpl
 * @Descprition: TODO
 * @author <a href="mailto:si-qi.liang@hp.com">Liang, Si-Qi</a>
 * @version 1.0
 */
@Service("sercurityService")
public class SercurityServiceImpl implements SercurityService {
	
	@Resource
	private SysUserDao sysUserDao;
	
	@Resource
	private SysGroupDao sysGroupDao;

	
	/*
	 * (non-Javadoc)
	 * @see com.hp.idc.system.security.service.SercurityService#hasPermission(com.hp.idc.system.security.entity.SysUser, com.hp.idc.system.security.bo.SysResource)
	 */
	@Override
	public boolean hasPermission(SysUser user, SysResource sysResource) {
		// TODO Auto-generated method stub
		return true;
	}
	/*
	 * (non-Javadoc)
	 * @see com.hp.idc.system.security.service.SercurityService#valiLoginUser(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean valiLoginUser(String loginName, String password) {
		// TODO Auto-generated method stub
		return true;
	}
	@Override
	public Page<SysUser> querySysUserList(Map<String,Object> paramMap,LinkedHashMap<String,String>sortMap,int pageNo, int pageSize){
		return sysUserDao.queryResultPage2(paramMap, sortMap, pageNo, pageSize);
	}
	public SysUserDao getSysUserDao() {
		return sysUserDao;
	}
	public void setSysUserDao(SysUserDao sysUserDao) {
		this.sysUserDao = sysUserDao;
	}
	public SysGroupDao getSysGroupDao() {
		return sysGroupDao;
	}
	public void setSysGroupDao(SysGroupDao sysGroupDao) {
		this.sysGroupDao = sysGroupDao;
	}
	
	
	

}
 