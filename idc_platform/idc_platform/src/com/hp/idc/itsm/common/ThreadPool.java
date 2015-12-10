package com.hp.idc.itsm.common;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ThreadPool {
	
	
    private Log logger = LogFactory.getLog(getClass());


	private int corePoolSize = 1;// �����߳�
	private int maxPoolSize = 5;// ����߳�
	private int keepAliveTime = 10;// �̳߳�ʱʱ��
	private int blockQueueNum = 20;// �������

	private ThreadPoolExecutor executor;

	public ThreadPool() {
		executor = new ThreadPoolExecutor(corePoolSize, maxPoolSize,
				keepAliveTime, TimeUnit.SECONDS,
				new ArrayBlockingQueue<Runnable>(blockQueueNum),
				new ThreadPoolExecutor.CallerRunsPolicy());
	}
	
	public void addThread(Runnable task){
		try {
			executor.execute(task);
		} catch (Exception e) {
            logger.error("�����ܾ߳̾�����.",e);
        }
	}

	public int getCorePoolSize() {
		return corePoolSize;
	}

	public void setCorePoolSize(int corePoolSize) {
		this.corePoolSize = corePoolSize;
	}

	public int getMaxPoolSize() {
		return maxPoolSize;
	}

	public void setMaxPoolSize(int maxPoolSize) {
		this.maxPoolSize = maxPoolSize;
	}

	public int getKeepAliveTime() {
		return keepAliveTime;
	}

	public void setKeepAliveTime(int keepAliveTime) {
		this.keepAliveTime = keepAliveTime;
	}

	public int getBlockQueueNum() {
		return blockQueueNum;
	}

	public void setBlockQueueNum(int blockQueueNum) {
		this.blockQueueNum = blockQueueNum;
	}

	public ThreadPoolExecutor getExecutor() {
		return executor;
	}

	public void setExecutor(ThreadPoolExecutor executor) {
		this.executor = executor;
	}

}
