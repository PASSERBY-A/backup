package com.hp.idc.resm.cache;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;

import com.hp.idc.resm.util.DbUtil;
import com.hp.idc.resm.util.IDumpObject;

/**
 * ��ʾ�ɻ���Ķ���
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public abstract class CacheableObject implements Cloneable, IDumpObject,
		Serializable {

	/**
	 * ���л�UID
	 */
	private static final long serialVersionUID = -7274055434166781175L;

	/**
	 * �Ƿ���ӵ��������ˣ���ӵ������еĶ����ܸ��ģ�Ӧ�õ�һ��clone�������ٽ����޸�
	 */
	private boolean cached = false;

	/**
	 * �����Ƿ���ӵ���������
	 * 
	 * @param cached
	 *            �Ƿ���ӵ���������
	 */
	public void setCached(boolean cached) {
		this.cached = cached;

	}

	/**
	 * �Ƿ���ӵ��������ˣ���ӵ������еĶ����ܸ��ģ�Ӧ�õ�һ��clone�������ٽ����޸�
	 * 
	 * @return true=�ڻ����У�false=���ڻ�����
	 */
	public boolean isCached() {
		return this.cached;
	}

	/**
	 * ���������б�����д���࣬ʵ�ֻ������Ŀ�¡
	 */
	@Override
	public Object clone() throws CloneNotSupportedException {
		CacheableObject obj = (CacheableObject) super.clone();
		obj.setCached(false); // clone�Ķ����ڻ�����
		return obj;
	}
	
	/**
	 * ������������д���࣬��Զ�̶������л��ɶ�������
	 */
	public void onDeserialize() {
		// ������������д����
	}

	/**
	 * ���ض��������
	 * 
	 * @return ���������
	 */
	public abstract String getPrimaryKey();

	/**
	 * �������ݿ����
	 * 
	 * @return ���ݿ����
	 */
	public abstract String getDatabaseTable();

	/**
	 * �������ݿ��������ֶ�
	 * 
	 * @return ���ݿ��������ֶ�
	 */
	public abstract String getPrimaryKeyField();

	/**
	 * ���µ��������ݼ���
	 * 
	 * @param conn
	 *            ���ݿ�����
	 * @param rs
	 *            ���ݼ�
	 * @throws Exception
	 *             ��������е��쳣
	 */
	public abstract void updateResultSet(Connection conn, ResultSet rs)
			throws Exception;

	/**
	 * �����ݼ��л�ȡ����
	 * 
	 * @param rs
	 *            ���ݼ�
	 * @throws Exception
	 *             ��������е��쳣
	 */
	public abstract void readFromResultSet(ResultSet rs) throws Exception;


	/**
	 * ���ز������ݵ�SQL���
	 * 
	 * @param conn
	 *            ���ݿ�����
	 * @return �������ݵ�SQL���
	 * @throws Exception
	 *             ��������е��쳣
	 */
	protected PreparedStatement getStatement(Connection conn) throws Exception {
		PreparedStatement stmt = null;
		stmt = conn.prepareStatement("select t.* from " + getDatabaseTable()
				+ " t where " + getPrimaryKeyField() + "=?",
				ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
		stmt.setString(1, getPrimaryKey());
		return stmt;
	}

	/**
	 * ������ͬ�������ݿ���
	 * 
	 * @param conn
	 *            ���ݿ�����
	 * @throws Exception
	 *             ��������е��쳣
	 */
	public void syncToDatabase(Connection conn) throws Exception {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = getStatement(conn);
			rs = stmt.executeQuery();
			if (rs.next()) {
				updateResultSet(conn, rs);
				rs.updateRow();
			} else {
				rs.moveToInsertRow();
				updateResultSet(conn, rs);
				rs.insertRow();
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			DbUtil.free(null, stmt, rs);
		}
	}


	/**
	 * �����ݿ���ɾ��
	 * 
	 * @param conn
	 *            ���ݿ�����
	 * @throws Exception
	 *             ��������е��쳣
	 */
	public void removeFromDatabase(Connection conn) throws Exception {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = getStatement(conn);
			rs = stmt.executeQuery();
			while (rs.next()) {
				rs.deleteRow();
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			DbUtil.free(null, stmt, rs);
		}
	}

	public void dump() {
		System.out.println(getClass().getName() + "û��ʵ��dump()������");
	}

	/**
	 * ���set��������ÿ��set�����Ŀ�ʼ�����ã���ֹ�����Ѿ������еĶ���
	 * 
	 * @throws CacheException
	 *             �����ѱ����棬������set����ʱ����
	 */
	protected void checkSet() throws CacheException {
		if (this.cached)
			throw new CacheException("��������ܱ��޸�");
	}
	
	/**
	 * ��ȡ��־��¼����չ��Ϣ����ͳһ��־ģ��ʹ�ã�������������д�˺���
	 * @return ��չ��Ϣ
	 */
	public Map<String, String> getLogExtendInfo() {
		return null;
	}
}
