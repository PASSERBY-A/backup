/**   
 * @Title: SysResource.java 
 * @Description: TODO
 * @date 2011-5-26 下午03:05:31   
 * @version 1.0  
 */
package com.hp.idc.system.security.bo;     

/**
 * 系统资源接口
 * @InterfaceName: SysResource
 * @Descprition: 系统资源类应该实现该接口
 * @author <a href="mailto:si-qi.liang@hp.com">Liang, Si-Qi</a>
 * @version 1.0
 */
public interface SysResource {
	
	public String getResourceId();
	
	public SysResource getParentResource();
	
	public String getResourceType();
	
	public String getResourceName();

}
 