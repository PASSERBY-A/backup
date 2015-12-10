package com.hp.idc.sms;

import com.hp.idc.sms.cmpp.SendMessageThread;

/**
 * 短信发送插入接口
 * 
 * 
 * @author <a href="mailto:ruiw@hp.com">Wang Rui</a>
 * @version 1.0, 下午3:35:00 2011-9-22
 *
 */
public class IDCSMSSend {

	
	/** 线程执行的任务的运行长度,单位是毫秒 */
	private int timeLong = 0;
	private int sleepInterval = 0;
	//失败后重复尝试发送次数
	private int replNum  = 0;
	
	// 每多少时间发一次
	private int missionInterval = 0;
	
	//启动线程
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
