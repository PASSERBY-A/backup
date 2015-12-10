package com.hp.idc.boss.action;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.hp.idc.boss.BOSSCommonInterface;

/**
 * 
 * 文件同步JOB
 * 
 * @author <a href="mailto:ruiw@hp.com">Wang Rui</a>
 * @version 1.0, 上午10:09:08 2011-8-9
 *
 */
public class SyncFileJob extends QuartzJobBean {
	
	protected final Log log = LogFactory.getLog(getClass());
	
	private BOSSCommonInterface[] BOSSCommonInterfaces;
	
	@Override
	protected void executeInternal(JobExecutionContext context)
			throws JobExecutionException {
		for (BOSSCommonInterface BOSSCommonInterface : BOSSCommonInterfaces) {
			boolean execFlg = BOSSCommonInterface.defExecute();
			if(!execFlg){
				log.error(BOSSCommonInterface.getErrorMsg());
			}
		}
	}
	

	public BOSSCommonInterface[] getBOSSCommonInterfaces() {
		return BOSSCommonInterfaces;
	}

	public void setBOSSCommonInterfaces(BOSSCommonInterface[] bOSSCommonInterfaces) {
		BOSSCommonInterfaces = bOSSCommonInterfaces;
	}

	

}
