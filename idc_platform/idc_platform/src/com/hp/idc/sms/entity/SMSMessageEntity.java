package com.hp.idc.sms.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the ITSM_MESSAGE database table.
 * 
 */
@Entity
@Table(name="ITSM_MESSAGE")
public class SMSMessageEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="MSG_OID", unique=true, nullable=false, precision=22)
	private long msgOid;
    // 消息内容
	@Column(name="MSG_CONTENT", nullable=false, length=4000)
	private String msgContent;

    @Temporal( TemporalType.DATE)
	@Column(name="MSG_CREATE", nullable=false)
	private Date msgCreate;
    
	@Column(name="MSG_EMAIL", nullable=false, precision=22)
	private int msgEmail;

	@Column(name="MSG_FROM", nullable=false, length=75)
	private String msgFrom;

	@Column(name="MSG_INTERNAL", nullable=false, precision=22)
	private int msgInternal;

	@Column(name="MSG_MODULE", length=75)
	private String msgModule;

	@Column(name="MSG_MODULE_DATA", length=255)
	private String msgModuleData;

    @Temporal( TemporalType.DATE)
	@Column(name="MSG_PROCESS")
	private Date msgProcess;
    //备注(报错后结果)
	@Column(name="MSG_REMARK", length=1024)
	private String msgRemark;

    @Temporal( TemporalType.DATE)
	@Column(name="MSG_SEND", nullable=false)
	private Date msgSend;
    //1表示需要发送,2表示处理中,3表示处理成功,4表示处理失败
	@Column(name="MSG_SMS", nullable=false, precision=22)
	private int msgSms;

	@Column(name="MSG_TITLE", length=255)
	private String msgTitle;

	@Column(name="MSG_TO", nullable=false, length=75)
	private String msgTo;

	public long getMsgOid() {
		return this.msgOid;
	}

	public void setMsgOid(long msgOid) {
		this.msgOid = msgOid;
	}

	public String getMsgContent() {
		return this.msgContent;
	}

	public void setMsgContent(String msgContent) {
		this.msgContent = msgContent;
	}

	public Date getMsgCreate() {
		return this.msgCreate;
	}

	public void setMsgCreate(Date msgCreate) {
		this.msgCreate = msgCreate;
	}

	public int getMsgEmail() {
		return this.msgEmail;
	}

	public void setMsgEmail(int msgEmail) {
		this.msgEmail = msgEmail;
	}

	public String getMsgFrom() {
		return this.msgFrom;
	}

	public void setMsgFrom(String msgFrom) {
		this.msgFrom = msgFrom;
	}

	public int getMsgInternal() {
		return this.msgInternal;
	}

	public void setMsgInternal(int msgInternal) {
		this.msgInternal = msgInternal;
	}

	public String getMsgModule() {
		return this.msgModule;
	}

	public void setMsgModule(String msgModule) {
		this.msgModule = msgModule;
	}

	public String getMsgModuleData() {
		return this.msgModuleData;
	}

	public void setMsgModuleData(String msgModuleData) {
		this.msgModuleData = msgModuleData;
	}

	public Date getMsgProcess() {
		return this.msgProcess;
	}

	public void setMsgProcess(Date msgProcess) {
		this.msgProcess = msgProcess;
	}

	public String getMsgRemark() {
		return this.msgRemark;
	}

	public void setMsgRemark(String msgRemark) {
		this.msgRemark = msgRemark;
	}

	public Date getMsgSend() {
		return this.msgSend;
	}

	public void setMsgSend(Date msgSend) {
		this.msgSend = msgSend;
	}

	public int getMsgSms() {
		return this.msgSms;
	}

	public void setMsgSms(int msgSms) {
		this.msgSms = msgSms;
	}

	public String getMsgTitle() {
		return this.msgTitle;
	}

	public void setMsgTitle(String msgTitle) {
		this.msgTitle = msgTitle;
	}

	public String getMsgTo() {
		return this.msgTo;
	}

	public void setMsgTo(String msgTo) {
		this.msgTo = msgTo;
	}

}