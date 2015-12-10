package com.hp.idc.resm.cache;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;

import com.hp.idc.resm.util.DbUtil;
import com.hp.idc.resm.util.IDumpObject;

/**
 * 表示可缓存的对象
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public abstract class CacheableObject implements Cloneable, IDumpObject,
		Serializable {

	/**
	 * 序列化UID
	 */
	private static final long serialVersionUID = -7274055434166781175L;

	/**
	 * 是否添加到缓存中了，添加到缓存中的对象不能更改，应该调一次clone方法，再进行修改
	 */
	private boolean cached = false;

	/**
	 * 设置是否添加到缓存中了
	 * 
	 * @param cached
	 *            是否添加到缓存中了
	 */
	public void setCached(boolean cached) {
		this.cached = cached;

	}

	/**
	 * 是否添加到缓存中了，添加到缓存中的对象不能更改，应该调一次clone方法，再进行修改
	 * 
	 * @return true=在缓存中，false=不在缓存中
	 */
	public boolean isCached() {
		return this.cached;
	}

	/**
	 * 在派生类中必须重写此类，实现缓存对象的克隆
	 */
	@Override
	public Object clone() throws CloneNotSupportedException {
		CacheableObject obj = (CacheableObject) super.clone();
		obj.setCached(false); // clone的对象不在缓存中
		return obj;
	}
	
	/**
	 * 在派生类中重写此类，在远程对象序列化成对象后调用
	 */
	public void onDeserialize() {
		// 在派生类中重写此类
	}

	/**
	 * 返回对象的主键
	 * 
	 * @return 对象的主键
	 */
	public abstract String getPrimaryKey();

	/**
	 * 返回数据库表名
	 * 
	 * @return 数据库表名
	 */
	public abstract String getDatabaseTable();

	/**
	 * 返回数据库表的主键字段
	 * 
	 * @return 数据库表的主键字段
	 */
	public abstract String getPrimaryKeyField();

	/**
	 * 更新到对象数据集中
	 * 
	 * @param conn
	 *            数据库连接
	 * @param rs
	 *            数据集
	 * @throws Exception
	 *             处理过程中的异常
	 */
	public abstract void updateResultSet(Connection conn, ResultSet rs)
			throws Exception;

	/**
	 * 从数据集中获取数据
	 * 
	 * @param rs
	 *            数据集
	 * @throws Exception
	 *             处理过程中的异常
	 */
	public abstract void readFromResultSet(ResultSet rs) throws Exception;


	/**
	 * 返回查找数据的SQL语句
	 * 
	 * @param conn
	 *            数据库连接
	 * @return 查找数据的SQL语句
	 * @throws Exception
	 *             处理过程中的异常
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
	 * 将对象同步到数据库中
	 * 
	 * @param conn
	 *            数据库连接
	 * @throws Exception
	 *             处理过程中的异常
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
	 * 从数据库中删除
	 * 
	 * @param conn
	 *            数据库连接
	 * @throws Exception
	 *             处理过程中的异常
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
		System.out.println(getClass().getName() + "没有实现dump()方法。");
	}

	/**
	 * 检查set方法，在每个set方法的开始处调用，防止操作已经缓存中的对象
	 * 
	 * @throws CacheException
	 *             对象已被缓存，但调用set方法时发生
	 */
	protected void checkSet() throws CacheException {
		if (this.cached)
			throw new CacheException("缓存对象不能被修改");
	}
	
	/**
	 * 获取日志记录的扩展信息，供统一日志模块使用，在派生类中重写此函数
	 * @return 扩展信息
	 */
	public Map<String, String> getLogExtendInfo() {
		return null;
	}
}
