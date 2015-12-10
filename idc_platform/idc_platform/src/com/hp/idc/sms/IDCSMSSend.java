package com.hp.idc.sms;

import com.hp.idc.sms.cmpp.SendMessageThread;

/**
 * ���ŷ��Ͳ���ӿ�
 * 
 * 
 * @author <a href="mailto:ruiw@hp.com">Wang Rui</a>
 * @version 1.0, ����3:35:00 2011-9-22
 *
 */
public class IDCSMSSend {

	
	/** �߳�ִ�е���������г���,��λ�Ǻ��� */
	private int timeLong = 0;
	private int sleepInterval = 0;
	//ʧ�ܺ��ظ����Է��ʹ���
	private int replNum  = 0;
	
	// ÿ����ʱ�䷢һ��
	private int missionInterval = 0;
	
	//�����߳�
	public void init(){
		SendMessageThread thread = new SendMessageThread("CMPP", timeLong, sleepInterval, replNum, missionInterval);
		thread.start();
	}
	
	public int getTimeLong() {
		return timeLong;
	}

	public void setTimeLong(int timeLong) {
		this.timeLong = timeLong;
	}

	public int getSleepInterval() {
		return sleepInterval;
	}

	public void setSleepInterval(int sleepInterval) {
		this.sleepInterval = sleepInterval;
	}

	public int getReplNum() {
		return replNum;
	}

	public void setReplNum(int replNum) {
		this.replNum = replNum;
	}

	public int getMissionInterval() {
		return missionInterval;
	}

	public void setMissionInterval(int missionInterval) {
		this.missionInterval = missionInterval;
	}
}
