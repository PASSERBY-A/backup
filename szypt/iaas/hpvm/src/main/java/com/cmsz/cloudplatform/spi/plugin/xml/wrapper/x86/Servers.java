package com.cmsz.cloudplatform.spi.plugin.xml.wrapper.x86;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.cmsz.cloudplatform.dto.x86.BayInfo;

@XmlRootElement(name="Servers")
@XmlAccessorType(XmlAccessType.NONE)
public class Servers {
	
	@XmlAttribute(name="Total")
	private int total ;
	
	@XmlAttribute(name="PowerOn")
	private int powerOn;
	
	@XmlElement(name="Server",type=BayInfo.class)
	private List<BayInfo> bayList;

	public List<BayInfo> getBayList() {
		if(bayList==null){
			return new ArrayList<BayInfo>();
		}
		return bayList;
	}

	public void setBayList(List<BayInfo> bayList) {
		this.bayList = bayList;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getPowerOn() {
		return powerOn;
	}

	public void setPowerOn(int powerOn) {
		this.powerOn = powerOn;
	}
	
}
