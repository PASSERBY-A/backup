package com.hp.idc.components.widgets.renderers
{
	import com.hp.idc.components.widgets.ComboBoxTree;
	
	import mx.controls.Tree;
	import mx.events.ListEvent;
	
	public class ComboBoxTreeRender extends Tree
	{
		[Bindable]
		public var outerDocument:ComboBoxTree;
		public function ComboBoxTreeRender()
		{
			super();
			this.addEventListener(ListEvent.CHANGE,updateLabel)
		}
		
		public function updateLabel(event:ListEvent):void{
			outerDocument._label = event.currentTarget.selectedItem[this.labelField];      
			outerDocument.treeSelectedItem = event.currentTarget.selectedItem;
		}
	}
}