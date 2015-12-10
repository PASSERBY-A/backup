package com.hp.idc.resm.security
{
	import mx.collections.ArrayCollection;

	[Bindable]
	[RemoteClass(alias="com.hp.idc.resm.security.UserRole")]
	public class UserRole
	{
		public var user :Person; 
		public var permissions :ArrayCollection; 
		public var authInfos :ArrayCollection;
	}
}