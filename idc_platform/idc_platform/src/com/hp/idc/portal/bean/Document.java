package com.hp.idc.portal.bean;

import java.util.Date;

import com.hp.idc.portal.util.StringUtil;

/**
 * �����ĵ�
 * 2011.02.17
 * @author chengqp
 *
 */
public class Document {
	private String oid;//oid
	private String name;//ԭ�ļ���
	private String filesize;//�ļ���С
	private String filetype;//�ļ�����
	private String filepath;//�ļ����������·��
	private String filename;//�ļ�����
	private Date updateTime;//�ļ�������ʱ��
	private String role;//Ȩ�޿���
	private int creator;//������
	private Date createTime;//����ʱ��
	
	public String getOid() {
		return oid;
	}
	public void setOid(String oid) {
		this.oid = oid;
	}
	public String getName() {
		return StringUtil.removeNull(name);
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getFilesize() {
		return StringUtil.removeNull(filesize);
	}
	public void setFilesize(String filesize) {
		this.filesize = filesize;
	}
	public String getFiletype() {
		return StringUtil.removeNull(filetype);
	}
	public void setFiletype(String filetype) {
		this.filetype = filetype;
	}
	public String getFilepath() {
		return StringUtil.removeNull(filepath);
	}
	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}
	public String getFilename() {
		return StringUtil.removeNull(filename);
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
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
}
