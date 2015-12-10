package com.cmsz.cloudplatform.spi;

import java.util.List;

import com.cmsz.cloudplatform.dto.VM;
import com.cmsz.cloudplatform.dto.VmSummary;

public interface VirtualMachineService {
	/**query VirtualMachine of host
	 * @param hostName 主机参数
	 * */
	public List<VmSummary> getVMList(String hostName);
	
	/**查询所有虚机*/
	public List<VmSummary> getVMList();
	
	/**查询虚机详情*/
	public VM getVMInfo(String hostName,String vmName);
}
