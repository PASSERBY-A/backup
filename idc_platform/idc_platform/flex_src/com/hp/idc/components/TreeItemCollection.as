package com.hp.idc.components
{
	import mx.collections.ArrayCollection;

	public class TreeItemCollection extends ArrayCollection
	{
		public function setCheckMode() : void
		{
			for each (var ti:TreeItem in this)
			{
				setCheckModeInternal(ti);
			}
		}
		
		protected function setCheckModeInternal(ti: TreeItem) : void
		{
			ti.allowCheck = true;
			for each (var ti0:TreeItem in ti.childs)
				setCheckModeInternal(ti0);
		}
		
		protected function check(ti: TreeItem, s:String) : void
		{
			ti.checked = (s.indexOf("," + ti.id + ",") != -1);
			for each (var ti0:TreeItem in ti.childs)
				check(ti0, s);
		}
		
		protected function getValueInternal(ti: TreeItem, s:String) : String
		{
			for each (var ti0:TreeItem in ti.childs)
				s = getValueInternal(ti0, s);
				if (ti.checked)
					s = s + "," + ti.id;
			return s;
		}
		
		
		public function get value() :String
		{
			var s:String = "";
			for each (var ti:TreeItem in this)
			{
				s = getValueInternal(ti, s);
			}
			if (s.length > 0)
				s = s.substr(1);
			return s;
		}
		
		public function set value(val :String) :void
		{
			for each (var ti:TreeItem in this)
			{
				check(ti, "," + val + ",");
			}
		}
		
		public function expand(item: TreeItem) :void
		{
			if (item.isOpened)
				return;
			item.isOpened = true;

			var index:int = getItemIndex(item);
			for each (var ti :TreeItem in item.childs)
				addItemAt(ti, index + 1);
		}
		
		protected function searchInternal(i:TreeItem, id:String) :TreeItem
		{
			if (i.id == id)
				return i;
			for each (var i0:TreeItem in i.childs) {
				var ti :TreeItem = searchInternal(i0, id);
				if (ti != null)
					return ti;
			}
			return null;
		}
		
		public function search(id: String) :TreeItem {
			for each (var i:TreeItem in this)
			{
				if (i.parent != null)
					continue;
				var ti :TreeItem = searchInternal(i, id);
				if (ti != null)
					return ti;
			}
			return null;
		}
		
		public function expandItem(id: String) :int
		{
			var i:TreeItem = search(id);
			if (i == null)
				return -1;
			var p:TreeItem = i;
			var l:ArrayCollection = new ArrayCollection;
			while ((p = p.parent) != null)
				l.addItem(p);
			for (var j:int = l.length - 1; j >= 0; j--)
				expand(l.getItemAt(j) as TreeItem);
			return this.getItemIndex(i);
		}
	}
}
