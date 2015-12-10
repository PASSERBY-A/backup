package com.hp.idc.sms.cmpp;

import java.io.IOException;
import java.util.Date;

import com.hp.idc.sms.entity.SMSMessageEntity;
import com.huawei.smproxy.CMPPSMProxy;
import com.huawei.smproxy.comm.cmpp.message.CMPPSubmitMessage;
import com.huawei.smproxy.comm.cmpp.message.CMPPSubmitRepMessage;
import com.huawei.smproxy.util.Args;
import com.huawei.smproxy.util.Cfg;

public class IDCCMPPProxy20 extends CMPPSMProxy {

	private String[] destTerminalId;
	
	private String msgContext;
	
	private static IDCCMPPProxy20 IDCCMPPProxy = null;
	
	public static synchronized  IDCCMPPProxy20 getInstance() {
		if (IDCCMPPProxy == null){
			Args args = new Args();
			try {
				args = new Cfg("WebContent/WEB-INF/classes/config.xml", false).getArgs("CMPPConnect");
			} catch (IOException e) {
				e.printStackTrace();
			}
			IDCCMPPProxy = new IDCCMPPProxy20(args);
		}
		return IDCCMPPProxy;
	}
	
	private IDCCMPPProxy20(Args args) {
		super(args);
	}
	private static CMPPSubmitMessage getSubMsg(String[] dest_Terminal_Id,
			String msg_Content) throws Exception {
		Args args = new Args();
		args = new Cfg("config.xml", false).getArgs("CMPPSubmitMessage");
		int pk_Total = 1;
		int pk_Number = 0;
		int registered_Delivery = 0;
		int msg_Level = 0;
		String service_Id = "";
		int fee_UserType = 0;
		String fee_Terminal_Id = "";
		int fee_Terminal_Type = 0;
		int tp_Pid = 0;
		int tp_Udhi = 0;
		int msg_Fmt = 0;
		String msg_Src = "";
		String fee_Type = "";
		String fee_Code = "";
		Date valid_Time = null;
		Date at_Time = null;
		String src_Terminal_Id = "";
		// String[] dest_Terminal_Id={};
		int dest_Terminal_Type = 0;
		// byte[] msg_Content= "".getBytes();
		String LinkID = "";

		pk_Total = args.get("pk_Total", pk_Total);
		pk_Number = args.get("pk_Number", pk_Number);
		registered_Delivery = args.get("registered_Delivery",
				registered_Delivery);
		msg_Level = args.get("msg_Level", msg_Level);
		service_Id = args.get("service_Id", service_Id);
		fee_UserType = args.get("fee_UserType", fee_UserType);
		fee_Terminal_Id = args.get("fee_Terminal_Id", fee_Terminal_Id);
		fee_Terminal_Type = args.get("fee_Terminal_Type", fee_Terminal_Type);
		tp_Pid = args.get("tp_Pid", tp_Pid);
		tp_Udhi = args.get("tp_Udhi", tp_Udhi);
		msg_Fmt = args.get("msg_Fmt", msg_Fmt);
		msg_Src = args.get("msg_Src", msg_Src);
		fee_Type = args.get("fee_Type", fee_Type);
		fee_Code = args.get("fee_Code", fee_Code);
		src_Terminal_Id = args.get("src_Terminal_Id", src_Terminal_Id);
		dest_Terminal_Type = args.get("dest_Terminal_Type", dest_Terminal_Type);
		LinkID = args.get("LinkID", LinkID);

		CMPPSubmitMessage submitMsg = new CMPPSubmitMessage(pk_Total,
				pk_Number, registered_Delivery, msg_Level, service_Id,
				fee_UserType, fee_Terminal_Id, tp_Pid,
				tp_Udhi, msg_Fmt, msg_Src, fee_Type, fee_Code, valid_Time,
				at_Time, src_Terminal_Id, dest_Terminal_Id, 
				msg_Content.getBytes(), LinkID);

		return submitMsg;
	}
	
	public void startSendThread(String msgContext, String[] destTerminalId,
			int timeLong, int sleepInterval, int replNum, int missionInterval) {
		if (IDCCMPPProxy == null)
			IDCCMPPProxy = IDCCMPPProxy20.getInstance();
		SendMessageThread thread = new SendMessageThread("CMPP", timeLong, sleepInterval, replNum, missionInterval);
		thread.start();
	}
	
	 /**
	   * 发送消息线程的线程体,负责发送消息。
	 * @throws Exception 
	   */
	public SMSMessageEntity Task(SMSMessageEntity SMSMessageEntity,String destID) throws Exception {
		
		String stateDesc = IDCCMPPProxy.getConnState();
		if (stateDesc == null) {
			System.out.println("连接正常.");
		}
		if (SMSMessageEntity != null && !"".equals(SMSMessageEntity.getMsgTo())) {
			String[] destTerminalId = { destID };
			CMPPSubmitMessage submitMsg = getSubMsg(destTerminalId,
					SMSMessageEntity.getMsgContent());
			CMPPSubmitRepMessage repMsg = (CMPPSubmitRepMessage)IDCCMPPProxy
					.send(submitMsg);
			if (repMsg != null && repMsg.getResult() == 0) {
				SMSMessageEntity.setMsgSend(new Date());
				SMSMessageEntity.setMsgSms(3);
				System.out.println("短消息发送成功。");
			}
		}
		IDCCMPPProxy.close();		
		return SMSMessageEntity;
	}
	
	public void CloseProxy(){
		IDCCMPPProxy.close();
		IDCCMPPProxy = null;
	}

	public String getMsgContext() {
		return msgContext;
	}


	public void setMsgContext(String msgContext) {
		this.msgContext = msgContext;
	}

	public String[] getDestTerminalId() {
		return destTerminalId;
	}

	public void setDestTerminalId(String[] destTerminalId) {
		this.destTerminalId = destTerminalId;
	}

}
