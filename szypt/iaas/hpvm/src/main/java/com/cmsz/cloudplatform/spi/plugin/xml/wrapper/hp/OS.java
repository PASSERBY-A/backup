package com.cmsz.cloudplatform.spi.plugin.xml.wrapper.hp;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="os")
public class OS {
	
	private String name;
	private String version;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	
	
	
}
