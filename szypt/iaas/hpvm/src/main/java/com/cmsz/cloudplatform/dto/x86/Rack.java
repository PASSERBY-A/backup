package com.cmsz.cloudplatform.dto.x86;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * 刀箱信息
 * @author limanx
 *
 */
@XmlRootElement(name="info")
@XmlAccessorType(XmlAccessType.NONE)
/**<info>
		<Enclosure-Name>OA-DD5-C7000-02</Enclosure-Name>
		<Enclosure-Type>BladeSystem-c7000-Enclosure-G2</Enclosure-Type>
		<Part-Number>507019-B21</Part-Number>
		<Serial-Number>CNG239S3VB</Serial-Number>
		<UUID>09CNG239S3VB</UUID>
		<Asset-Tag>OA-DD5-C7000-02</Asset-Tag>
		<Midplane-Spare-Part-Number>519345-001</Midplane-Spare-Part-Number>
		<Solutions-ID>0000000000000000</Solutions-ID>
	</info>
	*/
public class Rack {
	
	@XmlElement(name="Enclosure-Name")
	private String name;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@XmlElement(name="Serial-Number")
	private String serialNumber;
	
	@XmlElement(name="UUID")
	private String uuid;
	
	@XmlElement(name="Enclosure-Type")
	private String type;
	
	//@XmlTransient
	private int bayCount;
	
	@XmlTransient
	private String ip;
	@XmlTransient
	private List<BayInfo> bayInfos;
	public String getSerialNumber() {
		return serialNumber;
	}
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public int getBayCount() {
		return bayCount;
	}
	public void setBayCount(int bayCount) {
		this.bayCount = bayCount;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public List<BayInfo> getBayInfos() {
		return bayInfos;
	}
	public void setBayInfos(List<BayInfo> bayInfos) {
		this.bayInfos = bayInfos;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	@Override
	public String toString() {
		return String
				.format("Rack [name=%s, serialNumber=%s, uuid=%s, type=%s, bayCount=%s, ip=%s, bayInfos=%s]",
						name, serialNumber, uuid, type, bayCount, ip, bayInfos);
	}
	
	
	
		
	
	
}
