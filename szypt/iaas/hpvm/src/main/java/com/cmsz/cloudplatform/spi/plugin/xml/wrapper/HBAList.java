package com.cmsz.cloudplatform.spi.plugin.xml.wrapper;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="HBA")
@XmlAccessorType(XmlAccessType.NONE)
public class HBAList {
	
	@XmlAttribute(name="count")
	private int count;
	
	@XmlAttribute(name="Type")
	private String type;
	
	@XmlElement(name="Port",type=HBAPort.class)
	private List<HBAPort> ports;

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<HBAPort> getPorts() {
		return ports;
	}

	public void setPorts(List<HBAPort> ports) {
		this.ports = ports;
	}

	@Override
	public String toString() {
		return String.format("HBAList [count=%s, type=%s, ports=%s]", count,
				type, ports);
	}
	
	
}