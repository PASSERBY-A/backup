package com.hp.idc.sms;

import com.huawei.smproxy.comm.cmpp.message.CMPPSubmitMessage;

/**
 * CMPP协议测试代码
 */

public class CmppDemo
{

    public CmppDemo ()
    {}

    public static void main ( String[] args )
    {

	//生成100个接收短信的手机号码
        System.out.print ( "Create 100 receiver Mobile No..." ) ;
        String[] rcvMobile = new String[ 100 ] ;
        int count = 0 ;
        for ( int i = 0 ; i < 10 ; i++ )
        {
            for ( int j = 0 ; j < 10 ; j++ )
            {
                rcvMobile[ count ] = "136000000" + i + j ;
                count++ ;
            }
        }
        System.out.println ( "OK" ) ;
        //生成一个CMPP消息
        System.out.print ( "new CMPPMessage..." ) ;
        CMPPSubmitMessage msg = new CMPPSubmitMessage (
                1 , //相同Msg_id消息的总条数
                1 , //相同Msg_id的消息序号
                1 , //需要状态报告
                1 , //信息类别
                "websms" , //业务类型，应与Infox对应
                1 , //计费用户类型，0:对目的终端MSISDN计费，1:对源终端MSISDN计费，2:对SP计费，3:本字段无效
                "13813245678" , //被计费用户的号码(因是对主叫计费,所以此段填写了空)
                0 , //GSM协议类型
                0 , //GSM协议类型
                0 , //信息的CMPP格式类型：  0: ASCII格式, 1: UCS2格式
                "websms" , //信息内容来源，对应于登录Infox的帐号
                "02" , //资费类别，“短消息类型”为“发送”，对“计费用户号码”不计信息费，此类话单仅用于核减SP对称的信道费
                "10" , //资费代码，以分为单位
                new java.util.Date ( System.currentTimeMillis ()
                                     + 2 * 24 * 60 * 60 * 1000 ) , //存活有效期,当前时间+2天
                null , //定时发送时间(null:立即发送)
                "8888" , //源终端MSISDN号码(为SP的服务代码或前缀, 为服务代码的长号码,
                //网关将该号码完整的填到SMPP协议相应的destination_address字段，
                //该号码最终在用户手机上显示为短消息的主叫号码) (没有可以为空)
                rcvMobile , //接收业务的MSISDN号码(数组)
                "字段网关字段网关将该号码完整的填到SMPP协议相应的destination_address字段字段网关将该号码完整的填到SMPP协议相应的destination_address字段".getBytes () ,
                "" ) ;
        System.out.println ( "OK" ) ;
        int sendcount = 100 ;
        int sendinterval = 20 ;
        //第一个参数是发送次数
        try
        {
            sendcount = Integer.parseInt ( args[ 0 ] ) ;
        }
        catch ( Exception ex )
        {
            sendcount = 1 ;
        }
        //第二个参数是两次发送间等待的时长(秒)
        try
        {
            sendinterval = Integer.parseInt ( args[ 1 ] ) ;
        }
        catch ( Exception ex )
        {
            sendinterval = 1 ;
        }
        //发送消息(发送多次)
        for ( int i = 0 ; i < sendcount ; i++ )
        {
            System.out.print ( "Send Message..." ) ;
            if ( WebSMSender.getInstance ().send ( msg ))
            {
                System.out.println ( "Success" ) ;
            }
            else
            {
                System.out.println ( "Fail" ) ;
            }
            //两次发送间等待一段时间,以便于infox能够进行跟踪调试
            try
            {
                Thread.sleep ( 1000 * sendinterval ) ;
            }
            catch ( Exception ex )
            {}
        }
		while(true)
		{
			try
			{
				Thread.sleep ( 1000 ) ;
			}
			catch ( Exception ex )
			{}
		}
    }
}
