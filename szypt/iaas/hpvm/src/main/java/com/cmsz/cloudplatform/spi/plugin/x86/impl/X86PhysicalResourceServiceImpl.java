package com.cmsz.cloudplatform.spi.plugin.x86.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.MediaType;

import org.apache.cxf.jaxrs.client.WebClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

import com.cmsz.cloudplatform.dto.HBA;
import com.cmsz.cloudplatform.dto.Host;
import com.cmsz.cloudplatform.dto.x86.BayInfo;
import com.cmsz.cloudplatform.dto.x86.BayInfoList;
import com.cmsz.cloudplatform.dto.x86.Rack;
import com.cmsz.cloudplatform.spi.plugin.x86.X86PhysicalResourceService;
import com.cmsz.cloudplatform.spi.plugin.xml.wrapper.HBAList;
import com.cmsz.cloudplatform.spi.plugin.xml.wrapper.HBAPort;
import com.cmsz.cloudplatform.spi.plugin.xml.wrapper.HostWraper;


@Service("x86PhysicalResourceService")
public class X86PhysicalResourceServiceImpl implements
		X86PhysicalResourceService {
	
	
	private static Logger logger = LoggerFactory.getLogger(X86PhysicalResourceServiceImpl.class);
	
	
	
	private String RACKMOUNT = "/x86/rackmount/"; 
	@Autowired
	private  WebClient x86WebServiceClient;
	@Override
	public Host getRackmount(String ip) {
		logger.info("--------------------------invoke RestFul Service X86机架服务器接口begin------------------------------------");
		String pathParam = RACKMOUNT+ip;
		HostWraper hostWraper = null;
		try{
		hostWraper = x86WebServiceClient.back(true).path(pathParam).accept(MediaType.APPLICATION_XML).
				type(MediaType.APPLICATION_XML).get(HostWraper.class);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		Host h = transform(hostWraper);
		logger.info("--------------------------invoke RestFul Service X86机架服务器接口end------------------------------------");
		return h;
	}

	private String ENCLOSURE_INFO ="/x86/enclosure/";
	private String SERVERINFO = "/x86/enclosure/serverinfo/";
	private String SERVERLIST = "/x86/enclosure/serverlist/";
	private String SERVERNAMES = "/x86/enclosure/servernames/";
	@Override
	public Rack getEnclosureInfo(String ip) {
		
		logger.info("--------------------------invoke RestFul Service X86刀箱接口begin------------------------------------");
		logger.info("---------------------刀箱接口----------------------");
		String pathParam = ENCLOSURE_INFO+ip;
		Rack r = null;
		try{
			logger.info("webservice url="+x86WebServiceClient.getBaseURI().getScheme()+"://"+x86WebServiceClient.getBaseURI().getHost()+":"+x86WebServiceClient.getBaseURI().getPort()+x86WebServiceClient.getBaseURI().getRawPath()+pathParam);
			r = x86WebServiceClient.back(true).path(pathParam).accept(MediaType.APPLICATION_XML).
			type(MediaType.APPLICATION_XML).get(Rack.class);
		}catch(Exception ex){
			logger.info("webservice url="+x86WebServiceClient.getBaseURI().getScheme()+"://"+x86WebServiceClient.getBaseURI().getHost()+":"+x86WebServiceClient.getBaseURI().getPort()+x86WebServiceClient.getBaseURI().getRawPath()+pathParam);
			ex.printStackTrace();
		}
		
		if(r!=null){
			r.setIp(ip);
		}else{
			return null;
		}
		pathParam = SERVERLIST+ip;
		logger.info("----------------------刀箱卡槽信息接口(包含iLOName、iLOIP)---------------------");
		
		
		BayInfoList iLOBayList = null;
		try{
			iLOBayList = x86WebServiceClient.back(true).path(pathParam).accept(MediaType.APPLICATION_XML).
				type(MediaType.APPLICATION_XML).get(BayInfoList.class);
		}catch(Exception ex){
			logger.info("webservice url="+x86WebServiceClient.getBaseURI().getScheme()+"://"+x86WebServiceClient.getBaseURI().getHost()+":"+x86WebServiceClient.getBaseURI().getPort()+x86WebServiceClient.getBaseURI().getRawPath()+ pathParam);
			ex.printStackTrace();
		}
		
		pathParam = SERVERNAMES+ip;
		logger.info("----------------------刀箱卡槽信息接口(包含ServerName、SerialNum)---------------------");
		BayInfoList serverNameList = null;
		try{
			serverNameList = x86WebServiceClient.back(true).path(pathParam).accept(MediaType.APPLICATION_XML).
					type(MediaType.APPLICATION_XML).get(BayInfoList.class);
		}catch(Exception ex){
			logger.info("webservice url="+x86WebServiceClient.getBaseURI().getScheme()+"://"+x86WebServiceClient.getBaseURI().getHost()+":"+x86WebServiceClient.getBaseURI().getPort()+x86WebServiceClient.getBaseURI().getRawPath()+ pathParam);
			ex.printStackTrace();
		}
		
		Map<String,BayInfo> serverNameMap = new HashMap<String,BayInfo>();
		if(serverNameList!=null){
			for(BayInfo bi : serverNameList.getServer().getBayList()){
				serverNameMap.put(String.valueOf(bi.getBayIndex()), bi);
			}
		}
		if(iLOBayList!=null && iLOBayList.getServer()!=null){
			for(BayInfo bi : iLOBayList.getServer().getBayList()){
				BayInfo serverNameBayInfo = serverNameMap.get(String.valueOf(bi.getBayIndex()));
				if(serverNameBayInfo!=null){
					bi.setSerialNum(serverNameBayInfo.getSerialNum());
					bi.setServerName(serverNameBayInfo.getServerName());
				}
			}
			r.setBayCount(iLOBayList.getServer().getTotal());
			r.setBayInfos(iLOBayList.getServer().getBayList());
		}
		
		logger.info("--------------------------invoke RestFul Service X86刀箱接口end------------------------------------");
		return r;
	}
	
	

	@Override
	public List<Host> getAllHost(String ip) {
		logger.info("--------------------------invoke RestFul Service X86刀片机列表 接口begin------------------------------------");
		List<Host> hosts = new ArrayList<Host>();
		String pathParam = SERVERLIST+ip;
		logger.info("----------------------刀箱卡槽信息接口(包含iLOName、iLOIP)---------------------");
		
		try{
			BayInfoList iLOBayList = x86WebServiceClient.back(true).path(pathParam).accept(MediaType.APPLICATION_XML).
					type(MediaType.APPLICATION_XML).get(BayInfoList.class);
			logger.info("webservice url="+x86WebServiceClient.getBaseURI().getScheme()+"://"+x86WebServiceClient.getBaseURI().getHost()+":"+x86WebServiceClient.getBaseURI().getPort()+x86WebServiceClient.getBaseURI().getRawPath()+ pathParam);
			if(iLOBayList!=null && iLOBayList.getServer()!=null && iLOBayList.getServer().getBayList()!=null){
				for(BayInfo b : iLOBayList.getServer().getBayList()){
					try{
						pathParam = SERVERINFO+"/"+ip+"/"+b.getBayIndex();
						HostWraper hostWrap  = x86WebServiceClient.back(true).path(pathParam).accept(MediaType.APPLICATION_XML).
								type(MediaType.APPLICATION_XML).get(HostWraper.class);
						//logger.info(hostWrap);
						hosts.add(transform(hostWrap));
						logger.info("webservice url="+x86WebServiceClient.getBaseURI().getScheme()+"://"+x86WebServiceClient.getBaseURI().getHost()+":"+x86WebServiceClient.getBaseURI().getPort()+x86WebServiceClient.getBaseURI().getRawPath()+ pathParam);
					}catch(Exception ex){
						ex.printStackTrace();
						logger.info("webservice url="+x86WebServiceClient.getBaseURI().getScheme()+"://"+x86WebServiceClient.getBaseURI().getHost()+":"+x86WebServiceClient.getBaseURI().getPort()+x86WebServiceClient.getBaseURI().getRawPath()+ pathParam);
					}
					
				}
			}
		}catch(Exception ex){
			logger.info("webservice url="+x86WebServiceClient.getBaseURI().getScheme()+"://"+x86WebServiceClient.getBaseURI().getHost()+":"+x86WebServiceClient.getBaseURI().getPort()+x86WebServiceClient.getBaseURI().getRawPath()+ pathParam);
			ex.printStackTrace();
		}
		
		logger.info("--------------------------invoke RestFul Service X86刀片机列表 接口begin------------------------------------");
		return hosts;
	}
	

	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext(
					new String[] { "webclient.xml" });
		X86PhysicalResourceService  x86PhysicalResourceService = (X86PhysicalResourceService) context.getBean("x86PhysicalResourceService");
		//x86PhysicalResourceService.getRackmount("");
		//logger.info(x86PhysicalResourceService.getEnclosureInfo("192.168.21.14"));
		System.out.println(x86PhysicalResourceService.getAllHost("192.168.21.14"));
		
		
	}

	public WebClient getX86WebServiceClient() {
		return x86WebServiceClient;
	}

	public void setX86WebServiceClient(WebClient x86WebServiceClient) {
		this.x86WebServiceClient = x86WebServiceClient;
	}	
	
	private Host transform(HostWraper hostWraper){
		if(hostWraper==null){
			return null;
		}
		Host host = new Host();
		host.setHostName(hostWraper.getBaseInfo().getServerName());
		host.setManufacturer(hostWraper.getBaseInfo().getManufacture());
		host.setProductName(hostWraper.getBaseInfo().getProductName());
		host.setSerialNumber(hostWraper.getBaseInfo().getSeriaNumber());
		host.setServerName(hostWraper.getBaseInfo().getServerName());
		
		host.setCpuCores(hostWraper.getCpu().getCores());
		host.setCpuCount(hostWraper.getCpu().getSockets());
		host.setCpuType(hostWraper.getCpu().getType());
		
		host.setMemory(hostWraper.getMemory().getTotalMB());
		host.setNic(hostWraper.getNic().getCount());
		
		List<HBA> hbas  = new ArrayList<HBA>();
		HBA hba = null;
		HBAList  habList = hostWraper.getHbaList();
		for(HBAPort port  : habList.getPorts()){
			hba = new HBA();
			hba.setType(habList.getType());
			hba.setWwn(port.getWwn());
			hba.setIndex(port.getNum());
			hbas.add(hba);
		}
		
		host.setHbas(hbas);
		
		return host;
	}

}
