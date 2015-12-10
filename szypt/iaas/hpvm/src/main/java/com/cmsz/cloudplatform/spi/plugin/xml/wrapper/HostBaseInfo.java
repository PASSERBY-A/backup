package com.cmsz.cloudplatform.spi.plugin.xml.wrapper;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

//<BaseInfo Manufacture="HP" ProductName="ia64 hp Integrity rx2800 i2" SeriaNumber="SGH151YMW4" 
//ServerName="vm-srv01"/>
@XmlRootElement(name="BaseInfo")
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name="BaseInfo")
public class HostBaseInfo {
	
	@XmlAttribute(name="Manufacturer")
	private String manufacture;
	
	@XmlAttribute(name="ProductName")
	private String productName;

	@XmlAttribute(name="SerialNumber")
	private String seriaNumber;
	
	@XmlAttribute(name="ServerName")
	private String serverName;


	public String getManufacture() {
		return manufacture;
	}

	public void setManufacture(String manufacture) {
		this.manufacture = manufacture;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getSeriaNumber() {
		return seriaNumber;
	}

	public void setSeriaNumber(String seriaNumber) {
		this.seriaNumber = seriaNumber;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	@Override
	public String toString() {
		return String
				.format("HostBaseInfo [manufacture=%s, productName=%s, seriaNumber=%s, serverName=%s]",
						manufacture, productName, seriaNumber, serverName);
	}
	
	
	
	
}
