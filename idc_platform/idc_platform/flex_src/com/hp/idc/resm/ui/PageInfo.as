package com.hp.idc.resm.ui
{
	import mx.collections.ArrayCollection;

	[Bindable]
	[RemoteClass(alias="com.hp.idc.resm.ui.PageInfo")]
	public class PageInfo
	{
		public var totalCount:int;
		public var currentPage:int;
		public var totalPage:int;
		public var index:int;
		public var pageCount:int;
		public var data:ArrayCollection;
	}
}