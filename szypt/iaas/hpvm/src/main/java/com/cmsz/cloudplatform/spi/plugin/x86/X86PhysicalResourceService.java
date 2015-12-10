package com.cmsz.cloudplatform.spi.plugin.x86;

import java.util.List;

import org.apache.cxf.jaxrs.client.WebClient;

import com.cmsz.cloudplatform.dto.Host;
import com.cmsz.cloudplatform.dto.x86.Rack;

public interface X86PhysicalResourceService {
	
	/**
	 * 查询机架式服务器
	 * @param ip 机架式服务器IP地址
	 * @return
	 */
	public Host getRackmount(String ip);
	
	
	/**
	 * 刀箱信息包括刀箱基本信息，卡槽信息及状态
	 * @param ip
	 * @return
	 */
	public Rack getEnclosureInfo(String ip);
	
	
	
	/** 
	 * 查询指定IP的刀箱对应的所有的刀片机
	 * @param ip 刀箱对应的IP地址
	 * @return
	 */
	List<Host> getAllHost(String ip);
	
	
	
	public WebClient getX86WebServiceClient();
	
	
}
