package com.hp.idc.portal.bean;

import com.hp.idc.portal.util.StringUtil;

/**
 * 布局模版类
 * 2011.02.17
 * @author chengqp
 *
 */
public class Layout {
	private String oid;
	private String name;//布局名称
	private String path;//布局所对应模板在服务器上的相对路径
	private int areaNum;//布局模板对应的布局数
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
	public String getPath() {
		return StringUtil.removeNull(path);
	}
	public void setPath(String path) {
		this.path = path;
	}
	public int getAreaNum() {
		return areaNum;
	}
	public void setAreaNum(int areaNum) {
		this.areaNum = areaNum;
	}
}
