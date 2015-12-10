/****************************************************************************
 **
 ** This file is part of yFiles FLEX 1.4.2.
 **
 ** yWorks proprietary/confidential. Use is subject to license terms.
 **
 ** Unauthorized redistribution of this file or of a byte-code version
 ** of this file is strictly forbidden.
 **
 ** Copyright (c) 2006-2010 by yWorks GmbH, Vor dem Kreuzberg 28, 
 ** 72070 Tuebingen, Germany. All rights reserved.
 **
 ***************************************************************************/
package com.hp.idc.util
{
	public class ConfigStruct {
		
	    public var contextRoot:String;
	    public var serviceExtension:String; 
	    
	    public function ConfigStruct( contextRoot:String=null, serviceExtension:String=null ) {
	        this.contextRoot = contextRoot;
	        this.serviceExtension = serviceExtension;
	    }  
	}
}