package com.cmsz.cloudplatform.spi.plugin.hpvm;

import java.util.List;

import com.cmsz.cloudplatform.dto.Host;
import com.cmsz.cloudplatform.dto.VMDevInfo;

public interface HPPhysicalResourceService {
	
	
	/**
	 * 获取HP主机列表信息
	 * @return
	 */
	List<Host> getAllHost();
	
	/**
	 * 获取HP主机的信息
	 * @param hostName
	 * @return
	 */
	Host getHost(String hostName);
	
	/**
	 * 获取HP主机上运行的虚拟机的设备信息（显示HOST上分配的lv和vm上的disk信息）
	 * @param hostname
	 * @return
	 */
	List<VMDevInfo> getVMDevInfo(String hostname);
}
