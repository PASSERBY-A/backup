package com.cmsz.cloudplatform.model;

public class MemoryPerformance {
	long time;
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	String instanceId;
	String memoryUsageRatio;
	public String getInstanceId() {
		return instanceId;
	}
	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}
	public String getMemoryUsageRatio() {
		return memoryUsageRatio;
	}
	public void setMemoryUsageRatio(String memoryUsageRatio) {
		this.memoryUsageRatio = memoryUsageRatio;
	}
	

}
