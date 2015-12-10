package com.hp.idc.components.widgets
{
	import com.hp.idc.components.widgets.renderers.TreeCheckBoxRenderer;
	
	import mx.core.ClassFactory;
	
	public class CheckBoxTree extends AutoSizeTree
	{
		public var checkField:String = "checked";
		
		public function CheckBoxTree()
		{
			super();
			
			this.itemRenderer = new ClassFactory(TreeCheckBoxRenderer);
			this.rendererIsEditor = true;
			
			this.labelField = "@label";
			this.showRoot = false;
			this.checkField = "@checked";
			this.height = 400;
		}
		
	}
}