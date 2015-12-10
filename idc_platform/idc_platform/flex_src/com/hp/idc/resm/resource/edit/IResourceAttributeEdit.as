package com.hp.idc.resm.resource.edit
{
	import mx.collections.ArrayCollection;
	import mx.containers.FormItem;
	
	public interface IResourceAttributeEdit
	{
		/**
		 * 验证表单项或者整个容器
		 */
		function validate():Boolean;
		
	}
}