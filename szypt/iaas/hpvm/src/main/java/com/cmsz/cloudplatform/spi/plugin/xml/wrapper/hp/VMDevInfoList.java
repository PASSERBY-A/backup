package com.cmsz.cloudplatform.spi.plugin.xml.wrapper.hp;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.cmsz.cloudplatform.dto.VMDevInfo;

@XmlRootElement(name="hpvm")
@XmlAccessorType(XmlAccessType.NONE)
public class VMDevInfoList {
	@XmlElement(name="vmdevinfo",type=VMDevInfo.class)
	private List<VMDevInfo> vmDevInfoList;

	public List<VMDevInfo> getVmDevInfoList() {
		return vmDevInfoList;
	}

	public void setVmDevInfoList(List<VMDevInfo> vmDevInfoList) {
		this.vmDevInfoList = vmDevInfoList;
	}
	
	

}
