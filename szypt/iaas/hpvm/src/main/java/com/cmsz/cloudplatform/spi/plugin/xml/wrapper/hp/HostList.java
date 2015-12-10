package com.cmsz.cloudplatform.spi.plugin.xml.wrapper.hp;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="hpvm")
@XmlAccessorType(XmlAccessType.FIELD)
public class HostList {
	
	@XmlElementWrapper(name="hostlist")
	@XmlElement(name="host")
	private  List<HostItem> hostItems;

	public List<HostItem> getHostItems() {
		return hostItems;
	}

	public void setHostItems(List<HostItem> hostItems) {
		this.hostItems = hostItems;
	}

	
	

	
	
}
