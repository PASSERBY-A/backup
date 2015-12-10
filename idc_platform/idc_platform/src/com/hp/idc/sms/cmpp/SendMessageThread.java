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

	/** 该线程存活标志，kill()方法将该标志置为false。 */
	private boolean alive = true;

	/** 该类的所有子类对象均创建到这个线程组中。 */
	public static final ThreadGroup tg = new ThreadGroup("Req-thread");

	/** 的对象 */
	private IDCCMPPProxy20 IDCCMPPProxy = null;
	
	/** 线程执行的任务的运行长度,单位是毫秒 */
	private long timeLong = 0;
	private int sleepInterval = 0;
	private boolean IsSleep = false;
	//失败后重复尝试发送次数
	private int replNum  = 0;
	
	// 每多少时间发一次
	private int missionInterval = 0;
	
	private SMSDao SMSDao;

	/**
	 * 构造函数，提供一个线程名参数。构造方法只创建线程，并不启动。
	 * 
	 * 
	 * @param name 现场组名称
	 * @param timelong 一次连接的时长check
	 * @param sleepinterval 连接
	 * @param replnum 一次发送失败后 重复尝试发送的次数
	 * @param missionInterval 短信发送
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
				System.out.println("短信线程失败。");
				break;
			}
		}
	}
	
	private static boolean isMobileNO(String mobiles){
		Pattern p = Pattern.compile("^0{0,1}(13[0-9]?|15[0-9])[0-9]{8}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
		}
	
	//短信内容从DB中取得并发送
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
					System.out.println("号码不正常。无法发送。");
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
	
	//发送短信
	private SMSMessageEntity sendMessage(SMSMessageEntity SMSMessageEntity,String destID){
		//初始化短信代理
		IDCCMPPProxy = IDCCMPPProxy20.getInstance();
		// 失败重复发送次数计数
		int num = 0;
		while (true) {
			// 生成线程开始执行的时间
			long beginTime = new Date().getTime();
			try {
				num++;
				SMSMessageEntity = IDCCMPPProxy.Task(SMSMessageEntity,destID);
				SMSDao.save(SMSMessageEntity);
				// 判断是否超出线程任务执行的时间长度
				if (new Date().getTime() - beginTime >= timeLong) {
					break;
				}
				// 发送成功退出到下一个
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
	
	  /**杀死该线程*/
	  public void Kill()
	  {
	    alive = false;
	  }
}
