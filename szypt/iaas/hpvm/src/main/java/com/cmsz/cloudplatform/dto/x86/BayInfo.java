package com.cmsz.cloudplatform.dto.x86;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * 卡槽信息
 * @author limanx
 *
 */
@XmlRootElement(name="Server")
//@XmlType(name="Server")
@XmlAccessorType(XmlAccessType.NONE)
public class BayInfo {
	/*
	 * <info>
<Servers Total="13" PowerOn="12">
<Server Bay="1" iLOName="ILOCNG238TWB5" iLOIP="192.168.21.89" Status="OK" Power="On"/>
<Server Bay="2" iLOName="ILOCNG238TWBR" iLOIP="192.168.21.90" Status="OK" Power="On"/>
<Server Bay="3" iLOName="ILOCNG239S2L3" iLOIP="192.168.21.91" Status="OK" Power="On"/>
<Server Bay="4" iLOName="ILOCNG238TRHZ" iLOIP="192.168.21.92" Status="OK" Power="On"/>
<Server Bay="6" iLOName="ILOCNG238TWBW" iLOIP="192.168.21.96" Status="OK" Power="Off"/>
<Server Bay="7" iLOName="ILOCNG152T8BR" iLOIP="192.168.21.97" Status="OK" Power="On"/>
<Server Bay="8" iLOName="ILOCNG152T899" iLOIP="192.168.21.98" Status="OK" Power="On"/>
<Server Bay="9" iLOName="ILOCNG238TRJ3" iLOIP="192.168.21.93" Status="OK" Power="On"/>
<Server Bay="10" iLOName="ILOCNG239S2KJ" iLOIP="192.168.21.94" Status="OK" Power="On"/>
<Server Bay="11" iLOName="ILOCNG239S2K6" iLOIP="192.168.21.95" Status="OK" Power="On"/>
<Server Bay="14" iLOName="ILOCNG152T8FD" iLOIP="192.168.21.99" Status="OK" Power="On"/>
<Server Bay="15" iLOName="ILOCNG152T8FB" iLOIP="192.168.21.100" Status="OK" Power="On"/>
<Server Bay="16" iLOName="ILO" iLOIP="192.168.21.101" Status="OK" Power="On"/>
</Servers>
</info>*/
	  
	@XmlAttribute(name="Bay")
	private int bayIndex;
	
	@XmlAttribute(name="iLOName")
	private String iLOName;
	
	@XmlAttribute(name="iLOIP")
	private String iLOIP;
	
	@XmlAttribute(name="SerialNum")
	private String serialNum;
	
	@XmlAttribute(name="ServerName")
	private String serverName;	
	private String hostStatus;
	
	@XmlAttribute(name="Status")
	private String status;
	
	@XmlAttribute(name="Power")
	private String powerFlag;
	public int getBayIndex() {
		return bayIndex;
	}
	public void setBayIndex(int bayIndex) {
		this.bayIndex = bayIndex;
	}
	public String getiLOName() {
		return iLOName;
	}
	public void setiLOName(String iLOName) {
		this.iLOName = iLOName;
	}
	public String getiLOIP() {
		return iLOIP;
	}
	public void setiLOIP(String iLOIP) {
		this.iLOIP = iLOIP;
	}
	public String getSerialNum() {
		return serialNum;
	}
	public void setSerialNum(String serialNum) {
		this.serialNum = serialNum;
	}
	public String getServerName() {
		return serverName;
	}
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}
	public String getHostStatus() {
		return hostStatus;
	}
	public void setHostStatus(String hostStatus) {
		this.hostStatus = hostStatus;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getPowerFlag() {
		return powerFlag;
	}
	public void setPowerFlag(String powerFlag) {
		this.powerFlag = powerFlag;
	}
	//@Override
	public String toString() {
		return String
				.format("BayInfo [bayIndex=%s, iLOName=%s, iLOIP=%s, serialNum=%s, serverName=%s, hostStatus=%s, status=%s, powerFlag=%s]",
						bayIndex, iLOName, iLOIP, serialNum, serverName,
						hostStatus, status, powerFlag);
	}
	

			
}
