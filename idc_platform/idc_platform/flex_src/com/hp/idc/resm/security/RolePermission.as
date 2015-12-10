package com.hp.idc.resm.security
{
	[Bindable]
	[RemoteClass(alias="com.hp.idc.resm.security.RolePermission")]
	public class RolePermission
	{
		public var id :int = -1;
		public var roleId :String; 
		public var permId :String; 
		public var permParam :String; 
		public var remark :String; 
		public var priority :int = 1; 
		public var paramDesc:String;
		public var permisson :BasicPermission;
	}
}
