package com.hp.idc.sms.cmpp;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.hp.idc.cas.auc.PersonInfo;
import com.hp.idc.cas.auc.PersonManager;
import com.hp.idc.context.util.ContextUtil;
import com.hp.idc.sms.dao.SMSDao;
import com.hp.idc.sms.entity.SMSMessageEntity;

public class SendMessageThread extends Thread {

	/** ���̴߳���־��kill()�������ñ�־��Ϊfalse�� */
	private boolean alive = true;

	/** ���������������������������߳����С� */
	public static final ThreadGroup tg = new ThreadGroup("Req-thread");

	/** �Ķ��� */
	private IDCCMPPProxy20 IDCCMPPProxy = null;
	
	/** �߳�ִ�е���������г���,��λ�Ǻ��� */
	private long timeLong = 0;
	private int sleepInterval = 0;
	private boolean IsSleep = false;
	//ʧ�ܺ��ظ����Է��ʹ���
	private int replNum  = 0;
	
	// ÿ����ʱ�䷢һ��
	private int missionInterval = 0;
	
	private SMSDao SMSDao;

	/**
	 * ���캯�����ṩһ���߳������������췽��ֻ�����̣߳�����������
	 * 
	 * 
	 * @param name �ֳ�������
	 * @param timelong һ�����ӵ�ʱ��check
	 * @param sleepinterval ����
	 * @param replnum һ�η���ʧ�ܺ� �ظ����Է��͵Ĵ���
	 * @param missionInterval ���ŷ���
	 */
	public SendMessageThread(String name, int timelong, int sleepinterval,
			int replnum, int missionInterval) {
		super(tg, name);
		timeLong= timelong;
		if (sleepInterval > 0) {
			sleepInterval = sleepinterval;
			this.IsSleep = true;
		}
		replNum = replnum;
		this.SMSDao = (SMSDao) ContextUtil
				.getBean("SMSDao");
		this.missionInterval= missionInterval;
	}

	public final void run() {
		
		while (alive) {
			sendMessageFormDB();
			try {
				sleep(missionInterval);
			} catch (InterruptedException e) {
				System.out.println("�����߳�ʧ�ܡ�");
				break;
			}
		}
	}
	
	private static boolean isMobileNO(String mobiles){
		Pattern p = Pattern.compile("^0{0,1}(13[0-9]?|15[0-9])[0-9]{8}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
		}
	
	//�������ݴ�DB��ȡ�ò�����
	private void sendMessageFormDB() {
		List<SMSMessageEntity> smsMsg = SMSDao.querySMS4Send();
		for (Iterator<SMSMessageEntity> iterator = smsMsg.iterator(); iterator
				.hasNext();) {
			SMSMessageEntity SMSMessageEntity = iterator.next();
			String id = SMSMessageEntity.getMsgTo();
			if (isMobileNO(id)) {
				PersonInfo personInfo = PersonManager.getPersonById(id);
				if (personInfo != null) {
					id = personInfo.getMobile();
				}
				if (isMobileNO(id)) {
					sendMessage(SMSMessageEntity, id);
				}else{
					System.out.println("���벻�������޷����͡�");
				}
			}
			if (IsSleep) {
				try {
					sleep(sleepInterval);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	//���Ͷ���
	private SMSMessageEntity sendMessage(SMSMessageEntity SMSMessageEntity,String destID){
		//��ʼ�����Ŵ���
		IDCCMPPProxy = IDCCMPPProxy20.getInstance();
		// ʧ���ظ����ʹ�������
		int num = 0;
		while (true) {
			// �����߳̿�ʼִ�е�ʱ��
			long beginTime = new Date().getTime();
			try {
				num++;
				SMSMessageEntity = IDCCMPPProxy.Task(SMSMessageEntity,destID);
				SMSDao.save(SMSMessageEntity);
				// �ж��Ƿ񳬳��߳�����ִ�е�ʱ�䳤��
				if (new Date().getTime() - beginTime >= timeLong) {
					break;
				}
				// ���ͳɹ��˳�����һ��
				if(SMSMessageEntity.getMsgSms() == 3){
					break;
				}
			} catch (Exception ex) {
				if (num > replNum) {
					SMSMessageEntity.setMsgRemark(ex.getMessage());
					SMSMessageEntity.setMsgSms(4);
					SMSDao.save(SMSMessageEntity);
					IDCCMPPProxy.CloseProxy();
					break;
				}
			} catch (Throwable t) {
				if (num > replNum) {
					SMSMessageEntity.setMsgRemark(t.getMessage());
					SMSMessageEntity.setMsgSms(4);
					SMSDao.save(SMSMessageEntity);
					IDCCMPPProxy.CloseProxy();
					break;
				}
			}
		}
		return SMSMessageEntity;
	}
	
	  /**ɱ�����߳�*/
	  public void Kill()
	  {
	    alive = false;
	  }
}
