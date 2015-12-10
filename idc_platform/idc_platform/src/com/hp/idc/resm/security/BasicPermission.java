/**
 * 
 */
package com.hp.idc.resm.security;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.hp.idc.resm.cache.CacheException;
import com.hp.idc.resm.cache.CacheableObject;
import com.hp.idc.resm.util.RoleUtil;

/**
 * 权限
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public class BasicPermission extends CacheableObject {

	/**
	 * 序列化id
	 */
	private static final long serialVersionUID = 1101065225362372052L;

	/**
	 * 允许
	 */
	public static final int PERM_ALLOW = 1;

	/**
	 * 忽略
	 */
	public static final int PERM_PASS = 0;

	/**
	 * 拒绝
	 */
	public static final int PERM_DENY = -1;

	/**
	 * id
	 */
	protected String id;

	/**
	 * 名称
	 */
	protected String name;

	/**
	 * 描述
	 */
	protected String remark;

	/**
	 * 获取id
	 * 
	 * @return id
	 * @see #id
	 */
	public String getId() {
		return this.id;
	}

	/**
	 * 设置id
	 * 
	 * @param id
	 *            id
	 * @throws CacheException
	 *             对象已被缓存时发生
	 * @see #id
	 */
	public void setId(String id) throws CacheException {
		checkSet();
		this.id = id;
	}

	/**
	 * 获取名称
	 * 
	 * @return 名称
	 * @see #name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * 设置名称
	 * 
	 * @param name
	 *            名称
	 * @throws CacheException
	 *             对象已被缓存时发生
	 * @see #name
	 */
	public void setName(String name) throws CacheException {
		checkSet();
		this.name = name;
	}

	/**
	 * 获取描述
	 * 
	 * @return 描述
	 * @throws CacheException
	 *             对象已被缓存时发生
	 * @see #remark
	 */
	public String getRemark() throws CacheException {
		checkSet();
		return this.remark;
	}

	/**
	 * 设置描述
	 * 
	 * @param remark
	 *            描述
	 * @throws CacheException
	 *             对象已被缓存时发生
	 * @see #remark
	 */
	public void setRemark(String remark) throws CacheException {
		checkSet();
		this.remark = remark;
	}

	/**
	 * 对权限进行较验
	 * 
	 * @param userId
	 *            用户id
	 * @param obj
	 *            较验的对象
	 * @return PERM_ALLOW=有权限, PERM_PASS=跳过, PERM_DENY=无权限/拒绝访问
	 * @throws Exception
	 *             较验有异常时发生
	 * @see #PERM_ALLOW
	 * @see #PERM_PASS
	 * @see #PERM_DENY
	 */
	public int valid(int userId, Object obj) throws Exception {
		return PERM_ALLOW; // 默认可以访问
	}

	/**
	 * 对列表进行过滤，将无权限、有权限的对象从列表中清除
	 * @param <T> 对象类型
	 * 
	 * @param userId
	 *            用户id
	 * @param list
	 *            对象列表，本函数操作后，列表中只保留不匹配的对象
	 * @return 有权限的对象
	 * @throws Exception
	 *             较验有异常时发生
	 */
	public<T> List<T> filter(int userId, List<T> list)
			throws Exception {
		List<T> ret = new ArrayList<T>();
		if (RoleUtil.isSystemAdmin(userId))
			ret.addAll(list);
		list.clear();
		return ret;
	}

	/**
	 * 设置参数，在派生类中实现此函数
	 * 
	 * @param param
	 *            参数内容
	 */
	public void setParameter(String param) {
		// 在派生类中实现此函数
	}

	/**
	 * 获取参数说明
	 * 
	 * @return 参数说明
	 */
	public String getParamDesc() {
		return "";
	}

	@Override
	public String getPrimaryKey() {
		return this.id;
	}

	@Override
	public String getDatabaseTable() {
		return "resm_permission";
	}

	@Override
	public String getPrimaryKeyField() {
		return "id";
	}

	@Override
	public void updateResultSet(Connection conn, ResultSet rs) {
		// 不更新，有哪些权限在后台维护
	}

	@Override
	public void readFromResultSet(ResultSet rs) throws Exception {
		this.id = rs.getString("id");
		this.name = rs.getString("name");
		this.remark = rs.getString("remark");
	}
}
