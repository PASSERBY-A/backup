package com.hp.idc.itsm.security;

/**
 * 操作类型
 * @author Administrator
 *
 */
public class OperationType {
	/**
	 * 可更改
	 */
	public static int WRITE = 0;
	
	/**
	 * 只读
	 */
	public static int READONLY = 1;
	
	/**
	 * 隐藏
	 */
	public static int HIDDEN = 2;
	
	/**
	 * 禁止
	 */
	public static int DISABLED = 3;
	
	/**
	 * 只显示值
	 */
	public static int TEXTONLY = 4;
}
