package com.cmsz.cloudplatform.web.action;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.cmsz.cloudplatform.dto.VM;
import com.cmsz.cloudplatform.dto.VMDevInfo;
import com.cmsz.cloudplatform.dto.VmSummary;
import com.cmsz.cloudplatform.spi.VirtualMachineService;
import com.cmsz.cloudplatform.spi.plugin.hpvm.HPPhysicalResourceService;
import com.cmsz.cloudplatform.web.core.BaseAction;
import com.hp.util.Page;

public class HPVirtualManageAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 498632804595112282L;
	@Autowired
	private VirtualMachineService hpVirtualMachineService;
	
	@Autowired
	private HPPhysicalResourceService hpPhysicalResourceService;

	public String list() {
		List<VmSummary> vmSummarys = null;
		String hostName = requestParams.get("hostname")!=null? requestParams.get("hostname")[0].toString():"";
		if(StringUtils.isNotBlank(hostName)){
			vmSummarys = hpVirtualMachineService.getVMList(hostName);
		}else{
			vmSummarys = hpVirtualMachineService.getVMList();
		}
		List<VmSummary> result = null;
		String name = requestParams.get("name") != null ? requestParams
				.get("name")[0].toString() : "";
		if (StringUtils.isNotBlank(name)) {
			result = new ArrayList<VmSummary>();
			for (VmSummary s : vmSummarys) {
				if (s.getName().toLowerCase().contains(name.toLowerCase())) {
					result.add(s);
				}
			}
		} else {
			result = vmSummarys;
		}
		Page page = new Page();
		int pageNo = requestParams.get("page") != null ? Integer
				.valueOf(requestParams.get("page")[0].toString()) : 1;
		int pagesize = requestParams.get("pagesize") != null ? Integer
				.valueOf(requestParams.get("pagesize")[0].toString()) : 20;
		page.setPageSize(pagesize);
		page.setPageNo(pageNo);
		page.setTotalCount(result.size());
		JSONObject jsonPage = this.getJsonPage(page);
		jsonPage.put("vmlist", result);
		this.renderText(jsonPage.toString());
		return NONE;
	}

	public String getByVMName() {
		String hostname = requestParams.get("hostname") != null ? requestParams
				.get("hostname")[0].toString() : "";
		String vmname = requestParams.get("vmname") != null ? requestParams
				.get("vmname")[0].toString() : "";
		VM vm = hpVirtualMachineService.getVMInfo(hostname, vmname);

		JSONObject jsonVM = new JSONObject();

		jsonVM.put("vm", vm);
		this.renderText(jsonVM.toString());
		return NONE;
	}

	public String listDevinfo() {
		String hostname = request.getParameter("hostname");
		List<VMDevInfo> vmdevInfos = hpPhysicalResourceService.getVMDevInfo(hostname);
		
		
		
		Page page = new Page();
		page.setTotalCount(1);
		page.setPageNo(2);
		page.setPageSize(20);
		JSONObject jsonPage = this.getJsonPage(page);
		jsonPage.put("devinfos", vmdevInfos);
		this.renderText(jsonPage.toString());
		return NONE;
	}
}
