// ActionScript file
package com.hp.idc.resm.ui
{
	[Bindable]
	[RemoteClass(alias="com.hp.idc.resm.ui.Menu")]
	public class Menu
	{
		public var id :String;
		public var name :String;
		public var remark :String;
		public var groupName :String;
		public var icon :String;
		public var groupIcon :String;
		public var action :String;
		public var actionParam :String;
		public var moduleId :int;
	}
}
