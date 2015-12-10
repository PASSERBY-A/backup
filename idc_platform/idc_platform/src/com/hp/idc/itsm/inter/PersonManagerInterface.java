package com.hp.idc.itsm.inter;

import java.util.List;

import com.hp.idc.json.JSONArray;

public interface PersonManagerInterface {

	/**
	 * 根据id获取人员信息
	 * @param id 人员id
	 * @return 返回找到的人员对象
	 */
	public PersonInfoInterface getPersonById(String id);
	/**
	 * 根据id获取人员名称
	 * @param id 人员id
	 * @return 返回找到的人员名称,找不到的返回原id参数
	 */
	public String getPersonNameById(String id);
	
	/**
	 * 根据sql查询人员信息
	 * @param sql
	 * @return 根据sql的结果，返回相应的JSON对象
	 */
	public JSONArray getPersonsBySQL(String sql);
	/**
	 * 获取所有人员信息
	 * @return 返回所有人员信息List<PersonInfo>
	 */
	public List getAllPersons();
	
	/**
	 * 重置密码
	 * @param userId 用户id
	 * @param newPasswd 新密码
	 * @return 成功时返回null, 失败返回错误信息
	 */
	public String resetPassword(String userId, 
			String newPasswd);

	/**
	 * 更新用户密码
	 * @param userId 用户密码
	 * @param oldPasswd 原密码
	 * @param newPasswd 新密码
	 * @return 成功时返回null, 失败返回错误信息
	 */
	public String changePassword(String userId, String oldPasswd,
			String newPasswd);

	/**
	 * 用户登录
	 * @param userId 用户id
	 * @param passwd 密码
	 * @return 成功返回null, 失败返回错误信息
	 */
	public String login(String userId, String passwd);
		
	public List getPersonsByWorkgoupId(String workgroupId,boolean includeChildren);
		
	public List getPersonsByOrganizationId(String organizationId,boolean includeChildren);

	/**
	 * 返回其他系统的人员id对应本系统的id（如OVSD的system对应本系统的ROOT）
	 * 各个系统的接口里面自己实现
	 * @param id
	 * @return
	 */
	public String getLocalId(String id);
	
	/**
	 * 返回本系统对应别的系统的ID（如本系统的root对应OVSD的system）
	 * @param id
	 * @return
	 */
	public String getRemoteId(String id);
}
