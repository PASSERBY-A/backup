package com.cmsz.cloudplatform.model.vo;

public class ResourceStructureVO extends BaseVO implements Comparable<ResourceStructureVO> {

	private static final long serialVersionUID = -2421882270895414108L;

	// //虚机，主机，CLUSTER，POD,ZONE，一级资源池
	private String vmId;// 虚机 ID
	private String vmName;// 虚机名称
	private String host;
	private String cluster;
	private String pod;
	private String zone;
	private String resourcePool;

	private String hostId;
	private String clusterId;
	private String podId;
	private String zoneId;
	private String resourcePoolId;

	public String getVmId() {
		return vmId;
	}

	public void setVmId(String vmId) {
		this.vmId = vmId;
	}

	public String getVmName() {
		return vmName;
	}

	public void setVmName(String vmName) {
		this.vmName = vmName;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getCluster() {
		return cluster;
	}

	public void setCluster(String cluster) {
		this.cluster = cluster;
	}

	public String getPod() {
		return pod;
	}

	public void setPod(String pod) {
		this.pod = pod;
	}

	public String getZone() {
		return zone;
	}

	public void setZone(String zone) {
		this.zone = zone;
	}

	public String getResourcePool() {
		return resourcePool;
	}

	public void setResourcePool(String resourcePool) {
		this.resourcePool = resourcePool;
	}

	public String getHostId() {
		return hostId;
	}

	public void setHostId(String hostId) {
		this.hostId = hostId;
	}

	public String getClusterId() {
		return clusterId;
	}

	public void setClusterId(String clusterId) {
		this.clusterId = clusterId;
	}

	public String getPodId() {
		return podId;
	}

	public void setPodId(String podId) {
		this.podId = podId;
	}

	public String getZoneId() {
		return zoneId;
	}

	public void setZoneId(String zoneId) {
		this.zoneId = zoneId;
	}

	public String getResourcePoolId() {
		return resourcePoolId;
	}

	public void setResourcePoolId(String resourcePoolId) {
		this.resourcePoolId = resourcePoolId;
	}

	@Override
	public int compareTo(ResourceStructureVO o) {
		int rp = 0;
		if (o != null && o.resourcePool != null) {
			rp = o.resourcePool.compareTo(this.resourcePool);
			if (rp == 0 && o.vmId != null) {
				rp = o.vmId.compareTo(this.vmId);
			}
		}
		return rp;
	}

}
