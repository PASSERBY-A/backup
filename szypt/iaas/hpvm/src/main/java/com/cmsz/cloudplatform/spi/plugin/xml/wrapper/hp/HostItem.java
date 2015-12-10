package com.cmsz.cloudplatform.spi.plugin.xml.wrapper.hp;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "host")

public class HostItem {
	
	private String name;
	
	private String desc;
	
	@XmlAttribute(name = "name")
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}
	@XmlAttribute(name = "desc")
	public void setDesc(String desc) {
		this.desc = desc;
	}

}
