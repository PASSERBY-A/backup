package com.hp.idc.resm.resource.edit
{
	import com.hp.idc.components.PetAlert;
	import com.hp.idc.resm.model.ModelAttribute;
	import com.hp.idc.resm.resource.AttributeBase;
	import com.hp.idc.resm.resource.AttributeFactory;
	
	import mx.collections.ArrayCollection;
	
	import spark.components.Form;
	import spark.components.FormItem;
	import spark.components.HGroup;
	import spark.components.VGroup;

	public class DefaultGroup extends ResourceAttributeEditGroup
	{
		//布局容器，左
		private var leftgroup:Form;
		//布局容器，右
		private var rightgroup:Form;
		
		//布局容器，下，存放大的输入域或者列表数据
		private  var bottom:Form;
		
		
		public function DefaultGroup(attributes:ArrayCollection,editTag:Boolean)
		{
			super(editTag);
			initAttributes(attributes);
		}
		
		private var ignoredAttributes :Array = ["create_time", "last_update_time", "last_update_by",
			"id","order_id", "customer_id", "contract_start", "contract_end", "task_link", "searchcode"];
		
		private function initAttributes(attributes:ArrayCollection):void{
			if(this.editTag){
				for(var j:int=0;j<attributes.length;j++){
					var attr:AttributeBase = attributes.getItemAt(j) as AttributeBase;
					var ignorFlag :Boolean = false;
					for (var n:int = 0; n < ignoredAttributes.length; n++)
					{
						if (ignoredAttributes[n] == attr.getAttribute().attrId)
						{
							ignorFlag = true;
							break;
						}
					}
					if (ignorFlag)
						continue;
					var resourceAttributeEditBase:ResourceAttributeEditBase = new ResourceAttributeEditBase(true);
					resourceAttributeEditBase.resourceAttribute = attr;
					this.attributes.addItem(resourceAttributeEditBase);
				}
			 
			} else {
				for(var k:int=0;k<attributes.length;k++){
					var modelAttribute:ModelAttribute = attributes.getItemAt(k) as ModelAttribute;
					var ignorFlag1 :Boolean = false;
					for (var m:int = 0; m < ignoredAttributes.length; m++)
					{
						if (ignoredAttributes[m] == modelAttribute.attrId)
						{
							ignorFlag1 = true;
							break;
						}
					}
					if (ignorFlag1)
						continue;
					var resourceAttributeEditBase1:ResourceAttributeEditBase = new ResourceAttributeEditBase(false);
					resourceAttributeEditBase1.resourceAttribute = AttributeFactory.factory(modelAttribute.define.type);
					resourceAttributeEditBase1.resourceAttribute.setAttribute(modelAttribute);
					this.attributes.addItem(resourceAttributeEditBase1);
				}
			}
		}
		
		override public function createContainer():void{
			this.group = new VGroup();
			this.group.percentWidth = 100;
			
			this.leftgroup = new Form();
			this.rightgroup = new Form();
			leftgroup.percentWidth = 50;
			rightgroup.percentWidth = 50;
			
			
			var hgroup:HGroup = new HGroup();
			hgroup.percentWidth = 100;
			
			hgroup.addElement(leftgroup);
			hgroup.addElement(rightgroup);
			
			bottom = new Form();
			bottom.percentWidth = 100;
			bottom.percentHeight = 100;
			
			
			group.addElement(hgroup);
			group.addElement(bottom);
		}
		
		
		override public function doLayout(edit:Boolean=true):void{
			var tag:int = 0;
			for(var i:int=0;i<this.attributes.length;i++){
				var resourceAttributeBase:ResourceAttributeEditBase = this.attributes.getItemAt(i) as ResourceAttributeEditBase;
				resourceAttributeBase.setFormItem();
				resourceAttributeBase.resourceAttribute.setEdit(edit);
				var formitem:FormItem = resourceAttributeBase.getFormItem();
				if(resourceAttributeBase.resourceAttribute.isBigComponent()){
					bottom.addElement(formitem);
				}else{
					if(tag==0){
						leftgroup.addElement(formitem);
						tag = 1;
					}else{
						rightgroup.addElement(formitem);
						tag = 0;
					}
				}
			}
		}
	}
}