package com.hp.idc.resm.resource.model
{
	import mx.collections.ArrayCollection;
	import mx.containers.FormItem;
	
	import spark.components.ComboBox;
	import com.hp.idc.resm.model.Code;
	import com.hp.idc.resm.model.ModelAttribute;
	import com.hp.idc.resm.model.StringAttributeDefine;
	import com.hp.idc.resm.resource.AttributeBase;

	[Bindable]
	[RemoteClass(alias="com.hp.idc.resm.resource.StringAttribute")]
	public class StringAttributeCombo implements AttributeBase
	{
		[Transient]
		private var formItem:FormItem;
		[Transient]
		private var combo:ComboBox;
		
		
		public var value:String;
		public var attribute:ModelAttribute;
		
		public function createField():FormItem{
			
			var define:StringAttributeDefine = attribute.define as StringAttributeDefine;
			this.combo = new ComboBox;
			this.combo.dataProvider = define.codes;
			this.formItem = new FormItem();
			formItem.label = attribute.name;
			formItem.addElement(combo);
			return formItem;
		}
		
		public function setValue():void{
			var array:ArrayCollection = new ArrayCollection();
			for(var i:int=0;i<array.length;i++){
				var code:Code = array.getItemAt(i) as Code;
				if(code.name==value){
					this.combo.selectedItem = code;
					break;
				}
			}
		}
		
		public function  refreshValue():void{
			this.value = combo.selectedItem.name as String;
		}
		
		public function getFormItem():FormItem{
			return this.formItem;
		}
	}
}