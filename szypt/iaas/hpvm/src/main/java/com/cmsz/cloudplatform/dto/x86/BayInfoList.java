package com.cmsz.cloudplatform.dto.x86;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.cmsz.cloudplatform.spi.plugin.xml.wrapper.x86.Servers;

@XmlRootElement(name = "info")
// @XmlType
@XmlAccessorType(XmlAccessType.NONE)
/*
 * <info> - <Servers Total="13" PowerOn="12"> <Server Bay="1"
 * iLOName="ILOCNG238TWB5" iLOIP="192.168.21.89" Status="OK" Power="On" />
 * <Server Bay="2" iLOName="ILOCNG238TWBR" iLOIP="192.168.21.90" Status="OK"
 * Power="On" /> <Server Bay="3" iLOName="ILOCNG239S2L3" iLOIP="192.168.21.91"
 * Status="OK" Power="On" /> <Server Bay="4" iLOName="ILOCNG238TRHZ"
 * iLOIP="192.168.21.92" Status="OK" Power="On" /> <Server Bay="6"
 * iLOName="ILOCNG238TWBW" iLOIP="192.168.21.96" Status="OK" Power="Off" />
 * <Server Bay="7" iLOName="ILOCNG152T8BR" iLOIP="192.168.21.97" Status="OK"
 * Power="On" /> <Server Bay="8" iLOName="ILOCNG152T899" iLOIP="192.168.21.98"
 * Status="OK" Power="On" /> <Server Bay="9" iLOName="ILOCNG238TRJ3"
 * iLOIP="192.168.21.93" Status="OK" Power="On" /> <Server Bay="10"
 * iLOName="ILOCNG239S2KJ" iLOIP="192.168.21.94" Status="OK" Power="On" />
 * <Server Bay="11" iLOName="ILOCNG239S2K6" iLOIP="192.168.21.95" Status="OK"
 * Power="On" /> <Server Bay="14" iLOName="ILOCNG152T8FD" iLOIP="192.168.21.99"
 * Status="OK" Power="On" /> <Server Bay="15" iLOName="ILOCNG152T8FB"
 * iLOIP="192.168.21.100" Status="OK" Power="On" /> <Server Bay="16"
 * iLOName="ILO" iLOIP="192.168.21.101" Status="OK" Power="On" /> </Servers>
 * </info>
 */
public class BayInfoList {

	// @XmlElement(name)
	// @XmlTransient
	// private int total;

	// @XmlTransient
	// private int powerOn;

	/*@XmlElementWrapper(name = "Servers")
	@XmlElement(name = "Server", type = BayInfo.class)
	private List<BayInfo> bayInfos;*/
	@XmlElement(name="Servers")
	private Servers server;/*

	
	 * public int getTotal() { return total; }
	 * 
	 * public void setTotal(int total) { this.total = total; }
	 * 
	 * public int getPowerOn() { return powerOn; }
	 * 
	 * public void setPowerOn(int powerOn) { this.powerOn = powerOn; }
	 

	public List<BayInfo> getBayInfos() {
		if (bayInfos != null) {
			return new ArrayList<BayInfo>();
		}
		return bayInfos;
	}

	public void setBayInfos(List<BayInfo> bayInfos) {
		this.bayInfos = bayInfos;
	}*/

	public Servers getServer() {
		return server;
	}

	public void setServer(Servers server) {
		this.server = server;
	}
	
	

}
