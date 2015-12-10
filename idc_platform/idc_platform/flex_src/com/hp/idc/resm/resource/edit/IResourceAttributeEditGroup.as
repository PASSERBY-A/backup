package com.hp.idc.resm.resource.edit
{
	
	import mx.collections.ArrayCollection;
	
	import spark.components.Group;
	import spark.components.VGroup;

	public interface IResourceAttributeEditGroup extends IResourceAttributeEdit
	{
		/**
		 * 返回资源属性
		 */
		function getResourceAttributes():ArrayCollection;
		
		/**
		 * 返回容器 
		 */
		function getContainer(edit:Boolean=true):Group;
		
		/**
		 * 
		 */
		function doLayout(edit:Boolean=true):void;
	}
}