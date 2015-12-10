package com.hp.idc.common;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;

/**
 * quartz的定时器
 * 通过spring配置加载
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * @version 1.0, 9:15:56 AM Jul 12, 2011
 *
 */
public class SchedulerManager {
	private Scheduler scheduler;

	private Trigger trigger;

	private JobDetail jobDetail;

	public JobDetail getJobDetail() {
		return this.jobDetail;
	}

	public void setJobDetail(JobDetail jobDetail) {
		this.jobDetail = jobDetail;
	}

	public void init() {
		try {
			this.scheduler.scheduleJob(this.jobDetail, this.trigger);
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}

	public Scheduler getScheduler() {
		return this.scheduler;
	}

	public void setScheduler(Scheduler scheduler) {
		this.scheduler = scheduler;
	}

	public Trigger getTrigger() {
		return this.trigger;
	}

	public void setTrigger(Trigger trigger) {
		this.trigger = trigger;
	}
}
