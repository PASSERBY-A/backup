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
 * <p>Web���Ͷ���Ϣ��������࣬���帺��ҳ���ύ�Ķ���Ϣ���͵�infoX</p>
 */

public class WebSMSender extends CMPPSMProxy
{
	private static Logger logger = Logger.getLogger(WebSMSender.class);
	
	//ϵͳ������Ϣ
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
	 * ����InfoX�����ӱ��ж�ʱ�Ĵ���
	 */
	public void OnTerminate ()
	{
		logger.error( "Connection have been breaked! " ) ;
	}

	/**
	 * ��ISMG�����·�����Ϣ�Ĵ���������ֻ��״̬������д�������������Ϣ�������д����һظ�������Ϣ��
	 * @param msg �յ�����Ϣ��
	 * @return ���ص���Ӧ��Ϣ��
	 */
	public CMPPMessage onDeliver ( final CMPPDeliverMessage msg )
	{
		byte[] msgId = msg.getMsgId () ;
		//״̬����
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
		//����״̬����
		else
		{
			//System.out.println ( "\t\treceived non DELIVRD message msgid=[" + msg.getMsgId () + "]" ) ;
			//ע�ͣ���������дһ����ר�Ŷ�������Ϣ���д������������һ��handle������
			handle(msg);

		}
		return new CMPPDeliverRepMessage ( msgId , 0 ) ;
	}

	/**
	 * ����һ����Ϣ�������������Ϣ���͡�
	 * @param msg �����͵���Ϣ��
	 * @return true�����ͳɹ���false������ʧ�ܡ�
	 */
	public boolean send ( CMPPSubmitMessage msg )//ע�ͣ�������һ�������࣬��ʵ�ֶ��̷߳���
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
		int msgFmt = 0;           //������Ϣ�ı��뷽ʽ
		String msgContent = "";   //������Ϣ������
		String srcAddr = "";      //��Ϣ���е�ַ
		String destAddr = "";     //��Ϣ���е�ַ

		msgFmt = msg.getMsgFmt();
		srcAddr = msg.getSrcterminalId();
		destAddr = msg.getDestnationId().substring(4);	//����sp���볤����4


	    if(msgFmt != 8)
		{
			//ע�ͣ�дһ����Ϣ�࣬�̳�submit��Ϣ���߼�������ɺ󣬹���һ����Ϣ�����÷��ͷ������ύ��Ϣ������
			CMPPSubmitMessage msgmt = new CMPPSubmitMessage (
				1 ,
				1 ,
				1 , //��Ҫ״̬����
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
				"��Ϣ���뷽ʽ����".getBytes () ,
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
				1 , //��Ҫ״̬����
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
