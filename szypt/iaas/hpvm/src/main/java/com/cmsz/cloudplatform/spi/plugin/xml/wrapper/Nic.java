package com.cmsz.cloudplatform.spi.plugin.xml.wrapper;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Nic")
public class Nic {

	
	private int count;

	@XmlAttribute
	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

}
