/**
 * 
 */
package com.hp.idc.itsm.security;

/**
 * 代理人信息类
 * @author <a href="mailto:lihz@lianchuang.com">FluteD</a>
 *
 */
public class FactorInfo {

	private String operName;
	
	private String factors;
	
	private boolean sendSMS;

	/**
	 * @return the operName
	 */
	public String getOperName() {
		return operName == null?"":operName;
	}

	/**
	 * @param operName the operName to set
	 */
	public void setOperName(String operName) {
		this.operName = operName;
	}

	/**
	 * @return the factors
	 */
	public String getFactors() {
		return factors == null ? "" : factors;
	}

	/**
	 * @param factors the factors to set
	 */
	public void setFactors(String factors) {
		this.factors = factors;
	}

	/**
	 * @return the sendSMS
	 */
	public boolean isSendSMS() {
		return sendSMS;
	}

	/**
	 * @param sendSMS the sendSMS to set
	 */
	public void setSendSMS(boolean sendSMS) {
		this.sendSMS = sendSMS;
	}
}
