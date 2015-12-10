package com.hp.idc.sms;

import java.io.IOException ;

import com.huawei.smproxy.CMPPSMProxy ;
import com.huawei.smproxy.comm.cmpp.message.CMPPDeliverMessage ;
import com.huawei.smproxy.comm.cmpp.message.CMPPDeliverRepMessage ;
import com.huawei.smproxy.comm.cmpp.message.CMPPMessage ;
import com.huawei.smproxy.comm.cmpp.message.CMPPSubmitMessage ;
import com.huawei.smproxy.comm.cmpp.message.CMPPSubmitRepMessage ;
import com.huawei.smproxy.util.Args ;
import java.io.UnsupportedEncodingException;

import org.apache.log4j.Logger;

/**
 * <p>Web发送短消息管理操作类，具体负责将页面提交的短消息发送到infoX</p>
 */

public class WebSMSender extends CMPPSMProxy
{
	private static Logger logger = Logger.getLogger(WebSMSender.class);
	
	//系统配置信息
	private static Args arg = Env.getConfig ().getArgs ( "CMPPConnect" ) ;
	
	private static WebSMSender instance ;

	public static WebSMSender getInstance ()
	{
		if ( instance == null )
		{
			instance = new WebSMSender () ;
		}
		return instance ;
	}

	protected WebSMSender ()
	{
		super ( WebSMSender.arg ) ;
	}

	/**
	 * 当与InfoX的连接被中断时的处理
	 */
	public void OnTerminate ()
	{
		logger.error( "Connection have been breaked! " ) ;
	}

	/**
	 * 对ISMG主动下发的消息的处理。函数中只对状态报告进行处理，所有其它消息都不进行处理，且回复错误消息。
	 * @param msg 收到的消息。
	 * @return 返回的相应消息。
	 */
	public CMPPMessage onDeliver ( final CMPPDeliverMessage msg )
	{
		byte[] msgId = msg.getMsgId () ;
		//状态报告
		if ( msg.getRegisteredDeliver () == 1 )
		{
			if ( ( String.valueOf ( msg.getStat () ).equalsIgnoreCase (
				"DELIVRD" ) ) )
			{
				System.out.println ( "\t\treceived DELIVRD message msgid=[" + msg.getMsgId () + "]" ) ;				
				try
				{
					return new CMPPDeliverRepMessage ( msgId , 0 ) ;
				}
				catch ( Exception ex )
				{
					return new CMPPDeliverRepMessage ( msgId , 9 ) ;
				}
			}
		}
		//不是状态报告
		else
		{
			//System.out.println ( "\t\treceived non DELIVRD message msgid=[" + msg.getMsgId () + "]" ) ;
			//注释：可以另外写一个类专门对上行消息进行处理，类里面包含一个handle方法。
			handle(msg);

		}
		return new CMPPDeliverRepMessage ( msgId , 0 ) ;
	}

	/**
	 * 发送一条消息，完成真正的消息发送。
	 * @param msg 待发送的消息。
	 * @return true：发送成功。false：发送失败。
	 */
	public boolean send ( CMPPSubmitMessage msg )//注释：可以另一个发送类，并实现多线程发送
	{
		if ( msg == null )
		{
			return false ;
		}		
		@SuppressWarnings("unused")
		CMPPSubmitRepMessage reportMsg = null ;
		try
		{
			reportMsg = ( CMPPSubmitRepMessage )super.send ( msg ) ;
		}
		catch ( IOException ex )
		{
			ex.printStackTrace () ;
			return false ;
		}
		return true ;
	}

	public void handle(CMPPDeliverMessage msg)
	{
		int msgFmt = 0;           //上行消息的编码方式
		String msgContent = "";   //上行消息的内容
		String srcAddr = "";      //消息主叫地址
		String destAddr = "";     //消息被叫地址

		msgFmt = msg.getMsgFmt();
		srcAddr = msg.getSrcterminalId();
		destAddr = msg.getDestnationId().substring(4);	//假设sp号码长度是4


	    if(msgFmt != 8)
		{
			//注释：写一个消息类，继承submit消息，逻辑处理完成后，构建一个消息，调用发送方法，提交消息到网关
			CMPPSubmitMessage msgmt = new CMPPSubmitMessage (
				1 ,
				1 ,
				1 , //需要状态报告
				1 ,
				"websms" ,
				1 ,
				"13012345678" ,
				0 ,
				0 ,
				0 ,
				"websms" ,
				"02" ,
				"10" ,
				new java.util.Date ( System.currentTimeMillis ()
									 + 2 * 24 * 60 * 60 * 1000 ) ,
				null ,
				destAddr ,
				new String[]{srcAddr} ,
				"消息编码方式错误".getBytes () ,
				"" );
			send(msgmt);
		}
		else
		{
			try{
				msgContent = new String ( msg.getMsgContent () , "utf16-be" ) ;
				CMPPSubmitMessage msgmt = new CMPPSubmitMessage (
				1 ,
				1 ,
				1 , //需要状态报告
				1 ,
				"websms" ,
				1 ,
				"13012345678" ,
				0 ,
				0 ,
				0 ,
				"websms" ,
				"02" ,
				"10" ,
				new java.util.Date ( System.currentTimeMillis ()
									 + 2 * 24 * 60 * 60 * 1000 ) ,
				null ,
				destAddr ,
				new String[]{srcAddr} ,
				msgContent.getBytes () ,
				"" );
			send(msgmt);
			}catch (UnsupportedEncodingException e)
			{}
		}
	}
}
