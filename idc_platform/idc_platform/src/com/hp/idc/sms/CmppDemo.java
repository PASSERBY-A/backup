package com.hp.idc.sms;

import com.huawei.smproxy.comm.cmpp.message.CMPPSubmitMessage;

/**
 * CMPPЭ����Դ���
 */

public class CmppDemo
{

    public CmppDemo ()
    {}

    public static void main ( String[] args )
    {

	//����100�����ն��ŵ��ֻ�����
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
        //����һ��CMPP��Ϣ
        System.out.print ( "new CMPPMessage..." ) ;
        CMPPSubmitMessage msg = new CMPPSubmitMessage (
                1 , //��ͬMsg_id��Ϣ��������
                1 , //��ͬMsg_id����Ϣ���
                1 , //��Ҫ״̬����
                1 , //��Ϣ���
                "websms" , //ҵ�����ͣ�Ӧ��Infox��Ӧ
                1 , //�Ʒ��û����ͣ�0:��Ŀ���ն�MSISDN�Ʒѣ�1:��Դ�ն�MSISDN�Ʒѣ�2:��SP�Ʒѣ�3:���ֶ���Ч
                "13813245678" , //���Ʒ��û��ĺ���(���Ƕ����мƷ�,���Դ˶���д�˿�)
                0 , //GSMЭ������
                0 , //GSMЭ������
                0 , //��Ϣ��CMPP��ʽ���ͣ�  0: ASCII��ʽ, 1: UCS2��ʽ
                "websms" , //��Ϣ������Դ����Ӧ�ڵ�¼Infox���ʺ�
                "02" , //�ʷ���𣬡�����Ϣ���͡�Ϊ�����͡����ԡ��Ʒ��û����롱������Ϣ�ѣ����໰�������ں˼�SP�ԳƵ��ŵ���
                "10" , //�ʷѴ��룬�Է�Ϊ��λ
                new java.util.Date ( System.currentTimeMillis ()
                                     + 2 * 24 * 60 * 60 * 1000 ) , //�����Ч��,��ǰʱ��+2��
                null , //��ʱ����ʱ��(null:��������)
                "8888" , //Դ�ն�MSISDN����(ΪSP�ķ�������ǰ׺, Ϊ�������ĳ�����,
                //���ؽ��ú����������SMPPЭ����Ӧ��destination_address�ֶΣ�
                //�ú����������û��ֻ�����ʾΪ����Ϣ�����к���) (û�п���Ϊ��)
                rcvMobile , //����ҵ���MSISDN����(����)
                "�ֶ������ֶ����ؽ��ú����������SMPPЭ����Ӧ��destination_address�ֶ��ֶ����ؽ��ú����������SMPPЭ����Ӧ��destination_address�ֶ�".getBytes () ,
                "" ) ;
        System.out.println ( "OK" ) ;
        int sendcount = 100 ;
        int sendinterval = 20 ;
        //��һ�������Ƿ��ʹ���
        try
        {
            sendcount = Integer.parseInt ( args[ 0 ] ) ;
        }
        catch ( Exception ex )
        {
            sendcount = 1 ;
        }
        //�ڶ������������η��ͼ�ȴ���ʱ��(��)
        try
        {
            sendinterval = Integer.parseInt ( args[ 1 ] ) ;
        }
        catch ( Exception ex )
        {
            sendinterval = 1 ;
        }
        //������Ϣ(���Ͷ��)
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
            //���η��ͼ�ȴ�һ��ʱ��,�Ա���infox�ܹ����и��ٵ���
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
