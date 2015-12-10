package com.hp.idc.itsm.task;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 表示委托信息
 * 
 * @author 梅园
 * 
 */
public class DelegateInfo {

	/**
	 * 委托状态: 待接受
	 */
	public static int STATUS_OPEN = 0;

	/**
	 * 委托状态: 已接受
	 */
	public static int STATUS_ACCEPT = 1;

	/**
	 * 委托状态: 已关闭
	 */
	public static int STATUS_CLOSE = 2;

	/**
	 * 委托人
	 */
	protected String fromUser;

	/**
	 * 被委托人
	 */
	protected String toUser;

	/**
	 * 委托oid
	 */
	protected int oid = -1;

	/**
	 * 委托状态
	 */
	protected int status = STATUS_OPEN;

	/**
	 * 操作人
	 */
	protected String operName;

	/**
	 * 备注
	 */
	protected String remark = "";

	/**
	 * 开始/生效时间
	 */
	protected long startTime = -1;

	/**
	 * 结束/失效时间
	 */
	protected long endTime = -1;

	/**
	 * 创建时间
	 */
	protected long createTime = -1;

	/**
	 * 接受时间
	 */
	protected long acceptTime = -1;

	/**
	 * 关闭时间
	 */
	protected long closeTime = -1;

	/**
	 * 默认构造函数
	 * 
	 */
	public DelegateInfo() {
		// NOTING TO DO HERE
	}

	/**
	 * 根据数据库的当前记录创建委托对象
	 * 
	 * @param rs
	 * @throws SQLException
	 */
	public DelegateInfo(ResultSet rs) throws SQLException {
		setOid(rs.getInt("dg_oid"));
		if (rs.getTimestamp("dg_accept") != null)
			this.setAcceptTime(rs.getTimestamp("dg_accept").getTime());
		if (rs.getTimestamp("dg_close") != null)
			this.setCloseTime(rs.getTimestamp("dg_close").getTime());
		if (rs.getTimestamp("dg_create") != null)
			this.setCreateTime(rs.getTimestamp("dg_create").getTime());
		if (rs.getTimestamp("dg_end") != null)
			this.setEndTime(rs.getTimestamp("dg_end").getTime());
		this.setFromUser(rs.getString("dg_from_user"));
		this.setOperName(rs.getString("dg_opername"));
		this.setRemark(rs.getString("dg_remark"));
		if (rs.getTimestamp("dg_start") != null)
			this.setStartTime(rs.getTimestamp("dg_start").getTime());
		this.setStatus(rs.getInt("dg_status"));
		this.setToUser(rs.getString("dg_to_user"));
	}

	/**
	 * 获取接受时间
	 * 
	 * @return 返回接受时间
	 */
	public long getAcceptTime() {
		return this.acceptTime;
	}

	/**
	 * 设置接受时间
	 * 
	 * @param acceptTime
	 *            接受时间
	 */
	public void setAcceptTime(long acceptTime) {
		this.acceptTime = acceptTime;
	}

	/**
	 * 获取关闭时间
	 * 
	 * @return 返回关闭时间
	 */
	public long getCloseTime() {
		return this.closeTime;
	}

	/**
	 * 设置关闭时间
	 * 
	 * @param closeTime
	 *            关闭时间
	 */
	public void setCloseTime(long closeTime) {
		this.closeTime = closeTime;
	}

	/**
	 * 获取创建时间
	 * 
	 * @return 返回创建时间
	 */
	public long getCreateTime() {
		return this.createTime;
	}

	/**
	 * 设置创建时间
	 * 
	 * @param createTime
	 *            创建时间
	 */
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	/**
	 * 获取结束/失效时间
	 * 
	 * @return 返回结束/失效时间
	 */
	public long getEndTime() {
		return this.endTime;
	}

	/**
	 * 设置结束/失效时间
	 * 
	 * @param endTime
	 *            结束/失效时间
	 */
	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	/**
	 * 获取委托人
	 * 
	 * @return 返回委托人
	 */
	public String getFromUser() {
		return this.fromUser;
	}

	/**
	 * 设置委托人
	 * 
	 * @param fromUser
	 *            委托人
	 */
	public void setFromUser(String fromUser) {
		this.fromUser = fromUser;
	}

	/**
	 * 获取oid
	 * 
	 * @return 返回oid
	 */
	public int getOid() {
		return this.oid;
	}

	/**
	 * 设置oid
	 * 
	 * @param oid
	 *            oid
	 */
	public void setOid(int oid) {
		this.oid = oid;
	}

	/**
	 * 获取操作人
	 * 
	 * @return 返回操作人
	 */
	public String getOperName() {
		return this.operName;
	}

	/**
	 * 设置操作人
	 * 
	 * @param operName
	 *            操作人
	 */
	public void setOperName(String operName) {
		this.operName = operName;
	}

	/**
	 * 获取备注
	 * 
	 * @return 返回备注
	 */
	public String getRemark() {
		return this.remark;
	}

	/**
	 * 设置备注
	 * 
	 * @param remark
	 *            备注
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}

	/**
	 * 获取开始/生效时间
	 * 
	 * @return 返回开始/生效时间
	 */
	public long getStartTime() {
		return this.startTime;
	}

	/**
	 * 设置开始/生效时间
	 * 
	 * @param startTime
	 *            开始/生效时间
	 */
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	/**
	 * 获取委托状态
	 * 
	 * @return 返回委托状态
	 */
	public int getStatus() {
		return this.status;
	}

	/**
	 * 设置委托状态
	 * 
	 * @param status
	 *            委托状态
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * 获取被委托人
	 * 
	 * @return 返回被委托人
	 */
	public String getToUser() {
		return this.toUser;
	}

	/**
	 * 设置被委托人
	 * 
	 * @param toUser
	 *            被委托人
	 */
	public void setToUser(String toUser) {
		this.toUser = toUser;
	}

}
