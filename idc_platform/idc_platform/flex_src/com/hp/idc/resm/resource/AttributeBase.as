package com.hp.idc.resm.resource
{
	
	import com.hp.idc.resm.model.ModelAttribute;
	
	import spark.components.FormItem;

	public interface AttributeBase
	{
		
		function createField():void;
		
		function setValue(o:Object):void;
		
		function refreshValue():void;
		
		function getFormItem():FormItem;
		
		function setAttribute(modelAttribute:ModelAttribute):void;
		
		function isBigComponent():Boolean;
		
		function getValue():String;
		
		function getAttribute():ModelAttribute;
		
		function setEdit(edit:Boolean=true):void;
		
		function setDefaultValue():void;
	}
}