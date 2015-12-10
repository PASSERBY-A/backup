package com.cmsz.cloudplatform.utils;

/*
 * 业务异常 code 如 01_S_00001  
 * 由3位字符串组成加
 * 第一位 由两个数字组成，表示平台，01表示IAAS ,02 表示PAAS
 * 第二位 由一个字母字符组成，表示系常出错层次，S 表示Service层，D表示DAO层
 * 第三位 异常编号   5个阿拉拍数字组成
 */
public final class ServiceErrorCode {
	
	/*
	 * 参数错误
	 */
	public static String PARAMETER_EXCEPTION = "01_S_00001"; 
}
