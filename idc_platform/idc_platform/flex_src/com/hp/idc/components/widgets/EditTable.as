package com.hp.idc.components.widgets
{
	import com.hp.idc.components.PetAlert;
	import com.hp.idc.components.SelectModelWindow;
	import com.hp.idc.resm.event.EditTableEvent;
	import com.hp.idc.resm.model.ModelAttribute;
	
	import flash.events.MouseEvent;
	
	import mx.collections.ArrayCollection;
	import mx.controls.Spacer;
	import mx.managers.PopUpManager;
	
	import spark.components.Button;
	import spark.components.DataGrid;
	import spark.components.HGroup;
	import spark.components.VGroup;
	import spark.components.gridClasses.GridColumn;
	
	public class EditTable extends HGroup
	{
		private var datagrid:DataGrid;
		private var buttonBar:VGroup;
		
		private var _columns:ArrayCollection = new ArrayCollection();
		
		private var buttonNew:Button ;
		private var buttonUpdate:Button ;
		private var buttonDelete:Button ;
		private var buttonUp:Button ;
		private var buttonDown:Button ;
		
		public function EditTable(columns:ArrayCollection)
		{
			super();
			this.percentWidth = 100;
			this.percentHeight = 100;
			initDataGrid()
			initButtonBar();
			this.columns = columns;
			initButtonEvent();
		}
		
		public function set columns(columns:ArrayCollection):void{
			this._columns = columns;
			initGridColumns();
		}
		
		public function get columns():ArrayCollection{
			return this._columns;
		}
		
		private function initButtonEvent():void{
			this.buttonNew.addEventListener(MouseEvent.CLICK,newRowHandle);
			this.buttonUpdate.addEventListener(MouseEvent.CLICK,updateRowHandle);
		}
		
		/**
		 * 添加新纪录
		 */
		private function newRowHandle(event:MouseEvent):void{
			var newRowWindow:NewRowWindow = new NewRowWindow(_columns);
			newRowWindow.title = "添加新纪录";
			newRowWindow.width = 640;
			newRowWindow.height = 360;
			
			newRowWindow.addEventListener(EditTableEvent.ROWADD,addRow)
			
			PopUpManager.addPopUp(newRowWindow,parent,true);
			PopUpManager.centerPopUp(newRowWindow);
		}
		
		/**
		 * 修改新纪录
		 */
		private function updateRowHandle(event:MouseEvent):void{
			if(datagrid.selectedItem==null){
				PetAlert.show("没有选择任何记录！！！","提示信息");
			}
			var updateRowWindow:UpdateRowWindow = new UpdateRowWindow(_columns,datagrid.selectedItem);
			updateRowWindow.title = "修改纪录";
			updateRowWindow.width = 640;
			updateRowWindow.height = 360;
			
			updateRowWindow.addEventListener(EditTableEvent.ROWUPDATE,updateRow)
			
			PopUpManager.addPopUp(updateRowWindow,parent,true);
			PopUpManager.centerPopUp(updateRowWindow);
			
			
		}
		
		
		/**
		 * 添加记录的回调函数
		 */
		private function addRow(event:EditTableEvent):void{
			var o:Object = event.row;
			var dataprovider:ArrayCollection = this.datagrid.dataProvider==null?new ArrayCollection:this.datagrid.dataProvider as ArrayCollection;
			dataprovider.addItem(o);
			this.datagrid.dataProvider = dataprovider;
		}
		
		
		/**
		 * 修改记录的回调函数
		 */
		private function updateRow(event:EditTableEvent):void{
			var o:Object = event.row;
			var oldRecord:Object = event.oldRow;
			var dataprovider:ArrayCollection = this.datagrid.dataProvider==null?new ArrayCollection:this.datagrid.dataProvider as ArrayCollection;
			for(var i:int=0;i<dataprovider.length;i++){
				if(dataprovider.getItemAt(i)==o){
					dataprovider.removeItemAt(i);
					break;
				}
			}
			dataprovider.addItem(o);
			this.datagrid.dataProvider = dataprovider;
		}
		
		
		public function initGridColumns():void{
			if(this._columns==null){
				return;
			}
			var cols:ArrayCollection = new ArrayCollection();
			for(var i:int=0;i<_columns.length;i++){
				var col:ModelAttribute = _columns.getItemAt(i) as ModelAttribute;
				var gridColumn:GridColumn = new GridColumn();
				gridColumn.headerText = col.name;
				gridColumn.dataField = col.attrId;
				cols.addItem(gridColumn);
			}
			this.datagrid.columns = cols;
		}
		
		/**
		 * 初始化grid
		 */
		private function initDataGrid():void{
			//初始化gird
			this.datagrid = new DataGrid();
			this.datagrid.percentWidth = 100;
			this.datagrid.percentHeight = 100;
			var gridGroup:VGroup = new VGroup();
			gridGroup.paddingLeft = 10;
			gridGroup.paddingTop = 10;
			gridGroup.percentHeight = 100;
			gridGroup.percentWidth = 100;
			gridGroup.addElement(datagrid);
			this.addElement(gridGroup);
		}
		
		/**
		 * 初始化功能按钮菜单
		 */
		private function initButtonBar():void{
			//初始化功能按钮菜单
			this.buttonBar = new VGroup();
			buttonBar.horizontalAlign = "center";
			buttonBar.width = 150;
			buttonBar.percentHeight = 100;
			buttonBar.paddingTop = 20;
			buttonBar.gap = 8;
			
			//功能按钮
			
			buttonNew = new Button();
			buttonUpdate = new Button();
			buttonDelete = new Button();
			buttonUp = new Button();
			buttonDown = new Button();
			
			buttonNew.label = "新建";
			buttonNew.width = 100;
			buttonUpdate.label = "修改";
			buttonUpdate.width = 100;
			buttonDelete.label = "删除";
			buttonDelete.width = 100;
			buttonUp.label = "上移";
			buttonUp.width = 100;
			buttonDown.label = "下移";
			buttonDown.width = 100;
			
			buttonBar.addElement(buttonNew);
			buttonBar.addElement(buttonUpdate);
			buttonBar.addElement(buttonDelete);
			
			var space:Spacer = new Spacer();
			space.height = 10;
			
			buttonBar.addElement(space);
			buttonBar.addElement(buttonUp);
			buttonBar.addElement(buttonDown);
			
			this.addElement(buttonBar);
		}
	}
}