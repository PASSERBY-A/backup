/**   
 * @Title: SysResource.java 
 * @Description: TODO
 * @date 2011-5-26 ����03:05:31   
 * @version 1.0  
 */
package com.hp.idc.system.security.bo;     

/**
 * ϵͳ��Դ�ӿ�
 * @InterfaceName: SysResource
 * @Descprition: ϵͳ��Դ��Ӧ��ʵ�ָýӿ�
 * @author <a href="mailto:si-qi.liang@hp.com">Liang, Si-Qi</a>
 * @version 1.0
 */
public interface SysResource {
	
	public String getResourceId();
	
	public SysResource getParentResource();
	
	public String getResourceType();
	
	public String getResourceName();

}
 