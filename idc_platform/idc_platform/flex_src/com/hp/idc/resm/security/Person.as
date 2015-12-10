package com.hp.idc.resm.security
{
	import com.hp.idc.resm.resource.ResourceObject;

	[Bindable]
	[RemoteClass(alias="com.hp.idc.resm.security.Person")]
	public class Person extends ResourceObject
	{
		public function Person()
		{
		}
	}
}