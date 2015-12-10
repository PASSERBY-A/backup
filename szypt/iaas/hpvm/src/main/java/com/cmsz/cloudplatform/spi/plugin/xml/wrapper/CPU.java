package com.cmsz.cloudplatform.spi.plugin.xml.wrapper;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="Cpu")
@XmlAccessorType(XmlAccessType.FIELD)
//<Cpu Sockets="" Cores="8" Type="2 Intel(R) Itanium(R) Processor 9340s (1.6 GHz, 20 MB)"/>
public class CPU {
	@XmlAttribute(name="Sockets")
	private int sockets;
	@XmlAttribute(name="Cores")
	private int cores;
	@XmlAttribute(name="Type")
	private String type;
	public int getSockets() {
		return sockets;
	}
	public void setSockets(int sockets) {
		this.sockets = sockets;
	}
	public int getCores() {
		return cores;
	}
	public void setCores(int cores) {
		this.cores = cores;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	@Override
	public String toString() {
		return String.format("CPU [sockets=%s, cores=%s, type=%s]", sockets,
				cores, type);
	}
	
	
	
	
}
