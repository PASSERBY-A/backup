package com.hp.idc.cas.auc;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;

import com.hp.idc.cas.Cache;
import com.hp.idc.cas.common.CommonInfo;
import com.hp.idc.cas.common.DBUtil;
import com.hp.idc.cas.common.Encrypt;
import com.hp.idc.cas.common.OperationResult;
import com.hp.idc.cas.log.LogInfo;
import com.hp.idc.cas.log.LogManager;
import com.hp.idc.common.CacheSync;
import com.hp.idc.itsm.message.MessageManager;

/**
 * 人员管理类
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public class PersonManager {

	public PersonManager() {
	}

	private static PersonInfo getPersonInfoFromResultSet(ResultSet rs)
			throws SQLException {
		PersonInfo pi = new PersonInfo();
		pi.setOid(rs.getInt("OID"));
		pi.setId(rs.getString("id"));
		pi.setName(rs.getString("name"));
		pi.setMobile(rs.getString("mobile"));
		pi.setEmail(rs.getString("email"));
		pi.setPassword(rs.getString("password"));
		pi.setStatus(rs.getInt("status"));
		pi.setPersonStatus(rs.getInt("p_status"));
		pi.setCreateTime(rs.getTimestamp("CREATETIME").getTime());
		if (rs.getTimestamp("PASSWORD_LASTUPDATE") != null)
			pi.setPassword_lastUpdate(rs.getTimestamp("PASSWORD_LASTUPDATE")
					.getTime());
		pi.setPassword_his(rs.getString("PASSWORD_HISTORY"));
		return pi;
	}

	/**
	 * 加载所有人员进缓存
	 * 
	 * @throws SQLException
	 */
	public static void loadAllPersons() throws SQLException {
		String sql = "select * from cas_user order by id";
		DBUtil.getJdbcTemplate().query(sql, new Object[] {},
				new RowMapper<PersonInfo>() {
					public PersonInfo mapRow(ResultSet rs, int arg1)
							throws SQLException {
						PersonInfo ret = getPersonInfoFromResultSet(rs);
						Cache.PersonMap.put(ret.getId().toUpperCase(), ret);
						return ret;
					}

				});
	}

	/**
	 * 根据人员ID获取人员
	 * 
	 * @param id
	 *            人员ID
	 * @return 返回人员信息
	 */
	public static PersonInfo getPersonById(String id) {
		if (id == null || id.equals(""))
			return null;
		return Cache.PersonMap.get(id.toUpperCase());
	}

	/**
	 * 获取所有人员列表
	 * 
	 * @param includeAll
	 *            是否包含状态为删除的
	 * @return
	 * @throws SQLException
	 */
	public static List<PersonInfo> getAllPersons(boolean includeAll)
			throws SQLException {
		List<PersonInfo> ret = new ArrayList<PersonInfo>();
		ret.addAll(Cache.PersonMap.values());
		if (!includeAll) {
			deleteInactive(ret);
		}
		return ret;
	}

	/**
	 * 删除非活动的记录
	 * 
	 * @param pList
	 */
	private static void deleteInactive(List<PersonInfo> pList) {
		for (int i = 0; i < pList.size(); i++) {
			PersonInfo pi = pList.get(i);
			if (pi.getStatus() == PersonInfo.STATUS_DELETE) {
				pList.remove(i);
				i--;
			}
		}
	}

	/**
	 * 获取工作组下的所有人员
	 * 
	 * @param workgroupId
	 *            工作组ID
	 * @param includeAll
	 *            是否包括已经禁用的人员
	 * @return
	 * @throws SQLException
	 */
	public static List<PersonInfo> getPersonsOfWorkgroup(String workgroupId,
			boolean includeAll) throws SQLException {
		List<PersonInfo> ret = new ArrayList<PersonInfo>();
		List<String> ps = Cache.Relation_W_P.get(workgroupId);
		if (ps != null) {
			for (int i = 0; i < ps.size(); i++) {
				String piId = ps.get(i);
				PersonInfo pi = Cache.PersonMap.get(piId.toUpperCase());
				if (pi != null && pi.getStatus() != PersonInfo.STATUS_DELETE)
					ret.add(pi);
			}
		}
		return ret;
	}

	/**
	 * 获取组织下的所有人员
	 * 
	 * @param organizationId
	 *            组织ID
	 * @param includeAll
	 *            是否包括已经禁用的人员
	 * @return
	 * @throws SQLException
	 */
	public static List<PersonInfo> getPersonsOfOrganization(
			String organizationId, boolean includeAll) throws SQLException {
		List<PersonInfo> ret = new ArrayList<PersonInfo>();
		List<String> ps = Cache.Relation_O_P.get(organizationId);
		if (ps != null) {
			for (int i = 0; i < ps.size(); i++) {
				String piId = ps.get(i);
				PersonInfo pi = Cache.PersonMap.get(piId.toUpperCase());
				if (pi != null && pi.getStatus() != PersonInfo.STATUS_DELETE)
					ret.add(pi);
			}
		}
		return ret;
	}

	/**
	 * 获取待加入工作组的人员
	 * 
	 * @param workgroupId
	 *            工作组ID
	 * @param includeAll
	 *            是否包含状态为禁止的人员
	 * @return
	 * @throws SQLException
	 */
	public static List<PersonInfo> getPersonsForAddToWorkgroup(
			String workgroupId, boolean includeAll) throws SQLException {
		List<PersonInfo> ret = new ArrayList<PersonInfo>();
		ret.addAll(Cache.PersonMap.values());
		if (!includeAll) {
			deleteInactive(ret);
		}
		List<String> wgPersons = Cache.Relation_W_P.get(workgroupId);
		if (wgPersons != null) {
			for (int j = 0; j < wgPersons.size(); j++) {
				PersonInfo pi = Cache.PersonMap.get(wgPersons.get(j)
						.toUpperCase());
				if (pi != null) {
					ret.remove(pi);
				}
			}
		}
		return ret;
	}

	/**
	 * 返回待加入组织的人员列表
	 * 
	 * @param includeAll
	 *            是否包含状态为禁止的人员
	 * @return
	 * @throws SQLException
	 */
	public static List<PersonInfo> getPersonsForAddToOrganization(
			boolean includeAll) throws SQLException {
		List<PersonInfo> ret = new ArrayList<PersonInfo>();
		ret.addAll(Cache.PersonMap.values());
		if (!includeAll) {
			deleteInactive(ret);
		}

		for (int i = 0; i < ret.size(); i++) {
			PersonInfo pi = ret.get(i);
			if (Cache.Relation_P_O.get(pi.getId()) != null) {
				ret.remove(i);
				i--;
			}
		}
		return ret;
	}

	public static List<PersonInfo> getPersonsBySQL(String sql)
			throws SQLException {
		List<PersonInfo> ret = DBUtil.getJdbcTemplate().query(sql,
				new Object[] {}, new RowMapper<PersonInfo>() {
					public PersonInfo mapRow(ResultSet rs, int arg1)
							throws SQLException {
						PersonInfo ret = getPersonInfoFromResultSet(rs);
						Cache.PersonMap.put(ret.getId().toUpperCase(), ret);
						return ret;
					}

				});
		return ret;
	}

	public static OperationResult addPerson(PersonInfo pi, String operaName,
			String operIp) throws Exception {
		OperationResult retR = new OperationResult();

		PersonInfo pi_ = null;
		try {
			pi_ = getPersonById(pi.getId());
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		if (pi_ != null)
			throw new Exception("人员已存在id=" + pi.getId());

		String insertSQL = "insert into cas_user(oid,id,name,password,mobile,email,status,p_status,createtime,password_history,password_lastupdate) values(?,?,?,?,?,?,?,?,?,?,?)";
		String password = pi.getPassword();
		if (pi.getPassword() != null && !pi.getPassword().equals("")) {
			password = Encrypt.MD5(pi.getPassword());
			pi.setPassword(password);
		} else {
			password = Encrypt.MD5(pi.getId().toLowerCase() + "123");
			pi.setPassword(password);
		}
		pi.setCreateTime(System.currentTimeMillis());
		pi.setPassword_lastUpdate(pi.getCreateTime());
		
		DBUtil.getJdbcTemplate().update(
				insertSQL,
				new Object[] { DBUtil.getNextOid(), pi.getId().toLowerCase(),
						pi.getName(), password, pi.getMobile(), pi.getEmail(),
						pi.getStatus(), pi.getPersonStatus(),
						new Date(pi.getCreateTime()), "",
						new Date(pi.getPassword_lastUpdate()) });

		retR.setMessage("添加用户成功：(用户名:" + pi.getId().toLowerCase() + ",密码:"
				+ pi.getId().toLowerCase() + "123)");

		try {
			LogManager.addLog(operaName, operIp, LogInfo.OPER_TYPE_ADD, pi
					.getId(), pi.getName(), "对象新建");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		// 更新缓存
		pi.setId(pi.getId().toLowerCase());
		Cache.PersonMap.put(pi.getId().toUpperCase(), pi);

		// sync other person
		CacheSync.loadPersonCache();

		return retR;
	}

	public static void modifyPerson(PersonInfo pi, String operaName,
			String operIp) throws Exception {
		PersonInfo pi_ = null;
		try {
			pi_ = getPersonById(pi.getId());
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		if (pi_ != null) {
			if (pi.getPassword().equals(""))
				pi.setPassword(pi_.getPassword());
		} else {
			throw new SQLException("人员不存在id=" + pi.getId());
		}

		String updateSQL = "update cas_user set name=?,mobile=?,email=?,password=?,status=?,P_STATUS=?,PASSWORD_HISTORY=?,PASSWORD_LASTUPDATE=? where id=?";
		boolean success = true;
		try {
			DBUtil.getJdbcTemplate()
					.update(
							updateSQL,
							new Object[] { pi.getName(), pi.getMobile(),
									pi.getEmail(), pi.getPassword(),
									pi.getStatus(), pi.getPersonStatus(),
									pi.getPassword_his(),
									new Date(pi.getPassword_lastUpdate()),
									pi.getId() });
		} catch (Exception e) {
			success = false;
			throw e;
		}

		if (success) {
			try {
				LogManager.addLog(operaName, operIp, LogInfo.OPER_TYPE_UPDATE,
						pi.getId(), pi.getName(), pi_.getDiffrenceStr(pi));
			} catch (Exception e) {
				e.printStackTrace();
			}
			pi.setId(pi.getId());
			Cache.PersonMap.put(pi.getId().toUpperCase(), pi);

			// sync other person
			CacheSync.loadPersonCache();
		}
	}

	public static void modifyPassword(String userId, String oldPassword,
			String newPassword, String operaName, String operIp)
			throws Exception {
		PersonInfo pInfo = getPersonById(userId);
		if (pInfo == null)
			throw new NullPointerException("人员不存在(userId=" + userId + ")");
		if (oldPassword == null || oldPassword.equals(""))
			throw new NullPointerException("原始密码不能为空");
		if (!pInfo.getPassword().equals(Encrypt.MD5(oldPassword)))
			throw new Exception("原始密码不正确！");
		if (newPassword == null || newPassword.equals("")) {
			throw new NullPointerException("新密码不能为空");
		} else {
			String regulation = CommonInfo.SYS_CONFIG.get("pass_regulation");
			if (regulation != null && !regulation.equals("")) {
				Pattern p = Pattern.compile(regulation.trim());
				Matcher m = p.matcher(newPassword);
				if (!m.matches()) {
					String regDesc = CommonInfo.SYS_CONFIG
							.get("pass_regulation_desc");
					throw new Exception("新密码不符合组成规则:" + regDesc);
				}
			}
			String encryptedPass = Encrypt.MD5(newPassword);
			String repTime = CommonInfo.SYS_CONFIG.get("pass_repeatTime");
			int repeatTime = 0;
			if (repTime != null && !repTime.equals(""))
				repeatTime = Integer.parseInt(repTime);
			String phis = pInfo.getPassword_his();
			if (repeatTime > 0) {
				String[] passHisArray = phis.split("\\|\\_\\|");
				StringBuffer newPassHisStr = new StringBuffer();
				if (passHisArray.length >= repeatTime) {
					for (int i = 1; i < repeatTime; i++) {
						if (passHisArray[i - 1].equals(encryptedPass))
							throw new Exception("新密码不能与最近 " + repeatTime
									+ " 次的历史密码相同");
						newPassHisStr.append(passHisArray[i] + "|_|");
					}
					newPassHisStr.append(encryptedPass);
				} else {
					if (phis.indexOf(encryptedPass) != -1)
						throw new Exception("新密码不能与最近 " + repeatTime
								+ " 次的历史密码相同");
					if (phis.equals(""))
						newPassHisStr.append(encryptedPass);
					else {
						newPassHisStr.append(phis);
						newPassHisStr.append("|_|");
						newPassHisStr.append(encryptedPass);
					}
				}
				pInfo.setPassword_his(newPassHisStr.toString());

			} else {
				StringBuffer newPassHisStr = new StringBuffer();
				if (phis.equals(""))
					newPassHisStr.append(encryptedPass);
				else {
					newPassHisStr.append(phis);
					newPassHisStr.append("|_|");
					newPassHisStr.append(encryptedPass);
				}
				pInfo.setPassword_his(newPassHisStr.toString());
			}
			pInfo.setPassword_lastUpdate(System.currentTimeMillis());
			pInfo.setPassword(encryptedPass);
			modifyPerson(pInfo, operaName, operIp);
			return;
		}
	}

	public static OperationResult resetPassword(String userId,
			String operaName, String operIp) throws Exception {
		OperationResult retR = new OperationResult();
		PersonInfo pInfo = getPersonById(userId);
		if (pInfo == null) {
			throw new NullPointerException("人员不存在(userId=" + userId + ")");
		} else {
			pInfo.setPassword(Encrypt.MD5(userId + "123"));
			pInfo.setPassword_lastUpdate(System.currentTimeMillis());
			pInfo.setPassword_his("");
			modifyPerson(pInfo, operaName, operIp);
			unlockAccount(userId);
			retR.setMessage(userId + "的密码已重置为：" + userId + "123");
			return retR;
		}
	}

	public static OperationResult unlockAccount(String userId) {
		OperationResult retR = new OperationResult();
		String updateSQL = "update cas_user set login_count=0,last_login_time=null where id=?";
		DBUtil.getJdbcTemplate().update(updateSQL, new Object[] { userId });
		retR.setMessage(userId + "已被解锁");
		return retR;
	}

	public static void loginErr(String userId) {
		String updateSQL = "update cas_user set login_count=login_count+1,last_login_time=sysdate where id=?";
		DBUtil.getJdbcTemplate().update(updateSQL, new Object[] { userId });
	}

	public static boolean isSystemManager(String userName) {
		String systemManagerStr = CommonInfo.SYS_CONFIG.get("systemManager");
		if ("root".equals(userName))
			return true;
		if (systemManagerStr == null || systemManagerStr.equals(""))
			return false;
		if (systemManagerStr.endsWith("," + userName)
				|| systemManagerStr.equals(userName)
				|| systemManagerStr.startsWith(userName + ",")
				|| systemManagerStr.indexOf("," + userName + ",") != -1)
			return true;
		return false;
	}

	/**
	 * 验证密码是否需要修改 2、是否第一次登录，是则返回false 3、是否在有效期内，不是则返回false
	 * 
	 * @throws Exception
	 */
	public static boolean validatePassword(String userName) throws Exception {
		PersonInfo pInfo = getPersonById(userName);
		if (pInfo != null) {
			String period = CommonInfo.SYS_CONFIG.get("pass_period");
			int pass_period = 0;
			if (period != null && !period.equals("")) {
				pass_period = Integer.parseInt(period);
			}
			if (pass_period > 0) {
				long endtime = pInfo.getPassword_lastUpdate()
						+ (pass_period * 24 * 3600 * 1000L);
				if (endtime < System.currentTimeMillis())
					return false;
			}
			String first = CommonInfo.SYS_CONFIG.get("pass_first");
			if (first != null && first.equals("true")
					&& pInfo.getPassword_his().equals("")) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 验证用户是否允许登录 1、用户被锁定后，且不到解锁时间，不允许登录
	 * 
	 * @param userName
	 * @return 返回空""表示允许登录
	 * @throws Exception
	 */
	public static String validateLogin(final String userName, boolean sendSms) {
		String login_repeat = CommonInfo.SYS_CONFIG.get("login_repeat");
		final String login_locktime = CommonInfo.SYS_CONFIG
				.get("login_locktime");
		if (isSystemManager(userName))
			return "";
		int lp = 0;
		int llt = 0;
		if (login_repeat != null && !login_repeat.equals(""))
			lp = Integer.parseInt(login_repeat);
		if (login_locktime != null && !login_locktime.equals(""))
			llt = Integer.parseInt(login_locktime);
		if (lp <= 0)
			return "";
		String sql = "select * from cas_user where id=?";
		Object po = null;
		try {
			po = DBUtil.getJdbcTemplate().queryForObject(sql,
					new Object[] { userName }, new RowMapper<String[]>() {
						public String[] mapRow(ResultSet rs, int arg1)
								throws SQLException {
							String[] ret = new String[2];
							ret[0] = rs.getString("LOGIN_COUNT");
							if (rs.getTimestamp("LAST_LOGIN_TIME") != null)
								ret[1] = rs.getTimestamp("LAST_LOGIN_TIME")
										.getTime()
										+ "";
							else
								ret[1] = "";
							return ret;
						}
					});
		} catch (DataAccessException e) {
			// 用户不存在异常
			return "";
		}
		if (po != null) {
			String[] result = (String[]) po;
			final String loginTime = result[0];// 尝试登录的次数
			String lastLogin = result[1];// 最后登录时间
			int lt = 0;
			long ll = 0;
			if (loginTime != null && !loginTime.equals(""))
				lt = Integer.parseInt(loginTime);
			if (lastLogin != null && !lastLogin.equals(""))
				ll = Long.parseLong(lastLogin);

			// 判断锁定
			if (lt >= lp && llt <= 0 || lt >= lp
					&& System.currentTimeMillis() <= (ll + llt * 3600 * 1000L)) {
				if (sendSms) {// 发送锁定短信
					String msgSQL = "insert into cas_message(OID,RECEIVER,CONTENT,FLAG) values(seq_cas_auc.nextval,?,?,0)";
					DBUtil.getJdbcTemplate().update(msgSQL,
							new PreparedStatementSetter() {
								public void setValues(PreparedStatement ps)
										throws SQLException {
									ps.setString(1, userName);
									String content = "您的BOMC账户" + userName
											+ "由于错误登录次数太多，已被锁定。请于"
											+ login_locktime + "小时后再试，或联系管理员。";
									ps.setString(2, content);
								}
							});
				}
				return "账户已锁定，请联系管理员";
			}

			// 自动解锁
			if (lt >= lp && llt > 0
					&& System.currentTimeMillis() > (ll + llt * 3600 * 1000L)) {
				unlockAccount(userName);
				return "";
			}

		} else {
			return "用户不存在";
		}
		return "";
	}

	/**
	 * 验证密码有效期限
	 * 
	 * @param pInfo
	 * @return
	 */
	public static boolean validPassPeriod(PersonInfo pInfo) {
		if (isSystemManager(pInfo.getId()))
			return true;
		if (pInfo != null) {
			String period = CommonInfo.SYS_CONFIG.get("pass_period");
			int pass_period = 0;
			if (period != null && !period.equals("")) {
				pass_period = Integer.parseInt(period);
			}
			if (pass_period > 0) {
				long endtime = pInfo.getPassword_lastUpdate()
						+ (pass_period * 24 * 3600 * 1000L);
				if (endtime < System.currentTimeMillis())
					return false;
			}
		}
		return true;
	}

	/**
	 * 验证是否需要在首次登录后修改密码
	 * 
	 * @param pInfo
	 * @return 是否修改密码
	 */
	public static boolean validPassFirstTime(PersonInfo pInfo) {
		if (isSystemManager(pInfo.getId()))
			return false;
		if (pInfo != null) {
			String first = CommonInfo.SYS_CONFIG.get("pass_first");
			if (first != null && first.equals("true")
					&& pInfo.getPassword_his().equals("")) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 验证是否锁定
	 * 
	 * @param pInfo
	 * @param sendSms
	 * @return
	 */
	public static boolean validLocked(final PersonInfo pInfo, boolean sendSms) {
		if (isSystemManager(pInfo.getId()))
			return true;
		String login_repeat = CommonInfo.SYS_CONFIG.get("login_repeat");
		final String login_locktime = CommonInfo.SYS_CONFIG
				.get("login_locktime");
		int lp = 0;
		int llt = 0;
		if (login_repeat != null && !login_repeat.equals(""))
			lp = Integer.parseInt(login_repeat);
		else
			return true;
		if (login_locktime != null && !login_locktime.equals(""))
			llt = Integer.parseInt(login_locktime);
		else
			return true;
		if (lp <= 0)
			return true;

		String sql = "select * from cas_user where id=?";
		Object po = null;
		try {
			po = DBUtil.getJdbcTemplate().queryForObject(sql,
					new Object[] { pInfo.getId() }, new RowMapper<String[]>() {
						public String[] mapRow(ResultSet rs, int arg1)
								throws SQLException {
							String[] ret = new String[2];
							ret[0] = rs.getString("LOGIN_COUNT");
							if (rs.getTimestamp("LAST_LOGIN_TIME") != null)
								ret[1] = rs.getTimestamp("LAST_LOGIN_TIME")
										.getTime()
										+ "";
							else
								ret[1] = "";
							return ret;
						}
					});
		} catch (DataAccessException e) {
			// 用户不存在异常
			return false;
		}

		if (po != null) {
			String[] result = (String[]) po;
			final String loginTime = result[0];// 尝试登录的次数
			String lastLogin = result[1];// 最后登录时间
			int lt = 0;
			long ll = 0;
			if (loginTime != null && !loginTime.equals(""))
				lt = Integer.parseInt(loginTime);
			if (lt <= 0) // 如果登录次数是0，则返回
				return true;
			if (lastLogin != null && !lastLogin.equals(""))
				ll = Long.parseLong(lastLogin);
			
			// 判断锁定
			if ((lt >= lp) ||(lt >= lp
					&& System.currentTimeMillis() <= (ll + llt * 3600 * 1000L))) {
				if (sendSms) {// 发送锁定短信
				// String msgSQL=
				// "insert into cas_message(OID,RECEIVER,CONTENT,FLAG) values(seq_cas_auc.nextval,?,?,0)";
				// DBUtil.getJdbcTemplate().update(msgSQL, new
				// PreparedStatementSetter() {
				// public void setValues(PreparedStatement ps) throws
				// SQLException {
				// ps.setString(1, pInfo.getId());
				// String content = "您的账户" + pInfo.getId() +
				// "由于错误登录次数太多，已被锁定。请于"
				// + login_locktime + "小时后再试，或联系管理员。";
				// ps.setString(2, content);
				// }
				// });
					String content = "您的IDC平台账户" + pInfo.getId()
							+ "由于错误登录次数太多，已被锁定。请于" + login_locktime
							+ "小时后再试，或联系管理员。";
					try {
						MessageManager.sendSms(content, pInfo.getId(), "cas",
								new java.util.Date(), "system");
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				return false;
			}
			// 自动解锁
			if (lt > 0 
					|| System.currentTimeMillis() > (ll + llt * 3600 * 1000L)) {
				unlockAccount(pInfo.getId());
				return true;
			}
		}
		return true;
	}

}
