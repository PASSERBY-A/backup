package com.hp.idc.portal.bean;

import java.util.Date;

import com.hp.idc.portal.util.StringUtil;

/**
 * �˵���
 * 2011.02.17
 * @author chengqp
 *
 */
public class Menu {
	private String oid;
	private String title;//ģ������
	private String type;//ģ�����ͣ�[1,'�ۺ���Ӫ�Ż�'],[2,'��ع�������'],[3,'ҵ���������'],[4,'��ά��������'],[5,'��Ӫ��������']��
	private String url;//ģ������
	private String alt;//��ʾ��Ϣ
	private String role;//Ȩ���ֶ�
	private int creator;//������
	private Date createTime;//����ʱ��
	private int orderNo;
	
	public String getOid() {
		return oid;
	}
	public void setOid(String oid) {
		this.oid = oid;
	}
	public String getTitle() {
		return StringUtil.removeNull(title);
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getType() {
		return StringUtil.removeNull(type);
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getUrl() {
		return StringUtil.removeNull(url);
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getAlt() {
		return StringUtil.removeNull(alt);
	}
	public void setAlt(String alt) {
		this.alt = alt;
	}
	public String getRole() {
		return StringUtil.removeNull(role);
	}
	public void setRole(String role) {
		this.role = role;
	}
	public int getCreator() {
		return creator;
	}
	public void setCreator(int creator) {
		this.creator = creator;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public void setOrderNo(int orderNo) {
		this.orderNo = orderNo;
	}
	public int getOrderNo() {
		return orderNo;
	}
}
