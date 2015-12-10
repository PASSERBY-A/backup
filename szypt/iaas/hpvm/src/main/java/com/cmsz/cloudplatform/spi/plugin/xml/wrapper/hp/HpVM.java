package com.cmsz.cloudplatform.spi.plugin.xml.wrapper.hp;

import java.util.List;

import com.cmsz.cloudplatform.dto.VmSummary;

public class HpVM {
	
	private List<VmSummary> virtualMachines;

	public List<VmSummary> getVirtualMachines() {
		return virtualMachines;
	}

	public void setVirtualMachines(List<VmSummary> virtualMachines) {
		this.virtualMachines = virtualMachines;
	}
	
}
