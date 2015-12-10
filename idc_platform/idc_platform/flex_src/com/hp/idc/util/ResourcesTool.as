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
	import com.yworks.resources.images.ImageProvider;
	
	public class ResourcesTool {
		
		[Embed(source="svg/movie1.svg")]
		private static var movie1SVG:Class;
		
		[Embed(source="svg/movie2.svg")]
		private static var movie2SVG:Class;
		
		[Embed(source="svg/movie3.svg")]
		private static var movie3SVG:Class;
		
		[Embed(source="svg/camera.svg")]
		private static var cameraSVG:Class;
		
		[Embed(source="svg/cdrom.svg")]
		private static var cdromSVG:Class;
		
		[Embed(source="svg/coffee.svg")]
		private static var coffeeSVG:Class;
		
		[Embed(source="svg/computer.svg")]
		private static var computerSVG:Class;
		
		[Embed(source="svg/harddisk.svg")]
		private static var harddiskSVG:Class;
		
		[Embed(source="svg/network.svg")]
		private static var networkSVG:Class;
		
		[Embed(source="svg/printer.svg")]
		private static var printerSVG:Class;
		
		[Embed(source="svg/scanner.svg")]
		private static var scannerSVG:Class;
		
		[Embed(source="svg/zipdisk.svg")]
		private static var zipdiskSVG:Class;
		
		private static var _instance:ResourcesTool;
		
		public static function init():void {
			if( null == _instance ) {
				_instance = new ResourcesTool();
			}
			_instance.init();
		}
		
		private function init():Boolean {
			
			ImageProvider.addImage( "movie1", movie1SVG );
			ImageProvider.addImage( "movie2", movie2SVG );
			ImageProvider.addImage( "movie3", movie3SVG );
			
			ImageProvider.addImage( "camera", cameraSVG );
			ImageProvider.addImage( "cdrom", cdromSVG );
			ImageProvider.addImage( "coffee", coffeeSVG );
			ImageProvider.addImage( "computer", computerSVG );
			ImageProvider.addImage( "harddisk", harddiskSVG );
			ImageProvider.addImage( "network", networkSVG );
			ImageProvider.addImage( "printer", printerSVG );
			ImageProvider.addImage( "scanner", scannerSVG );
			ImageProvider.addImage( "zipdisk", zipdiskSVG );
			
			return true;
		}
		
	}
}