package com.hp.idc.portal.bean;

import com.hp.idc.portal.util.StringUtil;

/**
 * ����ģ����
 * 2011.02.17
 * @author chengqp
 *
 */
public class Layout {
	private String oid;
	private String name;//��������
	private String path;//��������Ӧģ���ڷ������ϵ����·��
	private int areaNum;//����ģ���Ӧ�Ĳ�����
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
