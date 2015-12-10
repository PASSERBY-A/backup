package com.hp.idc.resm.resource.edit
{
	import com.hp.idc.components.PetAlert;
	import com.hp.idc.resm.model.ModelAttribute;
	import com.hp.idc.resm.resource.AttributeBase;
	import com.hp.idc.resm.resource.AttributeFactory;
	import com.hp.idc.resm.resource.BooleanAttribute;
	import com.hp.idc.resm.resource.DateAttribute;
	import com.hp.idc.resm.resource.DateTimeAttribute;
	import com.hp.idc.resm.resource.StringAttribute;
	import com.hp.idc.resm.resource.TimeAttribute;
	
	import mx.collections.ArrayCollection;
	import mx.controls.Alert;
	
	import spark.components.FormItem;
	
	public class ResourceAttributeEditBase implements IResourceAttributeEditBase
	{
		
		public var resourceAttribute:AttributeBase;
		
		protected var editTag:Boolean = false;
		
		protected var modelAttributeId:String ;
		
		//资源的所有值，修改模型构造函数传递的是资源属性集合
		protected var resourceAttributes:ArrayCollection = new ArrayCollection();
		//模型的所有属性值，添加模型时 构造函数传递的是模型属性集合
		protected var modelAttributes:ArrayCollection = new ArrayCollection();
		
		
		public function ResourceAttributeEditBase(eidtTag:Boolean,attributes:ArrayCollection=null)
		{
			this.editTag = eidtTag;
			
			if(attributes!=null){
				if(eidtTag){
					this.resourceAttributes = attributes;
				}else{
					this.modelAttributes = attributes;
				}
			}
		}
		
		
		public function validate():Boolean
		{
			return true;
		}
		
		protected function isEdit():Boolean{
			return editTag;
		}
		
		public function setFormItem():void{
			if(resourceAttribute==null){
				return;
			}
			resourceAttribute.createField();
//			if(this.editTag){
//				resourceAttribute.setValue();
//			}
			
			//新增资源时, 设置默认值
			this.setDefaultValue();
		}
		
		public function setDefaultValue():void {
			if(this.editTag)
				return;
			resourceAttribute.setDefaultValue();
			
		}
		
		public function setResourceAttribute(editTag:Boolean):void{
			//默认为stirngattribute
			if(editTag){//修改，构造函数为资源属性
				for(var i:int = 0;i<resourceAttributes.length;i++){
					var attributebase:Object = resourceAttributes.getItemAt(i);
					var attrId:String = attributebase.attribute.attrId as String;
					if(this.modelAttributeId==attrId){
						this.resourceAttribute = attributebase as StringAttribute;
						break;
					}
				}
			}else{//添加，构造函数为模型属性
				for(var j:int=0;j<modelAttributes.length;j++){
					var modelAttribute:ModelAttribute = modelAttributes.getItemAt(j) as ModelAttribute;
					if(this.modelAttributeId == modelAttribute.attrId){
						this.resourceAttribute = AttributeFactory.factory(modelAttribute.define.type);
						resourceAttribute.setAttribute(modelAttribute);
						break;
					}
				}
			}
		}
		
		public function getResourceAttribute():AttributeBase{
			resourceAttribute.refreshValue();
			return resourceAttribute;
		}
		
		
		public function getFormItem():FormItem{
			if(resourceAttribute==null){
				return null;
			}
			return this.resourceAttribute.getFormItem();
		}
	}
}