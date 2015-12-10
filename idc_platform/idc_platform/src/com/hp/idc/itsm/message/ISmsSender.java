package com.hp.idc.itsm.message;

/**
 * 表示发送短信的接口<br>
 * 进程启动后，通过设置MessageManager.SmsSender对象来实现短信的发送<br>
 * 如：SMPPSmsSender为本接口的实现<br>
 * 那么调用语句：MessageManager.SmsSender = new SMPPSmsSender()即可
 * @author 梅园
 *
 */
public interface ISmsSender {
	/**
	 * 发送短信
	 * @param sms 短信内容
	 * @return 成功返回null, 否则返回错误信息
	 */
	public String send(SmsMessage sms);
}
