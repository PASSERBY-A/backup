package com.hp.idc.components.widgets
{
	import com.hp.idc.resm.event.EditTableEvent;
	import com.hp.idc.resm.model.ModelAttribute;
	import com.hp.idc.resm.resource.AttributeBase;
	import com.hp.idc.resm.resource.AttributeFactory;
	
	import flash.events.Event;
	import flash.events.MouseEvent;
	
	import mx.collections.ArrayCollection;
	import mx.events.CloseEvent;
	import mx.managers.PopUpManager;
	
	import spark.components.Button;
	import spark.components.Form;
	import spark.components.FormItem;
	import spark.components.HGroup;
	import spark.components.TitleWindow;
	import spark.components.VGroup;
	
	public class UpdateRowWindow extends TitleWindow
	{
		
		private var columns:ArrayCollection;
		
		private var record:Object;
		
		private var attributes:ArrayCollection = new ArrayCollection();
		
		private var leftForm:Form;
		private var rightForm:Form;
		private var bottomForm:Form;
		
		private var buttonSave:Button;
		private var buttonConcel:Button;
		
		public function UpdateRowWindow(columns:ArrayCollection,record:Object)
		{
			super();
			this.columns = columns;
			this.record = record;
			initAttributes();
			initForm();
			initFormItem();
			initButton();
			this.addEventListener(CloseEvent.CLOSE,closeWindowHandle);
			
		}
		
		private function closeWindowHandle(event:Event):void{
			PopUpManager.removePopUp(this);
		}
		
		private function initAttributes():void{
			for(var i:int=0;i<columns.length;i++){
				var col:ModelAttribute = columns.getItemAt(i) as ModelAttribute;
				var attribute:AttributeBase = AttributeFactory.factory(col.define.type);
				attribute.setAttribute(col);
				attributes.addItem(attribute);
			}
		}
		
		/**
		 * 初始化列表控件
		 */
		private function initFormItem():void{
			var tag:int = 0;
			for(var i:int=0;i<attributes.length;i++){
				var attribute:AttributeBase = attributes.getItemAt(i) as AttributeBase;
				attribute.createField();
				var formitem:FormItem = attribute.getFormItem();
				if(attribute.isBigComponent()){
					bottomForm.addElement(formitem);
				}else{
					if(tag==0){
						leftForm.addElement(formitem)
						tag = 1;
					}else{
						rightForm.addElement(formitem)
						tag = 0;
					}
				}
				//初始化值
				var itemValue:Object = record[attribute.getAttribute().attrId];
				if(itemValue!=null){
					attribute.setValue(itemValue);
				}
			}
		}
		
		private function initButton():void{
			buttonSave.label = "保存";
			buttonConcel.label = "取消";
			
			buttonConcel.addEventListener(MouseEvent.CLICK,closeWindowHandle);
			buttonSave.addEventListener(MouseEvent.CLICK,newRowHandle);
		}
		
		private function newRowHandle(event:MouseEvent):void{
			var row:Object = new Object();
			for(var i:int=0;i<attributes.length;i++){
				var attribute:AttributeBase = attributes.getItemAt(i) as AttributeBase;
				attribute.refreshValue();
				row[attribute.getAttribute().attrId] = attribute.getValue();
			}
			
			var e:EditTableEvent = new EditTableEvent(EditTableEvent.ROWADD,row);
			dispatchEvent(e);
			PopUpManager.removePopUp(this);
		}
		
		private function initForm():void{
			var v:VGroup = new VGroup();
			v.percentHeight = 100;
			v.percentWidth = 100;
			v.paddingTop = 10;
			v.paddingRight = 10;
			v.paddingBottom = 10;
			v.paddingLeft =10;
			
			var hAlignFormGroup:HGroup = new HGroup();
			hAlignFormGroup.percentWidth = 100;
			
			var bFormGroup:VGroup = new VGroup();
			bFormGroup.percentWidth = 100;
			bFormGroup.percentHeight = 100;
			
			leftForm = new Form();
			rightForm = new Form();
			bottomForm = new Form();
			leftForm.percentWidth = 50;
			rightForm.percentWidth = 50;
			leftForm.percentHeight = 100;
			rightForm.percentHeight = 100;
			
			
			bottomForm.percentWidth = 100;
			
			var buttonHgroup:HGroup = new HGroup();
			buttonHgroup.horizontalAlign = "right";
			buttonHgroup.paddingRight = 10;
			buttonHgroup.paddingBottom = 10;
			buttonHgroup.percentWidth = 98;
			
			buttonSave = new Button;
			buttonConcel = new Button;
			
			buttonHgroup.addElement(buttonSave);
			buttonHgroup.addElement(buttonConcel);
			
			
			hAlignFormGroup.addElement(leftForm);
			hAlignFormGroup.addElement(rightForm);
			bFormGroup.addElement(bottomForm);
			
			v.addElement(hAlignFormGroup);
			v.addElement(bFormGroup);
			v.addElement(buttonHgroup);
			this.addElement(v);
		}
	}
}