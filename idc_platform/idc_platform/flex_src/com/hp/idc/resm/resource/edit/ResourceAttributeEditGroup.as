package com.hp.idc.resm.resource.edit
{
	import com.hp.idc.components.PetAlert;
	import com.hp.idc.resm.resource.AttributeBase;
	
	import mx.collections.ArrayCollection;
	import mx.controls.Alert;
	
	import spark.components.Group;
	import spark.components.VGroup;

	public class ResourceAttributeEditGroup implements IResourceAttributeEditGroup
	{
		protected var attributes:ArrayCollection = new ArrayCollection();
		
		protected var group:Group;
		
		protected var editTag:Boolean = false;
		
		public function ResourceAttributeEditGroup(editTag:Boolean)
		{
			this.editTag = editTag;
		}
		
		public function setAttributes(attributes:ArrayCollection):void {
			this.attributes.addAll(attributes);
		}
		
		public function validate():Boolean
		{
			return true;
		}
							 
		public function getResourceAttributes():ArrayCollection{
			return attributes;
		}
		
		protected function isEdit():Boolean{
			return editTag;
		}
		
		public function getContainer(edit:Boolean=true):Group{
			createContainer();
			doLayout(edit);
			return group;
		}
		
		public function createContainer():void{
			this.group = new VGroup();
		}
		
		public function doLayout(edit:Boolean=true):void{
			for(var i:int=0;i<attributes.length;i++){
				var attribute:IResourceAttributeEditBase = attributes.getItemAt(i) as IResourceAttributeEditBase;
				group.addElement(attribute.getFormItem());
			}
		}
	}
}