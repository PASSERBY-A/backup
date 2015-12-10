package com.hp.idc.business.mapping
{
	[RemoteClass(alias="com.hp.idc.business.entity.ProductCatalog")]
	[Bindable]
	public class ProductCatalog
	{
		public var id:int;
		
		public var name:String;
		
		public var description:String;
		
		public var status:int;
		
		public var pkgId:int;
		
		public var effectDate:String;
		
		public var expireDate:String;
		
		public var createDate:String;
		
		public var creator:int;
		
		public var upldateDate:String;
		
		public var updateOperId:int;
		
		public function ProductCatalog()
		{
		}
	}
}