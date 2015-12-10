package com.hp.idc.business.mapping
{
	[RemoteClass(alias="com.hp.idc.business.entity.Product")]
	[Bindable]
	public class Product
	{
		public var id:int;
		
		public var name:String;
		
		public var desc:String;
		
		public var subParam:String;
		
		public var orgId:int;
		
		public var doneCode:int;
		
		public var createDate:String;
		
		public var doneDate:String;
		
		public function Product()
		{
		}
	}
}