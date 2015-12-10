package com.cmsz.cloudplatform.spi.plugin.hpvm.impl;

import java.util.ArrayList;
import java.util.List;

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
import com.cmsz.cloudplatform.dto.VMDevInfo;
import com.cmsz.cloudplatform.spi.plugin.hpvm.HPPhysicalResourceService;
import com.cmsz.cloudplatform.spi.plugin.xml.wrapper.HBAList;
import com.cmsz.cloudplatform.spi.plugin.xml.wrapper.HBAPort;
import com.cmsz.cloudplatform.spi.plugin.xml.wrapper.HostWraper;
import com.cmsz.cloudplatform.spi.plugin.xml.wrapper.hp.HostItem;
import com.cmsz.cloudplatform.spi.plugin.xml.wrapper.hp.HostList;
import com.cmsz.cloudplatform.spi.plugin.xml.wrapper.hp.VMDevInfoList;
@Service("hpPhysicalResourceService")
public class HPPhysicalResourceServiceImpl implements HPPhysicalResourceService {

	
	
	private static Logger logger = LoggerFactory.getLogger(HPPhysicalResourceServiceImpl.class);
	
	
	
	private String HOSTLIST="/hpvm/gethostlist";
	private String GETHOSTINFO = "/hpvm/gethostinfo";
	
	private String GETVMDEVINFO = "/hpvm/getvmdevinfo";
	@Autowired
	private WebClient hpWebServiceClient;
	
	@Override
	public List<Host> getAllHost() {
		List<Host> result = new ArrayList<Host>();
		logger.info("--------------------------invoke RestFul Service HP主机列表接口begin------------------------------------");
		logger.info("---------------------HP主机列表接口----------------------");
		String pathParam = HOSTLIST;
		HostList obj = null;	
		try{
			obj = hpWebServiceClient.back(true).path(pathParam).accept(MediaType.APPLICATION_XML).
			type(MediaType.APPLICATION_XML).get(HostList.class);
			logger.info("webservice url="+hpWebServiceClient.getCurrentURI().getScheme()+"://"+hpWebServiceClient.getCurrentURI().getHost()+":"+hpWebServiceClient.getCurrentURI().getPort()+hpWebServiceClient.getCurrentURI().getRawPath()+pathParam);
		}catch(Exception ex){
			logger.info("webservice url="+hpWebServiceClient.getCurrentURI().getScheme()+"://"+hpWebServiceClient.getCurrentURI().getHost()+":"+hpWebServiceClient.getCurrentURI().getPort()+hpWebServiceClient.getCurrentURI().getRawPath()+pathParam);
			ex.printStackTrace();
		}
		if(obj!=null && obj.getHostItems()!=null && obj.getHostItems().size()>0){
			for(HostItem item : obj.getHostItems()){
				pathParam = GETHOSTINFO+"/"+item.getName();
				//logger.info("webservice url="+hpWebServiceClient.getCurrentURI().getScheme()+"://"+hpWebServiceClient.getCurrentURI().getHost()+":"+hpWebServiceClient.getCurrentURI().getPort()+hpWebServiceClient.getCurrentURI().getRawPath());
				try{
					HostWraper host = hpWebServiceClient.back(true).path(pathParam).accept(MediaType.APPLICATION_XML).
							type(MediaType.APPLICATION_XML).get(HostWraper.class);
					logger.info("webservice url="+hpWebServiceClient.getCurrentURI().getScheme()+"://"+hpWebServiceClient.getCurrentURI().getHost()+":"+hpWebServiceClient.getCurrentURI().getPort()+hpWebServiceClient.getCurrentURI().getRawPath()+pathParam);
					result.add(transform(host));
				}catch(Exception ex){
					logger.info("webservice url="+hpWebServiceClient.getCurrentURI().getScheme()+"://"+hpWebServiceClient.getCurrentURI().getHost()+":"+hpWebServiceClient.getCurrentURI().getPort()+hpWebServiceClient.getCurrentURI().getRawPath()+pathParam);
					ex.printStackTrace();
				}
				
			}
		
		}
		logger.info("--------------------------invoke RestFul Service HP主机列表接口end------------------------------------");
		return result;
	}

	@Override
	public Host getHost(String hostName) {
		logger.info("--------------------------invoke RestFul Service HP主机接口   begin------------------------------------");
		String pathParam = GETHOSTINFO+"/"+hostName;
		logger.info("webservice url="+hpWebServiceClient.getCurrentURI().getScheme()+"://"+hpWebServiceClient.getCurrentURI().getHost()+":"+hpWebServiceClient.getCurrentURI().getPort()+hpWebServiceClient.getCurrentURI().getRawPath()+pathParam);
		HostWraper host = hpWebServiceClient.back(true).path(pathParam).accept(MediaType.APPLICATION_XML).
		type(MediaType.APPLICATION_XML).get(HostWraper.class);
		logger.info("--------------------------invoke RestFul Service HP主机接口   end------------------------------------");
		return transform(host);
	}

	@Override
	public List<VMDevInfo> getVMDevInfo(String hostname) {
		logger.info("--------------------------invoke RestFul Service HP VMDevInfo接口   begin------------------------------------");
		String pathParam = GETVMDEVINFO + "/"+hostname;
		logger.info("webservice url="+hpWebServiceClient.getCurrentURI().getScheme()+"://"+hpWebServiceClient.getCurrentURI().getHost()+":"+hpWebServiceClient.getCurrentURI().getPort()+hpWebServiceClient.getCurrentURI().getRawPath()+pathParam);
		VMDevInfoList vminfoList = hpWebServiceClient.back(true).path(pathParam).accept(MediaType.APPLICATION_XML).
		type(MediaType.APPLICATION_XML).get(VMDevInfoList.class);
		logger.info("--------------------------invoke RestFul Service HP VMDevInfo接口   end------------------------------------");
		return vminfoList==null?new ArrayList<VMDevInfo>():vminfoList.getVmDevInfoList();
		
	}

	
	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext(
					new String[] { "webclient.xml" });
		HPPhysicalResourceService  hpPhysicalResourceService = (HPPhysicalResourceService) context.getBean("hpPhysicalResourceService");
		
		/*List<Host> hosts = hpPhysicalResourceService.getAllHost();
		for(Host h : hosts){
			logger.info(h);
			logger.info(hpPhysicalResourceService.getVMDevInfo(h.getHostName()));
			logger.info(hpPhysicalResourceService.getHost(h.getHostName()));
		}*/
		
		System.out.println(hpPhysicalResourceService.getHost("vm-srv01"));
		
		
	}	
	
	private Host transform(HostWraper hphostWraper){
		if(hphostWraper==null){
			return null;
		}
		Host host = new Host();
		host.setHostName(hphostWraper.getBaseInfo().getServerName());
		host.setManufacturer(hphostWraper.getBaseInfo().getManufacture());
		host.setProductName(hphostWraper.getBaseInfo().getProductName());
		host.setSerialNumber(hphostWraper.getBaseInfo().getSeriaNumber());
		host.setServerName(hphostWraper.getBaseInfo().getServerName());
		
		host.setCpuCores(hphostWraper.getCpu().getCores());
		host.setCpuCount(hphostWraper.getCpu().getSockets());
		host.setCpuType(hphostWraper.getCpu().getType());
		
		host.setMemory(hphostWraper.getMemory().getTotalMB());
		
		host.setNic(hphostWraper.getNic().getCount());
		
		List<HBA> hbas  = new ArrayList<HBA>();
		HBA hba = null;
		HBAList  habList = hphostWraper.getHbaList();
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
