package com.hp.idc.resm.resource.edit
{
	import com.hp.idc.resm.resource.AttributeBase;
	
	import spark.components.FormItem;
	

	public interface IResourceAttributeEditBase extends IResourceAttributeEdit
	{
		/**
		 * 返回资源属性
		 */
		function getResourceAttribute():AttributeBase;
		
		/**
		 * 返回资源属性对象的编辑组件，根据资源属性的类型，返回的可能是textinput,textarea,combox,chechbox,tree,grid等
		 */
		function getFormItem():FormItem;
	}
}