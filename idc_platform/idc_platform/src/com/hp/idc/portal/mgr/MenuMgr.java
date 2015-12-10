package com.hp.idc.portal.mgr;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import com.hp.idc.context.util.ContextUtil;
import com.hp.idc.portal.bean.Menu;
import com.hp.idc.portal.security.Cache;
import com.hp.idc.portal.security.RuleManager;
import com.hp.idc.portal.util.DBUtil;
import com.hp.idc.unitvelog.Log;
import com.hp.idc.unitvelog.manager.ILogManager;

/**
 * �˵�������
 * 
 * @author silence
 * 
 */
public class MenuMgr {
	private static Logger logger = Logger.getLogger(MenuMgr.class);
	private ILogManager logManager = (ILogManager) ContextUtil
			.getBean("logManager");

	private static String SELECT_BY_OID = "SELECT * FROM tf_pt_menu WHERE oid = ? order by orderno asc";
	private static String INSERT_REGULAR = "insert into tf_pt_menu (oid,title,type,url,alt,role,creator,createtime, orderno) values (?,?,?,?,?,?,?,?,?)";
	private static String UPDATE_REGULAR = "update tf_pt_menu set title = ?, type = ?, url = ?, alt = ?, role = ?, orderno = ? where oid = ?";
	private static String DELETE_REGULAR = "delete from tf_pt_menu where oid = ?";

	public boolean checkOrder(int type, int orderno, int oid){
		List<Menu> list=new ArrayList<Menu>();
		list = DBUtil.getJdbcTemplate().query("select * from tf_pt_menu where type = ? and orderno=? "
				, new Object[] {type,orderno},
				new MenuRowMapper());
		if(oid<1&&list.size()>0){
			return false;
		}
		else if(oid>0&&list.size()>1){
			return false;
		}
		else if(oid>0&&list.size()==1&&Integer.parseInt((list.get(0)).getOid())!=oid){
			return false;
		}
		return true;
	}
	
	/**
	 * ���
	 * 
	 * @param bean
	 * @return
	 */
	public boolean add(final Menu bean, int userId) {
		boolean isSuccess = false;
		JdbcTemplate jdbcTemplate = DBUtil.getJdbcTemplate();
		final long oid = getSeqNextVal();
		if (oid <= 0)
			return false;
		else
			bean.setOid(oid + "");
		int ret = jdbcTemplate.update(INSERT_REGULAR,
				new PreparedStatementSetter() {
					public void setValues(PreparedStatement ps)
							throws SQLException {
						ps.setString(1, bean.getOid());
						ps.setString(2, bean.getTitle());
						ps.setString(3, bean.getType());
						ps.setString(4, bean.getUrl());
						ps.setString(5, bean.getAlt());
						ps.setString(6, bean.getRole());
						ps.setInt(7, bean.getCreator());
						ps.setTimestamp(8, new Timestamp(bean.getCreateTime()
								.getTime()));
						ps.setInt(9, bean.getOrderNo());
					}
				});
		Log log = new Log();
		log.setAppType("portal");
		log.setModuleType("�˵�����");
		log.setOperator(userId);
		if (ret > 0) {
			log.setOperatorResult(1);
			log.setContent("���Ӳ˵���" + bean.getTitle() + "���ɹ�");
			log.setBeginTime(System.currentTimeMillis());
			logger.error("�˵�����" + userId + "���Ӳ˵���" + bean.getTitle() + "���ɹ�");
			Cache.MenuMap.put(bean.getOid(), bean);// ���ݿ�����ɹ���Ի������ݽ��в���
			isSuccess = true;
		} else {
			log.setOperatorResult(0);
			log.setContent("���Ӳ˵���" + bean.getTitle() + "��ʧ��");
			log.setBeginTime(System.currentTimeMillis());
			logger.error("�˵�����" + userId + "���Ӳ˵���" + bean.getTitle() + "��ʧ��");
		}
		logManager.addLog(log);
		return isSuccess;
	}

	/**
	 * ȡ���к�
	 * 
	 * @return
	 */
	public long getSeqNextVal() {
		String sql = "select TF_PT_SEQ.NEXTVAL from dual";
		long ret = DBUtil.getJdbcTemplate().queryForLong(sql);
		return ret;
	}

	/**
	 * �޸�
	 * 
	 * @param bean
	 * @return
	 */
	public boolean update(final Menu bean, int userId) {
		boolean isSuccess = false;
		int ret = DBUtil.getJdbcTemplate().update(UPDATE_REGULAR,
				new PreparedStatementSetter() {
					public void setValues(PreparedStatement ps)
							throws SQLException {
						ps.setString(1, bean.getTitle());
						ps.setString(2, bean.getType());
						ps.setString(3, bean.getUrl());
						ps.setString(4, bean.getAlt());
						ps.setString(5, bean.getRole());
						ps.setInt(6, bean.getOrderNo());
						ps.setString(7, bean.getOid());
					}
				});
		Log log = new Log();
		log.setAppType("portal");
		log.setModuleType("�˵�����");
		log.setOperator(userId);
		if (ret > 0) {
			log.setOperatorResult(1);
			log.setContent("�޸Ĳ˵���" + bean.getOid() + "���ɹ�");
			log.setBeginTime(System.currentTimeMillis());
			logger.error("�˵�����" + userId + "�޸Ĳ˵���" + bean.getOid() + "���ɹ�");
			Cache.MenuMap.put(bean.getOid(), bean);// ���ݿ�����ɹ���Ի������ݽ��в���
			isSuccess = true;
		} else {
			log.setOperatorResult(0);
			log.setContent("�޸Ĳ˵���" + bean.getOid() + "��ʧ��");
			log.setBeginTime(System.currentTimeMillis());
			logger.error("�˵�����" + userId + "�޸Ĳ˵���" + bean.getOid() + "��ʧ��");
		}
		logManager.addLog(log);
		return isSuccess;
	}

	/**
	 * ɾ��
	 * 
	 * @param oid
	 * @return
	 */
	public boolean delete(final String oid, int userId) {
		boolean isSuccess = false;
		int ret = DBUtil.getJdbcTemplate().update(DELETE_REGULAR,
				new PreparedStatementSetter() {
					public void setValues(PreparedStatement ps)
							throws SQLException {
						ps.setString(1, oid);
					}
				});
		Log log = new Log();
		log.setAppType("portal");
		log.setModuleType("�˵�����");
		log.setOperator(userId);
		if (ret > 0) {
			log.setOperatorResult(1);
			log.setContent("ɾ���˵���" + oid + "���ɹ�");
			log.setBeginTime(System.currentTimeMillis());
			logger.error("�˵�����" + userId + "ɾ���˵���" + oid + "���ɹ�");
			Cache.MenuMap.remove(oid);// ���ݿ�����ɹ���Ի������ݽ��в���
			isSuccess = true;
		} else {
			log.setOperatorResult(0);
			log.setContent("ɾ���˵���" + oid + "��ʧ��");
			log.setBeginTime(System.currentTimeMillis());
			logger.error("�˵�����" + userId + "ɾ���˵���" + oid + "��ʧ��");
		}
		logManager.addLog(log);
		return isSuccess;
	}

	/**
	 * ����oid��ȡMenu
	 * 
	 * @param oid
	 * @return
	 */
	public Menu getBeanById(String oid) {
		Menu ret = null;
		if (Cache.MenuMap != null && Cache.MenuMap.size() > 0) {
			return Cache.MenuMap.get(oid);
		} else {
			ret = DBUtil.getJdbcTemplate().query(SELECT_BY_OID,
					new Object[] { oid }, new ResultSetExtractor<Menu>() {
						public Menu extractData(ResultSet rs)
								throws SQLException, DataAccessException {
							Menu temp = new Menu();
							if (rs.next()) {
								temp.setOid(rs.getString("oid"));
								temp.setTitle(rs.getString("title"));
								temp.setType(rs.getString("type"));
								temp.setUrl(rs.getString("url"));
								temp.setAlt(rs.getString("alt"));
								temp.setRole(rs.getString("role"));
								temp.setCreator(rs.getInt("creator"));
								temp.setCreateTime(rs
										.getTimestamp("createTime"));
								temp.setOrderNo(rs.getInt("orderno"));
								return temp;
							}
							return null;
						}
					});
		}
		return ret;
	}

	/**
	 * ����title��ȡMenu
	 * 
	 * @param oid
	 * @return
	 */
	public Menu getBeanByTitle(String title) {
		Menu ret = null;
		ret = DBUtil
				.getJdbcTemplate()
				.query(
						"SELECT * FROM tf_pt_menu WHERE title = ? order by orderno asc",
						new Object[] { title }, new ResultSetExtractor<Menu>() {
							public Menu extractData(ResultSet rs)
									throws SQLException, DataAccessException {
								Menu temp = new Menu();
								if (rs.next()) {
									temp.setOid(rs.getString("oid"));
									temp.setTitle(rs.getString("title"));
									temp.setType(rs.getString("type"));
									temp.setUrl(rs.getString("url"));
									temp.setAlt(rs.getString("alt"));
									temp.setRole(rs.getString("role"));
									temp.setCreator(rs.getInt("creator"));
									temp.setCreateTime(rs
											.getTimestamp("createTime"));
									temp.setOrderNo(rs.getInt("orderno"));
									return temp;
								}
								return null;
							}
						});
		return ret;
	}

	/**
	 * ��������Menu
	 * 
	 * @param userId
	 * @return
	 */
	public List<Menu> getBeans() {
		List<Menu> list = new ArrayList<Menu>();
		if (Cache.MenuMap != null && Cache.MenuMap.size() > 0) {
			list.addAll(Cache.MenuMap.values());
		} else {
			String sql = "SELECT * FROM tf_pt_menu order by orderno asc";
			list = DBUtil.getJdbcTemplate().query(sql, new Object[] {},
					new MenuRowMapper());
		}
		return list;
	}

	/**
	 * ���ݶ����˵�Menu ��type>0 and type<6
	 * 
	 * @param userId
	 * @return
	 */
	public List<Menu> getSubBeans() {
		String sql = "SELECT * FROM tf_pt_menu where type != -1 order by orderno,type asc";
		List<Menu> list = DBUtil.getJdbcTemplate().query(sql, new Object[] {},
				new MenuRowMapper());
		return list;
	}

	/**
	 * ����Ȩ�޻�ȡ���������ղؼе�Menu ��type>7
	 * 
	 * @param userId
	 *            ��ǰ�û���
	 * @return
	 */
	public List<Menu> getFavBeans(int userId) {
		String sql = "SELECT * FROM tf_pt_menu where type>7 order by orderno asc";
		List<Menu> list = new ArrayList<Menu>();
		List<Menu> _list = DBUtil.getJdbcTemplate().query(sql, new Object[] {},
				new MenuRowMapper());
		for (Iterator<Menu> iterator = _list.iterator(); iterator.hasNext();) {
			Menu menu = iterator.next();
			if (RuleManager.valid(String.valueOf(userId), menu.getRole(), true)) {
				list.add(menu);
			}
		}
		return list;
	}

	/**
	 * �����û�ID��ѯ����Menu
	 * 
	 * @param userId
	 *            �û���
	 * @param type
	 *            �˵�����
	 * @return
	 */
	public List<Menu> getBeanByFilter(int userId, String type) {
		List<Menu> list = new ArrayList<Menu>();
		// ʹ��sql����������, ��ʹ��collections.sort�����ڴ�����
		// if (Cache.MenuMap != null && Cache.MenuMap.size() > 0) {
		// for (Iterator<Menu> iterator = Cache.MenuMap.values().iterator();
		// iterator
		// .hasNext();) {
		// Menu menu = iterator.next();
		// if (menu.getType().equals(type)
		// && RuleManager.valid(String.valueOf(userId), menu
		// .getRole(), false)) {
		// list.add(menu);
		// }
		// }
		// } else {
		List<Menu> _list = new ArrayList<Menu>();
		StringBuffer sql = new StringBuffer();
		sql.append("select * from tf_pt_menu t where t.type = ?");
		sql.append(" order by t.orderno,t.type asc");
		_list = DBUtil.getJdbcTemplate().query(sql.toString(),
				new Object[] { type }, new MenuRowMapper());
		for (Iterator<Menu> iterator = _list.iterator(); iterator.hasNext();) {
			Menu menu = iterator.next();
			if (menu.getType().equals(type)
					&& RuleManager.valid(String.valueOf(userId),
							menu.getRole(), false)) {
				list.add(menu);
			}
		}
		// }
		return list;
	}

	/**
	 * �����û��ĵ�һ��url.
	 * �������û���¼ϵͳ��ʱ��, ����ʹ�ô�url����Ĭ��ҳ��
	 * @param userId
	 * @return
	 */
	public String getFirstUrl(int userId){
		String url = "";
		List<Menu> list = new ArrayList<Menu>();
		List<Menu> list1 = new ArrayList<Menu>();
		list = getBeanByFilter(userId, "-1");
		if (list.size() > 0) {
			list1 = getBeanByFilter(userId, list.get(0).getOid());
			if(list1.size() > 0){
				url = list1.get(0).getUrl();
			}
		}
		return url;
	}
	
	/**
	 * ��ѯ����Menu���ص��ڴ���
	 * 
	 * @return
	 */
	public static void loadAllMenus() {
		StringBuffer sql = new StringBuffer();
		sql.append("select * from tf_pt_menu t order by orderno asc");
		DBUtil.getJdbcTemplate().query(sql.toString(), new Object[] {},
				new RowMapper<Menu>() {
					public Menu mapRow(ResultSet rs, int arg1)
							throws SQLException {
						Menu menu = getMenuFromResultSet(rs);
						Cache.MenuMap.put(menu.getOid(), menu);
						return menu;
					}
				});
	}

	/**
	 * ��ѯ����Menu���ص��ڴ���
	 * 
	 * @return
	 */
	public static void syncMenus() {
		final Map<String, Menu> mm = new HashMap<String, Menu>();
		StringBuffer sql = new StringBuffer();
		sql.append("select * from tf_pt_menu t order by orderno asc");
		DBUtil.getJdbcTemplate().query(sql.toString(), new Object[] {},
				new RowMapper<Menu>() {
					public Menu mapRow(ResultSet rs, int arg1)
							throws SQLException {
						Menu menu = getMenuFromResultSet(rs);
						mm.put(menu.getOid(), menu);
						return menu;
					}
				});
		Cache.MenuMap = mm;
	}

	public static Menu getMenuFromResultSet(ResultSet rs) throws SQLException {
		Menu temp = new Menu();

		temp.setOid(rs.getString("oid"));
		temp.setTitle(rs.getString("title"));
		temp.setType(rs.getString("type"));
		temp.setUrl(rs.getString("url"));
		temp.setAlt(rs.getString("alt"));
		temp.setRole(rs.getString("role"));
		temp.setCreator(rs.getInt("creator"));
		temp.setCreateTime(rs.getTimestamp("createTime"));
		temp.setOrderNo(rs.getInt("orderno"));
		return temp;
	}

	private class MenuRowMapper implements RowMapper<Menu> {
		public Menu mapRow(ResultSet rs, int rowNum) throws SQLException {
			return getMenuFromResultSet(rs);
		}
	}
}
