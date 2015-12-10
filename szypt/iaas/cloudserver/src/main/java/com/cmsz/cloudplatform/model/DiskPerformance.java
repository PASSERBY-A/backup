package com.cmsz.cloudplatform.model;


public class DiskPerformance {
	long time;
	
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	String instanceId;
	String diskUsageRatio;
	
	public String getDiskioread() {
		return diskioread;
	}
	public void setDiskioread(String diskioread) {
		this.diskioread = diskioread;
	}
	public String getDiskiowrite() {
		return diskiowrite;
	}
	public void setDiskiowrite(String diskiowrite) {
		this.diskiowrite = diskiowrite;
	}
	public String getDiskkbsread() {
		return diskkbsread;
	}
	public void setDiskkbsread(String diskkbsread) {
		this.diskkbsread = diskkbsread;
	}
	public String getDiskkbswrite() {
		return diskkbswrite;
	}
	public void setDiskkbswrite(String diskkbswrite) {
		this.diskkbswrite = diskkbswrite;
	}
	String diskioread;// the read (io) of disk on the vm 
	String diskiowrite;// the write (io) of disk on the vm 
	String diskkbsread;// the read (bytes) of disk on the vm 
	String diskkbswrite;// the write (bytes) of disk on the vm 

	public String getInstanceId() {
		return instanceId;
	}
	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}
	public String getDiskUsageRatio() {
		return diskUsageRatio;
	}
	public void setDiskUsageRatio(String diskUsageRatio) {
		this.diskUsageRatio = diskUsageRatio;
	}
	
}
