package com.hp.idc.portal.bean;

import java.util.Date;

import com.hp.idc.portal.util.StringUtil;

/**
 * 个人文档
 * 2011.02.17
 * @author chengqp
 *
 */
public class Document {
	private String oid;//oid
	private String name;//原文件名
	private String filesize;//文件大小
	private String filetype;//文件类型
	private String filepath;//文件服务器存放路径
	private String filename;//文件名称
	private Date updateTime;//文件最后更新时间
	private String role;//权限控制
	private int creator;//创建人
	private Date createTime;//创建时间
	
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
