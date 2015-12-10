package com.hp.idc.components
{
	import mx.collections.ArrayCollection;

	[Bindable]
	public class TreeItem
	{
		public function TreeItem(p:TreeItem, x :XML, indent:int = 0)
		{
			parent = p;
			isOpened = false;
			label = x.@label;
			id = x.@id;
			enable = x.@enable;
			if (x.hasOwnProperty("check") && x.@check == "true")
				allowCheck = true;
				
			childs = new ArrayCollection();
			isLeaf = false;
			this.indent = indent;
			for each (var xml :XML in x.children())
			{
				childs.addItem(new TreeItem(this, xml, indent + 18));
			}
			if (childs.length == 0)
				isLeaf = true;
		}
		
		public static function createDataProviderFromXML(doc :XML) : TreeItemCollection {
			var list:TreeItemCollection = new TreeItemCollection();
			for each (var xml:XML in doc.children())
			{
				var ti :TreeItem = new TreeItem(null, xml);
				list.addItem(ti);
				ti.adjustLabel();
			}
			return list;
		}
		
		public static function createDataProvider(doc :String) : TreeItemCollection {
			return createDataProviderFromXML(new XML(doc));
		}
		
		public function adjustLabel() : void {
			for each (var ti:TreeItem in childs) {
				ti.adjustLabel();
			}
			if (parent != null) {
				if (label.indexOf(parent.label + "/") == 0)
					label = label.substr(parent.label.length + 1);
			}
		}
		
		public function closeChild() :int {
			if (!isOpened)
				return 0;
			isOpened = false;
			var count:int = childs.length;
			for each (var item:TreeItem in childs)
				count += item.closeChild();
			return count;
		}
		
		public var isOpened :Boolean;
		public var isLeaf :Boolean;
		public var label :String;
		public var id :String;
		public var indent :int;
		public var parent :TreeItem;
		public var checked :Boolean = false;
		public var allowCheck :Boolean = false;
		public var enable :String;
		
		public var childs :ArrayCollection;
	}
}
