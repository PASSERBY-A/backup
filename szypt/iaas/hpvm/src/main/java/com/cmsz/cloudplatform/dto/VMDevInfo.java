package com.cmsz.cloudplatform.dto;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * HP主机上运行的虚拟机的设备信息（显示HOST上分配的lv和vm上的disk信息）
 * @author Li Manxin
 *
 */
/*<hpvm>
	<vmdevinfo vmname="vm-jk01" vmdevtype="disk" target="[0,0,1]" storetype="lv" hostdevname="/dev/vgvmdata01/rlvosjk01" vmdevname="/dev/rdisk/disk7"/>
	<vmdevinfo vmname="vm-jk02" vmdevtype="disk" target="[0,0,1]" storetype="lv" hostdevname="/dev/vgvmdata01/rlvosjk02" vmdevname="/dev/rdisk/disk7"/>
</hpvm>*/
@XmlRootElement(name="vmdevinfo")
public class VMDevInfo {
	
	private String vmname;
	
	private String vmdevtype;
	
	private String target = "";
	
	private String storetype;
	
	private String hostdevname;
	
	private String vmdevname;

	@XmlAttribute
	public String getVmname() {
		return vmname;
	}

	public void setVmname(String vmname) {
		this.vmname = vmname;
	}
	@XmlAttribute
	public String getVmdevtype() {
		return vmdevtype;
	}

	public void setVmdevtype(String vmdevtype) {
		this.vmdevtype = vmdevtype;
	}
	@XmlAttribute
	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}
	@XmlAttribute
	public String getStoretype() {
		return storetype;
	}

	public void setStoretype(String storetype) {
		this.storetype = storetype;
	}
	@XmlAttribute
	public String getHostdevname() {
		return hostdevname;
	}

	public void setHostdevname(String hostdevname) {
		this.hostdevname = hostdevname;
	}

	@XmlAttribute
	public String getVmdevname() {
		return vmdevname;
	}

	public void setVmdevname(String vmdevname) {
		this.vmdevname = vmdevname;
	}

	@Override
	public String toString() {
		return String
				.format("VMDevInfo [vmname=%s, vmdevtype=%s, target=%s, storetype=%s, hostdevname=%s, vmdevname=%s]",
						vmname, vmdevtype, target, storetype, hostdevname,
						vmdevname);
	}
	
	

}
