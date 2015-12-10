package com.cmsz.cloudplatform.spi.plugin.xml.wrapper;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name="Info")
@XmlAccessorType(XmlAccessType.FIELD)
public class HostWraper {
	
	@XmlElement(name="BaseInfo",type=HostBaseInfo.class)
	private HostBaseInfo baseInfo;
	//HostBaseInf
	
	@XmlElement(name="Memory")
	private Memory memory;
	
	@XmlElement(name="Cpu")
	private CPU cpu ;
	
	@XmlElement(name="Nic")
	private Nic nic;

	@XmlElement(name="HBA")
	private HBAList hbaList;

	public HostBaseInfo getBaseInfo() {
		return baseInfo;
	}

	public void setBaseInfo(HostBaseInfo baseInfo) {
		this.baseInfo = baseInfo;
	}

	public CPU getCpu() {
		return cpu;
	}

	public void setCpu(CPU cpu) {
		this.cpu = cpu;
	}

	@Override
	public String toString() {
		return String
				.format("HPHostWraper [baseInfo=%s, memory=%s, cpu=%s, nic=%s, hbaList=%s]",
						baseInfo, memory, cpu, nic, hbaList);
	}

	public Memory getMemory() {
		return memory;
	}

	public void setMemory(Memory memory) {
		this.memory = memory;
	}

	public Nic getNic() {
		return nic;
	}

	public void setNic(Nic nic) {
		this.nic = nic;
	}

	public HBAList getHbaList() {
		return hbaList;
	}

	public void setHbaList(HBAList hbaList) {
		this.hbaList = hbaList;
	}
	
}
