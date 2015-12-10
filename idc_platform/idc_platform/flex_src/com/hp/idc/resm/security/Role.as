package com.hp.idc.resm.security
{
	import mx.collections.ArrayCollection;

	[Bindable]
	[RemoteClass(alias="com.hp.idc.resm.security.Role")]
	public class Role
	{
		public var id :String; 
		public var name :String; 
		public var remark :String; 
		public var priority :int; 
		public var relationDesc :String;
		public var organizations :Array; 
		public var persons :Array; 
	}
}
