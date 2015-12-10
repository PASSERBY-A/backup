package com.cmsz.cloudplatform.spi.plugin.xml.wrapper;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="Memory")
//@XmlAccessorType(XmlAccessType.NONE)
public class Memory {
	
	private int totalMB;

	@XmlAttribute(name="TotalMB")
	public int getTotalMB() {
		return totalMB;
	}

	public void setTotalMB(int totalMB) {
		this.totalMB = totalMB;
	}
	
	
}
