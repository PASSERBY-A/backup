package com.hp.idc.system.security.dao.jpa;


import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.hp.idc.common.core.bo.Page;
import com.hp.idc.common.core.dao.jpa.GenericDaoJpa;
import com.hp.idc.system.security.dao.SysUserDao;
import com.hp.idc.system.security.entity.SysUser;
/**
 * 示例
 * @author <a href="mailto:si-qi.liang@hp.com">Liang, Si-Qi</a>
 *
 */
@Repository("sysUserDao")
public class SysUserDaoJpa extends GenericDaoJpa<SysUser, Long> implements SysUserDao{

	public SysUserDaoJpa() {
		super(SysUser.class);
	}
	/**
	 * 回调实现
	 */
	@Override
	public List<SysUser> queryResultList (Map<String, Object> paramMap,LinkedHashMap<String,String> sortMap){
		return this.queryResultList(new JsqlBuilder(){
			public String buildJSql(Map<String, Object> paramMap) {
				StringBuffer sb = new StringBuffer();
				sb.append("select o from SysUser o where 1=1");
				for(String key:paramMap.keySet()){
					if(key.equals("userName")){
						sb.append(" and o.loginName like :loginName");
						paramMap.put(key, "%"+(String)paramMap.get(key)+"%");
					}
					if(key.equals("actualName")){
						sb.append(" and o.actualName like :actualName");
						paramMap.put(key, "%"+(String)paramMap.get(key)+"%");
					}
				}
				return sb.toString();
			}
		}, paramMap, sortMap);
	}
	
	/**
	 * 自行拼接查询sql
	 * @Title:queryResultList2
	 * @Desciption:TODO
	 * @param paramMap
	 * @param sortMap
	 * @return
	 */
	@Override
	public List<SysUser> queryResultList2 (Map<String, Object> paramMap,LinkedHashMap<String,String> sortMap){
		StringBuffer sb = new StringBuffer();
		sb.append("select o from SysUser o where 1=1");
		for(String key:paramMap.keySet()){
			if(paramMap.get(key)!=null)
			{
				if(key.equals("userName")){
					sb.append(" and o.loginName like :loginName");
					paramMap.put(key, "%"+(String)paramMap.get(key)+"%");
				}
				if(key.equals("actualName")){
					sb.append(" and o.actualName like :actualName");
					paramMap.put(key, "%"+(String)paramMap.get(key)+"%");
				}
			}
		}
		return this.queryResultList(sb.toString(), paramMap, sortMap);
	}
	/**
	 * 回调实现
	 */
	@Override
	public Page<SysUser> queryResultPage(Map<String, Object> paramMap, LinkedHashMap<String,String> sortMap,int pageNo,int pageSize){
		return this.queryResultPage(new SqlBuilder(){
			public String buildJSql(Map<String, Object> paramMap) {
				StringBuffer sb = new StringBuffer();
				sb.append("select o from SysUser o where 1=1");
				for(String key:paramMap.keySet()){
					if(paramMap.get(key)!=null)
					{
						if(key.equals("loginName")){
							sb.append(" and o.loginName like :loginName");
							paramMap.put(key, "%"+(String)paramMap.get(key)+"%");
						}
					}
				}
				return sb.toString();
			}
			public String buildCountSql(Map<String, Object> paramMap) {
				StringBuffer sb = new StringBuffer();
				sb.append("select count(o.id) from SysUser o where 1=1");
				for(String key:paramMap.keySet()){
					if(paramMap.get(key)!=null)
					{
						if(key.equals("loginName")){
							sb.append(" and o.loginName like :loginName");
							paramMap.put(key, "%"+(String)paramMap.get(key)+"%");
						}
					}
				}
				return sb.toString();
			}			
		}, 				
		paramMap, sortMap, pageNo, pageSize);
	}
	/**
	 * 自行拼接查询sql与count sql
	 * @Title:queryResultList2
	 * @Desciption:TODO
	 * @param paramMap
	 * @param sortMap
	 * @return
	 */
	@Override
	public Page<SysUser> queryResultPage2(Map<String, Object> paramMap, LinkedHashMap<String,String> sortMap,int pageNo,int pageSize){
		StringBuffer sb = new StringBuffer();
		for(String key:paramMap.keySet()){
			if(paramMap.get(key)!=null)
			{
				if(key.equals("loginName")){
					sb.append(" and o.loginName like :userName");
					paramMap.put(key, "%"+(String)paramMap.get(key)+"%");
				}
			}
		}
		StringBuffer jsqlSb=new StringBuffer();
		jsqlSb.append("select o from SysUser o where 1=1");
		jsqlSb.append(sb);
		StringBuffer countSb=new StringBuffer();
		countSb.append("select count(o.id) from SysUser o where 1=1");
		countSb.append(sb);
		return this.queryResultPage(jsqlSb.toString(), countSb.toString(), paramMap, sortMap, pageNo, pageSize);
	}
	
	/**
	 * 根据登录名查找用户
	 * @Title:findSysUserByLodinName
	 * @param loginName
	 * @return
	 */
	@Transactional(readOnly=true,propagation=Propagation.NOT_SUPPORTED)
	public SysUser findSysUserByLodinName(String loginName){
		try{
			return getEntityManager().createNamedQuery("SYS_USER.findSysUserByLoginName", SysUser.class)
					.setParameter("loginName", loginName).getSingleResult();
		}catch(Exception e){
			log.error(e);
			return null;
		}
	}
	
	

}