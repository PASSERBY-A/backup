package com.cmsz.cloudplatform.spi.plugin.xml.wrapper.hp;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Nic")
public class Nic {

	private int count;

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

}
