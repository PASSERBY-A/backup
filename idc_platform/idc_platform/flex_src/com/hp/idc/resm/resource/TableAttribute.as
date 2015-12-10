package com.hp.idc.resm.resource
{
	import com.hp.idc.resm.model.ModelAttribute;
	
	import mx.core.UIComponent;
	
	import spark.components.FormItem;
	
	[Bindable]
	[RemoteClass(alias="com.hp.idc.resm.resource.TableAttribute")]
	public class TableAttribute implements AttributeBase
	{
		
		public var text:String;
		public var value:Object;
		public var attribute:ModelAttribute;
		
		
		[Transient]
		private var formItem:FormItem;
		[Transient]
		private var uicomponent:UIComponent;
		
		public function createField():void
		{
			
		}
		
		public function setValue(o:Object):void
		{
			if(o==null)
				return;
		}
		
		public function refreshValue():void
		{
		}
		
		public function getFormItem():FormItem
		{
			return this.formItem;
		}
		
		public function setAttribute(modelAttribute:ModelAttribute):void
		{
			this.attribute = modelAttribute;
		}
		
		public function isBigComponent():Boolean{
			return true;
		}
		
		public function getValue():String{
			return value.toString();
		}
		
		public function getAttribute():ModelAttribute{
			return attribute;
		}
		
		public function setEdit(edit:Boolean=true):void{
		}
		
		public function setDefaultValue():void{
			if(this.uicomponent == null)
				return;
			if(this.attribute == null)
				return;
			this.setValue(this.attribute.defaultValue);
		}
	}
}