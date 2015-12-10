package com.cmsz.cloudplatform.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import com.hp.core.model.BaseEntity;
@Entity(name="T_TEMPLATE_ACCOUNT")
public class TemplateAccount extends BaseEntity {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5861484597214755884L;

	@SequenceGenerator(sequenceName="T_TEMPLATE_ACCOUNT_ID_SEQ",name="T_TEMPLATE_ACCOUNT_ID_SEQ_GEN")
	@Id
	@GeneratedValue(generator="T_TEMPLATE_ACCOUNT_ID_SEQ_GEN",strategy=GenerationType.SEQUENCE)
	@Column(name="pk_T_TEMPLATE_ACCOUNT_ID",nullable=false)
	private Long id = null;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name="template_id", nullable=false)
	private String templateId ="";
	
	
	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	@Column(name="account_password", nullable=false, updatable=true)
	private String password;
	
	@Column(name="account_name", nullable=false,updatable=true)
	private String accountName;
	
	@Column(name="account_type", nullable=false)
	/**
	 * 0    OS 表示OS帐户
	 * 1    FTP 表示SFTP帐户
	 */
	private String accountType ;

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}
	
	public String toString() {
		return "BaseEntity [createdBy=" + createdBy + ", createdOn="
				+ createdOn + ", modifiedBy=" + modifiedBy + ", modifiedOn="
				+ modifiedOn + 
				"]";
	}
	
	
}
