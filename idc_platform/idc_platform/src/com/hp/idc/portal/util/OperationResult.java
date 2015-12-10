package com.hp.idc.portal.util;


/**
 * 表示操作的返回结果，可以获取操作的成功/失败状态以及相关的详细描述
 * @author 梅园
 *
 */
public class OperationResult {
	/**
	 * 存储操作是否成功
	 */
	protected boolean success = true;
	
	/**
	 * 存储操作结果的详细信息，默认为成功的信息
	 */
	protected String message = "您的操作已提交成功";

	/** 
	 * 获取操作结果的详细信息
	 * @return 返回操作结果的详细信息
	 */
	public String getMessage() {
		return this.message;
	}
	
	/**
	 * 设置操作结果的详细信息
	 * @param message 操作结果的详细信息
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/** 
	 * 获取操作是否成功
	 * @return 返回操作是否成功
	 */
	public boolean isSuccess() {
		return this.success;
	}
	
	/**
	 * 设置操作结果是否成功
	 * @param success 操作结果是否成功
	 */
	public void setSuccess(boolean success) {
		this.success = success;
	}
}
