/**
 * 
 */
package com.hp.idc.itsm.common;

import org.dom4j.Node;

/**
 * ����ģ��������,���ַ�ʽ���и�ֵ��<br>
 * 1��ϵͳ����ʱ�ļ��أ����س�ȫ�ֱ���<br>
 * 2���������̵Ķ���ģ������<br>
 * 
 * ʹ�ù����ȷ������̵ĵ������ã����������ϵͳȫ�ֵ�
 * @author <a href="mailto:lihz@lianchuang.com">FluteD</a>
 *
 */
public class SMSTemplate {

	/**
	 * �����񵽴� �������Ѹ�ʽ����
	 */
	private String smsNew = "";
	
	/**
	 * �������� �������Ѹ�ʽ����
	 */
	private String smsDealed="";
	/**
	 * ���� �������Ѹ�ʽ����
	 */
	private String smsRollback="";
	/**
	 * ��ʱ��δ���� �������Ѹ�ʽ����
	 */
	private String smsOvertime="";
	/**
	 * ��ʱ �������Ѹ�ʽ����
	 */
	private String smsOverdue="";
	/**
	 * �����߰� �������Ѹ�ʽ����
	 */
	private String smsRemind="";
	
	/**
	 * ��ʼ��ϵͳĬ�϶���ģ��
	 */
	public void init(){
		Consts.SMS_TEMPLATE = this;
	}
	
	/**
	 * ��ʽ��dom�ڵ����
	 * @param smsTemplateNode
	 */
	public void parse(Node smsTemplateNode) {
		if(smsTemplateNode == null)
			return;
		Node smsNewNode = smsTemplateNode.selectSingleNode("./new");
		if (smsNewNode!=null)
			this.smsNew = smsNewNode.getText();
		Node smsDealedNode = smsTemplateNode.selectSingleNode("./dealed");
		if (smsDealedNode!=null)
			this.smsDealed = smsDealedNode.getText();
		Node smsRollbackNode = smsTemplateNode.selectSingleNode("./rollback");
		if (smsRollbackNode!=null)
			this.smsRollback = smsRollbackNode.getText();
		Node smsOvertimeNode = smsTemplateNode.selectSingleNode("./overtime");
		if (smsOvertimeNode!=null)
			this.smsOvertime = smsOvertimeNode.getText();
		Node smsOverdueNode = smsTemplateNode.selectSingleNode("./overdue");
		if (smsOverdueNode!=null)
			this.smsOverdue = smsOverdueNode.getText();
		Node smsRemindNode = smsTemplateNode.selectSingleNode("./remind");
		if (smsRemindNode!=null)
			this.smsRemind = smsRemindNode.getText();
	}
	
	/**
	 * @return the smsNew
	 */
	public String getSmsNew() {
		return smsNew;
	}
	/**
	 * @param smsNew the smsNew to set
	 */
	public void setSmsNew(String smsNew) {
		this.smsNew = smsNew;
	}
	/**
	 * @return the smsDealed
	 */
	public String getSmsDealed() {
		return smsDealed;
	}
	/**
	 * @param smsDealed the smsDealed to set
	 */
	public void setSmsDealed(String smsDealed) {
		this.smsDealed = smsDealed;
	}
	/**
	 * @return the smsRollback
	 */
	public String getSmsRollback() {
		return smsRollback;
	}
	/**
	 * @param smsRollback the smsRollback to set
	 */
	public void setSmsRollback(String smsRollback) {
		this.smsRollback = smsRollback;
	}
	/**
	 * @return the smsOvertime
	 */
	public String getSmsOvertime() {
		return smsOvertime;
	}
	/**
	 * @param smsOvertime the smsOvertime to set
	 */
	public void setSmsOvertime(String smsOvertime) {
		this.smsOvertime = smsOvertime;
	}
	/**
	 * @return the smsOverdue
	 */
	public String getSmsOverdue() {
		return smsOverdue;
	}
	/**
	 * @param smsOverdue the smsOverdue to set
	 */
	public void setSmsOverdue(String smsOverdue) {
		this.smsOverdue = smsOverdue;
	}
	/**
	 * @return the smsRemind
	 */
	public String getSmsRemind() {
		return smsRemind;
	}
	/**
	 * @param smsRemind the smsRemind to set
	 */
	public void setSmsRemind(String smsRemind) {
		this.smsRemind = smsRemind;
	}
	
}
