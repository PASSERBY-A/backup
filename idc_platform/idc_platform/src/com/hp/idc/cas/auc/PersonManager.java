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
 * ��Ա������
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
	 * ����������Ա������
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
	 * ������ԱID��ȡ��Ա
	 * 
	 * @param id
	 *            ��ԱID
	 * @return ������Ա��Ϣ
	 */
	public static PersonInfo getPersonById(String id) {
		if (id == null || id.equals(""))
			return null;
		return Cache.PersonMap.get(id.toUpperCase());
	}

	/**
	 * ��ȡ������Ա�б�
	 * 
	 * @param includeAll
	 *            �Ƿ����״̬Ϊɾ����
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
	 * ɾ���ǻ�ļ�¼
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
	 * ��ȡ�������µ�������Ա
	 * 
	 * @param workgroupId
	 *            ������ID
	 * @param includeAll
	 *            �Ƿ�����Ѿ����õ���Ա
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
	 * ��ȡ��֯�µ�������Ա
	 * 
	 * @param organizationId
	 *            ��֯ID
	 * @param includeAll
	 *            �Ƿ�����Ѿ����õ���Ա
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
	 * ��ȡ�����빤�������Ա
	 * 
	 * @param workgroupId
	 *            ������ID
	 * @param includeAll
	 *            �Ƿ����״̬Ϊ��ֹ����Ա
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
	 * ���ش�������֯����Ա�б�
	 * 
	 * @param includeAll
	 *            �Ƿ����״̬Ϊ��ֹ����Ա
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
			throw new Exception("��Ա�Ѵ���id=" + pi.getId());

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

		retR.setMessage("����û��ɹ���(�û���:" + pi.getId().toLowerCase() + ",����:"
				+ pi.getId().toLowerCase() + "123)");

		try {
			LogManager.addLog(operaName, operIp, LogInfo.OPER_TYPE_ADD, pi
					.getId(), pi.getName(), "�����½�");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		// ���»���
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
			throw new SQLException("��Ա������id=" + pi.getId());
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
			throw new NullPointerException("��Ա������(userId=" + userId + ")");
		if (oldPassword == null || oldPassword.equals(""))
			throw new NullPointerException("ԭʼ���벻��Ϊ��");
		if (!pInfo.getPassword().equals(Encrypt.MD5(oldPassword)))
			throw new Exception("ԭʼ���벻��ȷ��");
		if (newPassword == null || newPassword.equals("")) {
			throw new NullPointerException("�����벻��Ϊ��");
		} else {
			String regulation = CommonInfo.SYS_CONFIG.get("pass_regulation");
			if (regulation != null && !regulation.equals("")) {
				Pattern p = Pattern.compile(regulation.trim());
				Matcher m = p.matcher(newPassword);
				if (!m.matches()) {
					String regDesc = CommonInfo.SYS_CONFIG
							.get("pass_regulation_desc");
					throw new Exception("�����벻������ɹ���:" + regDesc);
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
							throw new Exception("�����벻������� " + repeatTime
									+ " �ε���ʷ������ͬ");
						newPassHisStr.append(passHisArray[i] + "|_|");
					}
					newPassHisStr.append(encryptedPass);
				} else {
					if (phis.indexOf(encryptedPass) != -1)
						throw new Exception("�����벻������� " + repeatTime
								+ " �ε���ʷ������ͬ");
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
			throw new NullPointerException("��Ա������(userId=" + userId + ")");
		} else {
			pInfo.setPassword(Encrypt.MD5(userId + "123"));
			pInfo.setPassword_lastUpdate(System.currentTimeMillis());
			pInfo.setPassword_his("");
			modifyPerson(pInfo, operaName, operIp);
			unlockAccount(userId);
			retR.setMessage(userId + "������������Ϊ��" + userId + "123");
			return retR;
		}
	}

	public static OperationResult unlockAccount(String userId) {
		OperationResult retR = new OperationResult();
		String updateSQL = "update cas_user set login_count=0,last_login_time=null where id=?";
		DBUtil.getJdbcTemplate().update(updateSQL, new Object[] { userId });
		retR.setMessage(userId + "�ѱ�����");
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
	 * ��֤�����Ƿ���Ҫ�޸� 2���Ƿ��һ�ε�¼�����򷵻�false 3���Ƿ�����Ч���ڣ������򷵻�false
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
	 * ��֤�û��Ƿ������¼ 1���û����������Ҳ�������ʱ�䣬�������¼
	 * 
	 * @param userName
	 * @return ���ؿ�""��ʾ�����¼
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
			// �û��������쳣
			return "";
		}
		if (po != null) {
			String[] result = (String[]) po;
			final String loginTime = result[0];// ���Ե�¼�Ĵ���
			String lastLogin = result[1];// ����¼ʱ��
			int lt = 0;
			long ll = 0;
			if (loginTime != null && !loginTime.equals(""))
				lt = Integer.parseInt(loginTime);
			if (lastLogin != null && !lastLogin.equals(""))
				ll = Long.parseLong(lastLogin);

			// �ж�����
			if (lt >= lp && llt <= 0 || lt >= lp
					&& System.currentTimeMillis() <= (ll + llt * 3600 * 1000L)) {
				if (sendSms) {// ������������
					String msgSQL = "insert into cas_message(OID,RECEIVER,CONTENT,FLAG) values(seq_cas_auc.nextval,?,?,0)";
					DBUtil.getJdbcTemplate().update(msgSQL,
							new PreparedStatementSetter() {
								public void setValues(PreparedStatement ps)
										throws SQLException {
									ps.setString(1, userName);
									String content = "����BOMC�˻�" + userName
											+ "���ڴ����¼����̫�࣬�ѱ�����������"
											+ login_locktime + "Сʱ�����ԣ�����ϵ����Ա��";
									ps.setString(2, content);
								}
							});
				}
				return "�˻�������������ϵ����Ա";
			}

			// �Զ�����
			if (lt >= lp && llt > 0
					&& System.currentTimeMillis() > (ll + llt * 3600 * 1000L)) {
				unlockAccount(userName);
				return "";
			}

		} else {
			return "�û�������";
		}
		return "";
	}

	/**
	 * ��֤������Ч����
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
	 * ��֤�Ƿ���Ҫ���״ε�¼���޸�����
	 * 
	 * @param pInfo
	 * @return �Ƿ��޸�����
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
	 * ��֤�Ƿ�����
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
			// �û��������쳣
			return false;
		}

		if (po != null) {
			String[] result = (String[]) po;
			final String loginTime = result[0];// ���Ե�¼�Ĵ���
			String lastLogin = result[1];// ����¼ʱ��
			int lt = 0;
			long ll = 0;
			if (loginTime != null && !loginTime.equals(""))
				lt = Integer.parseInt(loginTime);
			if (lt <= 0) // �����¼������0���򷵻�
				return true;
			if (lastLogin != null && !lastLogin.equals(""))
				ll = Long.parseLong(lastLogin);
			
			// �ж�����
			if ((lt >= lp) ||(lt >= lp
					&& System.currentTimeMillis() <= (ll + llt * 3600 * 1000L))) {
				if (sendSms) {// ������������
				// String msgSQL=
				// "insert into cas_message(OID,RECEIVER,CONTENT,FLAG) values(seq_cas_auc.nextval,?,?,0)";
				// DBUtil.getJdbcTemplate().update(msgSQL, new
				// PreparedStatementSetter() {
				// public void setValues(PreparedStatement ps) throws
				// SQLException {
				// ps.setString(1, pInfo.getId());
				// String content = "�����˻�" + pInfo.getId() +
				// "���ڴ����¼����̫�࣬�ѱ�����������"
				// + login_locktime + "Сʱ�����ԣ�����ϵ����Ա��";
				// ps.setString(2, content);
				// }
				// });
					String content = "����IDCƽ̨�˻�" + pInfo.getId()
							+ "���ڴ����¼����̫�࣬�ѱ�����������" + login_locktime
							+ "Сʱ�����ԣ�����ϵ����Ա��";
					try {
						MessageManager.sendSms(content, pInfo.getId(), "cas",
								new java.util.Date(), "system");
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				return false;
			}
			// �Զ�����
			if (lt > 0 
					|| System.currentTimeMillis() > (ll + llt * 3600 * 1000L)) {
				unlockAccount(pInfo.getId());
				return true;
			}
		}
		return true;
	}

}
