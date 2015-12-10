package com.hp.idc.itsm.task;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * ��ʾί����Ϣ
 * 
 * @author ÷԰
 * 
 */
public class DelegateInfo {

	/**
	 * ί��״̬: ������
	 */
	public static int STATUS_OPEN = 0;

	/**
	 * ί��״̬: �ѽ���
	 */
	public static int STATUS_ACCEPT = 1;

	/**
	 * ί��״̬: �ѹر�
	 */
	public static int STATUS_CLOSE = 2;

	/**
	 * ί����
	 */
	protected String fromUser;

	/**
	 * ��ί����
	 */
	protected String toUser;

	/**
	 * ί��oid
	 */
	protected int oid = -1;

	/**
	 * ί��״̬
	 */
	protected int status = STATUS_OPEN;

	/**
	 * ������
	 */
	protected String operName;

	/**
	 * ��ע
	 */
	protected String remark = "";

	/**
	 * ��ʼ/��Чʱ��
	 */
	protected long startTime = -1;

	/**
	 * ����/ʧЧʱ��
	 */
	protected long endTime = -1;

	/**
	 * ����ʱ��
	 */
	protected long createTime = -1;

	/**
	 * ����ʱ��
	 */
	protected long acceptTime = -1;

	/**
	 * �ر�ʱ��
	 */
	protected long closeTime = -1;

	/**
	 * Ĭ�Ϲ��캯��
	 * 
	 */
	public DelegateInfo() {
		// NOTING TO DO HERE
	}

	/**
	 * �������ݿ�ĵ�ǰ��¼����ί�ж���
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
	 * ��ȡ����ʱ��
	 * 
	 * @return ���ؽ���ʱ��
	 */
	public long getAcceptTime() {
		return this.acceptTime;
	}

	/**
	 * ���ý���ʱ��
	 * 
	 * @param acceptTime
	 *            ����ʱ��
	 */
	public void setAcceptTime(long acceptTime) {
		this.acceptTime = acceptTime;
	}

	/**
	 * ��ȡ�ر�ʱ��
	 * 
	 * @return ���عر�ʱ��
	 */
	public long getCloseTime() {
		return this.closeTime;
	}

	/**
	 * ���ùر�ʱ��
	 * 
	 * @param closeTime
	 *            �ر�ʱ��
	 */
	public void setCloseTime(long closeTime) {
		this.closeTime = closeTime;
	}

	/**
	 * ��ȡ����ʱ��
	 * 
	 * @return ���ش���ʱ��
	 */
	public long getCreateTime() {
		return this.createTime;
	}

	/**
	 * ���ô���ʱ��
	 * 
	 * @param createTime
	 *            ����ʱ��
	 */
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	/**
	 * ��ȡ����/ʧЧʱ��
	 * 
	 * @return ���ؽ���/ʧЧʱ��
	 */
	public long getEndTime() {
		return this.endTime;
	}

	/**
	 * ���ý���/ʧЧʱ��
	 * 
	 * @param endTime
	 *            ����/ʧЧʱ��
	 */
	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	/**
	 * ��ȡί����
	 * 
	 * @return ����ί����
	 */
	public String getFromUser() {
		return this.fromUser;
	}

	/**
	 * ����ί����
	 * 
	 * @param fromUser
	 *            ί����
	 */
	public void setFromUser(String fromUser) {
		this.fromUser = fromUser;
	}

	/**
	 * ��ȡoid
	 * 
	 * @return ����oid
	 */
	public int getOid() {
		return this.oid;
	}

	/**
	 * ����oid
	 * 
	 * @param oid
	 *            oid
	 */
	public void setOid(int oid) {
		this.oid = oid;
	}

	/**
	 * ��ȡ������
	 * 
	 * @return ���ز�����
	 */
	public String getOperName() {
		return this.operName;
	}

	/**
	 * ���ò�����
	 * 
	 * @param operName
	 *            ������
	 */
	public void setOperName(String operName) {
		this.operName = operName;
	}

	/**
	 * ��ȡ��ע
	 * 
	 * @return ���ر�ע
	 */
	public String getRemark() {
		return this.remark;
	}

	/**
	 * ���ñ�ע
	 * 
	 * @param remark
	 *            ��ע
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}

	/**
	 * ��ȡ��ʼ/��Чʱ��
	 * 
	 * @return ���ؿ�ʼ/��Чʱ��
	 */
	public long getStartTime() {
		return this.startTime;
	}

	/**
	 * ���ÿ�ʼ/��Чʱ��
	 * 
	 * @param startTime
	 *            ��ʼ/��Чʱ��
	 */
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	/**
	 * ��ȡί��״̬
	 * 
	 * @return ����ί��״̬
	 */
	public int getStatus() {
		return this.status;
	}

	/**
	 * ����ί��״̬
	 * 
	 * @param status
	 *            ί��״̬
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * ��ȡ��ί����
	 * 
	 * @return ���ر�ί����
	 */
	public String getToUser() {
		return this.toUser;
	}

	/**
	 * ���ñ�ί����
	 * 
	 * @param toUser
	 *            ��ί����
	 */
	public void setToUser(String toUser) {
		this.toUser = toUser;
	}

}
