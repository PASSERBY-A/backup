package com.cmsz.cloudplatform.spi.plugin.xml.wrapper.hp;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="Port")
@XmlAccessorType(XmlAccessType.NONE)
public class HBAPort {
	@XmlAttribute(name="Num")
	private int num;
	
	@XmlAttribute(name="WWN")
	private String wwn;

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public String getWwn() {
		return wwn;
	}

	public void setWwn(String wwn) {
		this.wwn = wwn;
	}

	@Override
	public String toString() {
		return String.format("HBAPort [num=%s, wwn=%s]", num, wwn);
	}
	
	
	
}
