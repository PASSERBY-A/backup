/**
 * 
 */
package com.hp.idc.itsm.common;

import org.dom4j.Node;

/**
 * 短信模板内容类,两种方式进行赋值：<br>
 * 1、系统启动时的加载，加载成全局变量<br>
 * 2、单个流程的短信模板配置<br>
 * 
 * 使用规则：先访问流程的单独配置，若无则访问系统全局的
 * @author <a href="mailto:lihz@lianchuang.com">FluteD</a>
 *
 */
public class SMSTemplate {

	/**
	 * 新任务到达 短信提醒格式内容
	 */
	private String smsNew = "";
	
	/**
	 * 被代理处理 短信提醒格式内容
	 */
	private String smsDealed="";
	/**
	 * 回退 短信提醒格式内容
	 */
	private String smsRollback="";
	/**
	 * 长时间未处理 短信提醒格式内容
	 */
	private String smsOvertime="";
	/**
	 * 超时 短信提醒格式内容
	 */
	private String smsOverdue="";
	/**
	 * 工单催办 短信提醒格式内容
	 */
	private String smsRemind="";
	
	/**
	 * 初始化系统默认短信模板
	 */
	public void init(){
		Consts.SMS_TEMPLATE = this;
	}
	
	/**
	 * 格式化dom节点对象
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
