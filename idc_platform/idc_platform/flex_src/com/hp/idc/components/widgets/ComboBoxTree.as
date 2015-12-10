package com.hp.idc.components.widgets
{
	import com.hp.idc.components.widgets.renderers.ComboBoxTreeRender;
	
	import mx.collections.ArrayCollection;
	import mx.controls.ComboBox;
	import mx.controls.listClasses.ListBase;
	import mx.core.ClassFactory;
	import mx.events.FlexEvent;
	import mx.events.ListEvent;
	
	
	public class ComboBoxTree extends ComboBox
	{
		
		[Bindable]  
		public var _label:String;
		[Bindable]  
		public var treeSelectedItem:Object;
		
		private var _factory:ClassFactory;
		public function get factory():ClassFactory {
			if (_factory == null)
			{
				_factory = new ClassFactory();
				_factory.generator = ComboBoxTreeRender;
				_factory.properties = {
					width:this.width, 
					showRoot:false,
					height:400,
					outerDocument:this
				};
			}
			return _factory;        
		}

		
		public function ComboBoxTree()
		{
			super();
			this.dropdownFactory = factory;
		}
		
		
		/**
		 *  @private
		 *  Make sure the drop-down width is the same as the rest of the ComboBox
		 */
		override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void
		{
			super.updateDisplayList(unscaledWidth, unscaledHeight);
			if(dropdown && _label != null){
				text = _label;  
			}else{
				text = "请选择...";  
			}
		}
		
		override public function set selectedItem(value:Object):void
		{
			super.selectedItem = value;
			this.treeSelectedItem = value;
			if(value==null){
				this._label = "";
				text = _label;
			}else{
				this._label = treeSelectedItem.@label;
				text = _label;
			}
		}
	}
}