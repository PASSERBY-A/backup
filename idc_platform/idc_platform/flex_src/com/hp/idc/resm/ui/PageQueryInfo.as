package com.hp.idc.resm.ui
{
	[Bindable]
	[RemoteClass(alias="com.hp.idc.resm.ui.PageQueryInfo")]
	public class PageQueryInfo
	{
		public var startPage:int = 1;
		public var pageCount:int;
		public var sortBy:String;
	}
}