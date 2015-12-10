package com.hp.idc.resm.security
{
	import com.hp.idc.components.PetAlert;
	import com.hp.idc.components.TreeItem;
	import com.hp.idc.components.TreeItemCollection;
	import com.hp.idc.components.TreeItemRenderer;
	
	import flash.display.DisplayObject;
	import flash.events.Event;
	
	import mx.core.IVisualElement;
	import mx.events.FlexEvent;
	import mx.rpc.events.ResultEvent;
	import mx.rpc.remoting.RemoteObject;
	
	import spark.components.List;
	import spark.components.TextArea;
	
	
	[Bindable]
	[RemoteClass(alias="com.hp.idc.resm.security.ModelPermission")]
	public class ModelPermission extends BasicPermission
	{
		private var list:List = null;
		private var coll:TreeItemCollection = null;
	
		private function onModelLoaded(event:ResultEvent):void
		{
			coll  =  TreeItem.createDataProvider(event.result as String);
			coll.setCheckMode();
			list.dataProvider = coll;
			if (_param != null)
				coll.value = _param;
		}
		
		public function add(ev:Event) : void
		{
			var r:RemoteObject = new RemoteObject();
			r.destination = "modelService";
			r.addEventListener(ResultEvent.RESULT, onModelLoaded);
			r.getModelTree();
		}
		
		override public function createControl() :IVisualElement {
			list = new List();
			list.itemRenderer = new TreeItemRenderer();
			list.percentHeight = 100;
			list.width = 250;
			list.addEventListener(FlexEvent.CREATION_COMPLETE, add);
			return list;
		}
		
		override public function get param() : String
		{
			if (coll == null)
				return "";
			return coll.value;
		}
		
		private var _param :String = null;

		override public function set param(s :String) : void 
		{
			_param = s;
			if (coll != null)
				coll.value = s;
		}
		
	}
}