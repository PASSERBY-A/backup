package com.cmsz.cloudplatform.spi.plugin.hpvm.impl;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.client.WebClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

import com.cmsz.cloudplatform.dto.Device;
import com.cmsz.cloudplatform.dto.Memory;
import com.cmsz.cloudplatform.dto.Storage;
import com.cmsz.cloudplatform.dto.VCpu;
import com.cmsz.cloudplatform.dto.VM;
import com.cmsz.cloudplatform.dto.VmSummary;
import com.cmsz.cloudplatform.spi.VirtualMachineService;
import com.cmsz.cloudplatform.spi.plugin.xml.wrapper.hp.HostItem;
import com.cmsz.cloudplatform.spi.plugin.xml.wrapper.hp.HostList;
import com.cmsz.cloudplatform.spi.plugin.xml.wrapper.hp.HpVM;
import com.cmsz.cloudplatform.spi.plugin.xml.wrapper.hp.OS;
import com.thoughtworks.xstream.XStream;
@Service("hpVirtualMachineService")
public class HPVirtualMachineServiceImpl implements VirtualMachineService {

	@Autowired	private WebClient hpWebServiceClient; 
	
	private String VMSUMMARY = "/hpvm/getvmstatus/";
	
	private String HPHOSTLIST = "/hpvm/gethostlist";  
	
	private String VMINFO = "/hpvm/getvmstatus/";
	
	@Override
	public List<VmSummary> getVMList(String hostName) {
		
		System.out.println("--------------------------invoke RestFul Service HPVM 概要信息接口begin------------------------------------");
		List<VmSummary> result = new ArrayList<VmSummary>();
		String pathParam = VMSUMMARY+hostName;
		try{
			Response response = hpWebServiceClient.back(true).path(pathParam).accept(MediaType.APPLICATION_XML).
					type(MediaType.APPLICATION_XML).get();
			String resultStr = response.readEntity(String.class);
			HpVM hpVM = xmlToVmList(resultStr);
			System.out.println("webservice url="+hpWebServiceClient.getCurrentURI().getScheme()+"://"+hpWebServiceClient.getCurrentURI().getHost()+":"+hpWebServiceClient.getCurrentURI().getPort()+hpWebServiceClient.getCurrentURI().getRawPath());
			for(VmSummary v : hpVM.getVirtualMachines()){
				v.setHostName(hostName);
				result.add(v);
			}
		}catch(Exception ex){
			System.out.println("webservice url="+hpWebServiceClient.getCurrentURI().getScheme()+"://"+hpWebServiceClient.getCurrentURI().getHost()+":"+hpWebServiceClient.getCurrentURI().getPort()+hpWebServiceClient.getCurrentURI().getRawPath());
			ex.printStackTrace();
		}
		System.out.println("--------------------------invoke RestFul Service HPVM 概要信息接口end------------------------------------");
		return result;
	}

	@Override
	public List<VmSummary> getVMList() {
		List<VmSummary> result = new ArrayList<VmSummary>();
		System.out.println("--------------------------invoke RestFul Service HPVM 列表接口begin------------------------------------");
		System.out.println("---------------------HP主机列表接口----------------------");
		String pathParam = HPHOSTLIST;
		HostList obj = null;
		try{
			obj = hpWebServiceClient.back(true).path(pathParam).accept(MediaType.APPLICATION_XML).
					type(MediaType.APPLICATION_XML).get(HostList.class);
			System.out.println("webservice url="+hpWebServiceClient.getCurrentURI().getScheme()+"://"+hpWebServiceClient.getCurrentURI().getHost()+":"+hpWebServiceClient.getCurrentURI().getPort()+hpWebServiceClient.getCurrentURI().getRawPath());
		}catch(Exception ex){
			ex.printStackTrace();
		}
		System.out.println("---------------------HPVM 概要信息 接口----------------------");
		if(obj!=null && obj.getHostItems()!=null && obj.getHostItems().size()>0){
			for(HostItem item : obj.getHostItems()){
				pathParam = VMSUMMARY+item.getName();
				try{
					Response response = hpWebServiceClient.back(true).path(pathParam).accept(MediaType.APPLICATION_XML).
							type(MediaType.APPLICATION_XML).get();
					String resultStr = response.readEntity(String.class);
					HpVM hpVM = xmlToVmList(resultStr);
					System.out.println("webservice url="+hpWebServiceClient.getCurrentURI().getScheme()+"://"+hpWebServiceClient.getCurrentURI().getHost()+":"+hpWebServiceClient.getCurrentURI().getPort()+hpWebServiceClient.getCurrentURI().getRawPath());
					for(VmSummary v : hpVM.getVirtualMachines()){
						v.setHostName(item.getName());
						result.add(v);
					}
				}catch(Exception ex){
					System.out.println("webservice url="+hpWebServiceClient.getCurrentURI().getScheme()+"://"+hpWebServiceClient.getCurrentURI().getHost()+":"+hpWebServiceClient.getCurrentURI().getPort()+hpWebServiceClient.getCurrentURI().getRawPath());
					ex.printStackTrace();
				}
				
			}
		
		}
		System.out.println("--------------------------invoke RestFul Service HPVM 列表接口end------------------------------------");
		return result;
		
	}

	@Override
	public VM getVMInfo(String hostName,String vmName) {
		VM vm = null;
		System.out.println("--------------------------invoke RestFul Service HPVM 详细信息接口begin------------------------------------");
		
		String pathParam = VMINFO+hostName+"/"+vmName;
		try{
			String responseString = hpWebServiceClient.back(true).path(pathParam).accept(MediaType.APPLICATION_XML).
					type(MediaType.APPLICATION_XML).get().readEntity(String.class);
			System.out.println("webservice url="+hpWebServiceClient.getCurrentURI().getScheme()+"://"+hpWebServiceClient.getCurrentURI().getHost()+":"+hpWebServiceClient.getCurrentURI().getPort()+hpWebServiceClient.getCurrentURI().getRawPath());
			vm = xmlToVm(responseString);
		}catch(Exception ex){
			ex.printStackTrace();
			System.out.println("webservice url="+hpWebServiceClient.getCurrentURI().getScheme()+"://"+hpWebServiceClient.getCurrentURI().getHost()+":"+hpWebServiceClient.getCurrentURI().getPort()+hpWebServiceClient.getCurrentURI().getRawPath());
		}
	
		System.out.println("--------------------------invoke RestFul Service HPVM 详细信息接口end------------------------------------");
		return vm;
		
	}
	
	
	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext(
					new String[] { "webclient.xml" });
		VirtualMachineService  virtualMachineService = (VirtualMachineService) context.getBean("hpVirtualMachineService");
		System.out.println(virtualMachineService);
		/*List<Host> hosts = hpPhysicalResourceService.getAllHost();
		for(Host h : hosts){
			System.out.println(h);
			System.out.println(hpPhysicalResourceService.getVMDevInfo(h.getHostName()));
			System.out.println(hpPhysicalResourceService.getHost(h.getHostName()));
		}*/
		
		VM v = virtualMachineService.getVMInfo("vm-srv01", "vm-jk01");
		System.out.print(v.getMemory().getTotal());
		List<VmSummary> list = virtualMachineService.getVMList();
		for(VmSummary s : list){
			System.out.println(s.getMemory()+"sss");
		}
		
		
	}	
	
	
	private HpVM xmlToVmList(String vmXml) {
		if (vmXml!=null && !vmXml.trim().equals("")) {
			vmXml = vmXml.replace("<pman>", "").replace("</pman>", "");
			
			XStream xstream = new XStream();
			xstream.alias("hpvm", HpVM.class);
			xstream.alias("virtual_machine", VmSummary.class);
	    	xstream.useAttributeFor(VmSummary.class, "name");
	    	xstream.useAttributeFor(VmSummary.class, "uuid");
	    	xstream.useAttributeFor(VmSummary.class, "local_id");
	    	xstream.useAttributeFor(VmSummary.class, "guest_label");
			xstream.alias("memory", Memory.class);
			xstream.alias("OS", OS.class);
			xstream.addImplicitCollection(HpVM.class, "virtualMachines");
			try {
				return (HpVM) xstream.fromXML(vmXml);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	
	private VM xmlToVm(String vmXml) {
		if (vmXml!=null && !vmXml.trim().equals("")) {			
			vmXml = vmXml.replace("<hpvm>", "").replace("</hpvm>", "");
			vmXml = replaceTag(vmXml, "online_migration_attributes");
			vmXml = replaceTag(vmXml, "suspend_resume_attributes");
			vmXml = replaceTag(vmXml, "boot_time_information");
			vmXml = replaceTag(vmXml, "remote_console");
			vmXml = replaceTag(vmXml, "authorized_admin");
			vmXml = replaceTag(vmXml, "entitlement");
			vmXml = replaceTag(vmXml, "olad_cpu");
			vmXml = replaceTag(vmXml, "networks");
			vmXml = replaceTag(vmXml, "misc_interfaces");
			XStream xstream = new XStream();
			xstream.alias("virtual_machine", VM.class);
			xstream.useAttributeFor(VM.class, "name");
	    	xstream.useAttributeFor(VM.class, "uuid");
	    	xstream.useAttributeFor(VM.class, "local_id");
	    	xstream.useAttributeFor(VM.class, "guest_label");
			xstream.alias("vcpu", VCpu.class);
			xstream.alias("memory", Memory.class);
			xstream.alias("OS", OS.class);
			xstream.alias("storage", Storage.class);
			xstream.alias("device", Device.class);
			xstream.addImplicitCollection(Storage.class, "device");
			try {
				return (VM) xstream.fromXML(vmXml);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	private String replaceTag(String source, String tagName){
		if (source.indexOf("<"+tagName+">") > -1 && source.indexOf("</"+tagName+">") > -1) {			
			return source.replace(source.substring(source.indexOf("<"+tagName+">"), source.indexOf("</"+tagName+">")+("</"+tagName+">").length()), "");
		}
		return source;
	}

}
