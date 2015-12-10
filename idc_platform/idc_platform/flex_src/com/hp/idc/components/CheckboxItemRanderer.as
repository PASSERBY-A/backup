package com.hp.idc.components
{
	import flash.events.Event;   
	
	import mx.controls.CheckBox;   
	
	public class CheckboxItemRanderer extends CheckBox{   
		
		public function CheckboxItemRanderer() {   
			super();   
			this.addEventListener('change', changeHandler);   
		}   
		
		private var _data:Object;   
		override public function get data():Object {   
			return _data;   
		}   
		override public function set data(value:Object):void {   
			_data = value;   
			if(_data &amp;&amp; _data['selected']){   
				this.selected = true;   
			}else{   
				this.selected = false;   
			}   
		}   
		
		private function changeHandler(event:Event):void{   
			_data['selected'] = this.selected;   
		}   
	}   
	
}