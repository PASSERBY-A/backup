package com.hp.idc.portal.bean;

import java.util.Date;

import com.hp.idc.portal.util.StringUtil;

/**
 * ��ͼ��
 * @author chengqp
 *
 */
public class View {
	private String oid;//oid
	private String name;//��ͼ����
	private String layoutId;//����ID
	/**
	 * �������Ľڵ���Ϣ
	 * �磺<nodes>
	 * 		<node nodeId="1040" areaId="2"/>
	 * 		<node..../>
	 * 		...
	 * 		</nodes>
	 */
	private String nodes;
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
	public String getLayoutId() {
		return StringUtil.removeNull(layoutId);
	}
	public void setLayoutId(String layoutId) {
		this.layoutId = layoutId;
	}
	public String getNodes() {
		return StringUtil.removeNull(nodes);
	}
	public void setNodes(String nodes) {
		this.nodes = nodes;
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
