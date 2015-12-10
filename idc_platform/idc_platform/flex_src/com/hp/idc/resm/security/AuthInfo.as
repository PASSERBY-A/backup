package com.hp.idc.resm.security
{
	[Bindable]
	[RemoteClass(alias="com.hp.idc.resm.security.AuthInfo")]
	public class AuthInfo
	{
		public var startIndex :int; 
		public var endIndex :int; 
		public var role :Role;
		public var authType :int;
		
	
	}
}