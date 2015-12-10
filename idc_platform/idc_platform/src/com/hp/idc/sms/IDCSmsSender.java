/*
 * @(#)IDCSmsSender.java
 *
 * Copyright (c) 2011, hp and/or its affiliates. All rights reserved.
 * HP PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.hp.idc.sms;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import com.hp.idc.itsm.message.ISmsSender;
import com.hp.idc.itsm.message.MessageManager;
import com.hp.idc.itsm.message.SmsMessage;
import com.huawei.smproxy.comm.cmpp.message.CMPPSubmitMessage;
import com.huawei.smproxy.util.Args;

/**
 * 
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * @version 1.0, 8:41:58 PM Oct 26, 2011
 * 
 */

public class IDCSmsSender implements ISmsSender {

	private static final int SMS_MAX_LENGTH = 140;

	private Logger logger = Logger.getLogger(IDCSmsSender.class);

	private static Args args = new Args();

	public void init() throws IOException {
		MessageManager.SmsSender = new IDCSmsSender();
		IDCSmsSender.args = Env.getConfig().getArgs("CMPPSubmitMessage");
		System.out.println("**************Cmpp Config*****************");
		logger.info(IDCSmsSender.args.toString());
		System.out.println("******************************************");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hp.idc.itsm.message.ISmsSender#send(com.hp.idc.itsm.message.SmsMessage
	 * )
	 */
	@Override
	public String send(SmsMessage sms) {
		List<CMPPSubmitMessage> list = new LinkedList<CMPPSubmitMessage>();
		try {
			byte[] content = sms.getContent().getBytes("UTF-16BE");
			int total = content.length;
			float _n = (float) total / SMS_MAX_LENGTH;
			int n = total / SMS_MAX_LENGTH;
			if (_n > n)
				n = n + 1;
			for (int i = 0; i < n; i++) {
				byte[] _content = Arrays.copyOfRange(content, i*SMS_MAX_LENGTH, (i+1)*140>total?total:(i+1)*140);
				CMPPSubmitMessage msg = new CMPPSubmitMessage(IDCSmsSender.args
						.get("pk_Total", 1), IDCSmsSender.args.get("pk_Number",
						1), IDCSmsSender.args.get("registered_Delivery", 0),
						IDCSmsSender.args.get("msg_Level", 1),
						IDCSmsSender.args.get("service_Id", "10658576"),
						IDCSmsSender.args.get("fee_UserType", 1),
						IDCSmsSender.args.get("fee_Terminal_Id", ""),
						IDCSmsSender.args.get("tp_Pid", 0), IDCSmsSender.args
								.get("tp_Udhi", 0), IDCSmsSender.args.get(
								"msg_Fmt", 15), IDCSmsSender.args.get(
								"msg_Src", ""), IDCSmsSender.args.get(
								"fee_Type", ""), IDCSmsSender.args.get(
								"fee_Code", ""), new Date(System
								.currentTimeMillis()
								+ sms.getLiveTime()), sms.getAtTime(),
						IDCSmsSender.args.get("src_Terminal_Id", ""),
						new String[] { sms.getReceiver() }, _content,
						IDCSmsSender.args.get("LinkID", ""));
				list.add(msg);
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		for (CMPPSubmitMessage msg : list) {
			if (msg != null) {
				logger.info(msg);
				if (WebSMSender.getInstance().send(msg) == false)
					return "调用短信网关出错,信息发送失败!";
			}
		}
		return null;
	}

	/**
	 * UCS2解码
	 * 
	 * @param src
	 *            UCS2 源串
	 * @return 解码后的UTF-16BE字符串
	 */
	public String DecodeUCS2(String src) {
		byte[] bytes = new byte[src.length() / 2];

		for (int i = 0; i < src.length(); i += 2) {
			bytes[i / 2] = (byte) (Integer
					.parseInt(src.substring(i, i + 2), 16));
		}
		String reValue = null;
		try {
			reValue = new String(bytes, "UTF-16BE");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return reValue;

	}

	/**
	 * UCS2编码
	 * 
	 * @param src
	 *            UTF-16BE编码的源串
	 * @return 编码后的UCS2串
	 */
	public String EncodeUCS2(String src) {

		byte[] bytes = null;
		try {
			bytes = src.getBytes("UTF-16BE");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		StringBuffer reValue = new StringBuffer();
		StringBuffer tem = new StringBuffer();
		for (int i = 0; i < bytes.length; i++) {
			tem.delete(0, tem.length());
			tem.append(Integer.toHexString(bytes[i] & 0xFF));
			if (tem.length() == 1) {
				tem.insert(0, '0');
			}
			reValue.append(tem);
		}
		return reValue.toString().toUpperCase();
	}
}
