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
	import com.yworks.remote.HttpServiceFactory;
	
	import flash.display.DisplayObject;
	import flash.events.Event;
	import flash.events.EventDispatcher;
	import flash.net.SharedObject;
	import flash.net.registerClassAlias;
	import flash.system.Security;
	import flash.utils.ByteArray;
	
	import mx.controls.Alert;
	import mx.core.FlexGlobals;
	import mx.core.IMXMLObject;
	import mx.events.FlexEvent;
	import mx.managers.CursorManager;
	import mx.managers.PopUpManager;
	import mx.rpc.events.FaultEvent;
	import mx.rpc.events.ResultEvent;
	import mx.rpc.http.HTTPService;
	
	import spark.components.Application;
	
	
	/**
	 * Dispatched when the configuration was successful. 
	 * @eventType flash.events.Event.COMPLETE
	 */ 
	[Event(name="complete", type="flash.events.Event")]
	
	
	public class ConfigTool extends EventDispatcher implements IMXMLObject {
		
		private static const DEFAULT_CONTEXT_ROOT:String = "http://localhost:8080/yfiles-flex/";
		private static const DEFAULT_SERVICE_EXTENSION:String = "";
		
		public static const SHARED_OBJECT_KEY:String = "config";
		
		private static const CONFIG_FILE_PARAM_CONTEXT_ROOT:String = "context-root";
		private static const CONFIG_FILE_PARAM_SERVICE_EXTENSION:String = "service-extension";
		
		private static const CONNECTION_TEST_SERVICE:String = "connectionTest.jsp";
		
		private var _defaultConfigURL:ConfigStruct;
		private var _defaultConfigRelative:ConfigStruct;
		
		private var _defaultServiceExtension:String = DEFAULT_SERVICE_EXTENSION;
		
		private var _workingConfig:ConfigStruct;
		
		private var _applicationComplete:Boolean = false;
		
		private var _serverRequired:Boolean = false;
		private var _configFinished:Boolean = false;
		
		private var _triedDefaults:Boolean = false;
		
		private var _disableApplication:Boolean = true;
		
		private var _debug:Boolean = false;
		
		[Embed(source="config.xml",mimeType="application/octet-stream")]
		public static const CONFIG_XML_FILE:Class;
		public static const CONFIG_XML:XML = initConst();
		
		private static function initConst():XML {
			var ba:ByteArray = new CONFIG_XML_FILE() as ByteArray;
			return new XML(ba.readUTFBytes(ba.length));
		}
		
		public function initialized(document:Object, id:String):void {
			
			registerClassAlias( "com.hp.idc.util::ConfigStruct", ConfigStruct );
			
			this._defaultServiceExtension = getProperty( CONFIG_FILE_PARAM_SERVICE_EXTENSION );
			this._defaultServiceExtension = (this._defaultServiceExtension == null) ? "" : this._defaultServiceExtension;
			
			this._defaultConfigURL = new ConfigStruct( DEFAULT_CONTEXT_ROOT, _defaultServiceExtension );
			this._defaultConfigRelative = new ConfigStruct( "../../", _defaultServiceExtension );
			
			FlexGlobals.topLevelApplication.addEventListener(FlexEvent.APPLICATION_COMPLETE, applicationComplete);
		}
		
		public function get serverRequired():Boolean {
			return this._serverRequired;
		}
		
		public function set serverRequired( value:Boolean ):void {
			this._serverRequired = value;
		}
		
		public function get debug():Boolean {
			return this._debug;
		}
		
		public function set debug( value:Boolean ):void {
			this._debug = value;
		}
		
		/**
		 * Whether the application should be completely disabled by setting
		 * its <code>enabled</code> flag to <code>false</code> while trying to 
		 * connect to the server component
		 */ 
		public function get disableApplication():Boolean {
			return this._disableApplication;
		}
		
		public function set disableApplication( value:Boolean ):void {
			this._disableApplication = value;
		}
		
		private function applicationComplete( evt:Event ):void {
			initConnectionTest();
			this._applicationComplete = true;
			
			if( this._configFinished ) {
				fireComplete();
			}
		}
		
		protected function initConnectionTest():void {
			
			if( ! this._serverRequired ) {
				configFinished();                	
			} else {
				var sandboxType:String = Security.sandboxType;
				if (sandboxType == Security.LOCAL_WITH_FILE) {
					// show dialog to compile with network or set trusted
					var dialog:ConfigInfoDialog = new ConfigInfoDialog();
					dialog.addEventListener(ConfigInfoDialog.COMPLETE,
						function( evt:Event=null ):void {
							var dialog:ConfigInfoDialog = ConfigInfoDialog( evt.target );
							PopUpManager.removePopUp( dialog );
							configFinished();
						}
					);
					PopUpManager.addPopUp(dialog, DisplayObject( FlexGlobals.topLevelApplication ), true);
					PopUpManager.centerPopUp(dialog);
					
				} else {
					// sandboxType ==  Security.LOCAL_WITH_NETWORK  or  Security.LOCAL_TRUSTED  or Security.REMOTE
					// so we have the right to access a network address
					
					var configFileConfig:ConfigStruct = getConfigFileServerConfig();
					
					var configStructs:Array;
					
					if (this._triedDefaults) {
						configStructs = [ configFileConfig ];
					} else {
						if (sandboxType == Security.LOCAL_WITH_NETWORK) { 
							// we may not access relative URLs (e.g. ../../)
							var configFileContextRootIsRelative:Boolean = 
								configFileConfig.contextRoot.indexOf( "http://" ) != 0;
							
							if( configFileContextRootIsRelative ) {
								configStructs = [ this._defaultConfigURL];
							} else {
								configStructs = [ configFileConfig, this._defaultConfigURL];
							}
						} else {
							configStructs = [ configFileConfig, this._defaultConfigURL, this._defaultConfigRelative];
						}
					}
					
					this._triedDefaults = true;
					
					if (isConfigStoredInCache()) {
						configStructs.push(getConfigFromCache());
					}
					
					tryConnect(configStructs, 0, false);
				}
			}
		}
		
		private function getConfigFileServerConfig():ConfigStruct {
			var contextRoot:String = getProperty( CONFIG_FILE_PARAM_CONTEXT_ROOT );
			contextRoot = contextRoot == null ? "" : contextRoot;
			var serviceExtension:String = getProperty( CONFIG_FILE_PARAM_SERVICE_EXTENSION );
			serviceExtension = serviceExtension == null ? "" : serviceExtension;
			
			var configFileConfig:ConfigStruct = new ConfigStruct( contextRoot, serviceExtension );
			return configFileConfig;
		}
		
		private function tryConnect( configStructs:Array, configIndex:int=0, showError:Boolean=true ):void {
			
			var configStruct:ConfigStruct = configStructs[ configIndex ];
			
			var serviceURL:String = configStruct.contextRoot + 
				CONNECTION_TEST_SERVICE +
				configStruct.serviceExtension;
			
			var service:HTTPService = new HTTPService();
			service.url = serviceURL;
			service.method = "POST";
			service.resultFormat = "e4x";
			service.requestTimeout = 5;
			
			service.addEventListener( mx.rpc.events.FaultEvent.FAULT, function( evt:FaultEvent ):void {
				if( debug ) {
					var detail:String = "";
					if( null != evt.fault ) {
						detail = ": "+evt.fault.faultString;
					}
					trace("ConfigTool: Connecting to "+serviceURL+" failed "+detail);
				}
				
				CursorManager.removeBusyCursor();
				if( disableApplication ) {
					FlexGlobals.topLevelApplication.enabled = true;
				}
				if( configIndex < configStructs.length-1 ) {
					tryConnect( configStructs, configIndex+1, showError );
				} else {
					onTestRequestFailed( configStructs, evt, showError );
				}
			});
			
			service.addEventListener( ResultEvent.RESULT, function( evt:ResultEvent ):void {
				
				if( debug ) {
					trace("ConfigTool: Connecting to "+service.url+" succeeded.");
					trace("ConfigTool: Server reply: "+evt.result);
				}
				CursorManager.removeBusyCursor();
				if( disableApplication ) {
					FlexGlobals.topLevelApplication.enabled = true;
				}
				onTestRequestSucceeded( configStructs, configIndex, evt );
			});
			
			CursorManager.setBusyCursor();
			if( disableApplication ) {
				FlexGlobals.topLevelApplication.enabled = false;
			}
			
			if( debug ) {
				trace("ConfigTool: Connecting to "+service.url+" ...");
			}
			service.send();
		}
		
		protected function onTestRequestFailed( configStructs:Array, evt:FaultEvent, showError:Boolean=true ):void {
			
			var dialog:ConfigDialog = new ConfigDialog();
			dialog.autoCorrect = true;
			if( showError ) {
				dialog.errorMessage = evt.fault.faultString;
			}
			var suggestedContextRoot:String = DEFAULT_CONTEXT_ROOT;
			var suggestedServiceExtension:String = _defaultServiceExtension;
			if( configStructs.length > 0 ) {
				var struct:ConfigStruct = configStructs[ 0 ];
				suggestedContextRoot = struct.contextRoot;
				suggestedServiceExtension = struct.serviceExtension;
			}
			dialog.defaultContextRoot = suggestedContextRoot;
			dialog.defaultServiceExtension = suggestedServiceExtension;
			dialog.addEventListener(ConfigDialog.RETRY,onRetry);
			dialog.addEventListener(ConfigDialog.IGNORE, 
				function( evt:Event=null ):void {
					var dialog:ConfigDialog = ConfigDialog( evt.target );
					PopUpManager.removePopUp( dialog );
					configFinished();    	
				} 
			);
			PopUpManager.addPopUp(dialog, DisplayObject( FlexGlobals.topLevelApplication), true);
			PopUpManager.centerPopUp(dialog);
		}
		
		internal function onRetry(evt:Event):void {
			
			var dialog:ConfigDialog = ConfigDialog( evt.target );
			PopUpManager.removePopUp( dialog );
			
			var customContextRoot:String = dialog.customContextRoot;
			var customServiceExtension:String = dialog.customServiceExtension;
			
			var customDemoConfigStruct:ConfigStruct = new ConfigStruct( customContextRoot, customServiceExtension );
			
			tryConnect([ customDemoConfigStruct, getConfigFileServerConfig()]);
			
		}
		
		protected function onTestRequestSucceeded( configStructs:Array, configIndex:int, evt:ResultEvent ):void {
			this._workingConfig = configStructs[ configIndex ];
			HttpServiceFactory.useConfig = false;
			HttpServiceFactory.defaultContextRoot = contextRoot;
			HttpServiceFactory.defaultServiceExtension = serviceExtension;
			
			storeConfigInCache( this._workingConfig );
			
			var result:String = evt.result.toString() as String;
			
			if ("success" == result.toLowerCase()) {
				configFinished();
			} else {
				var dialog:ConfigInfoDialog = new ConfigInfoDialog();
				dialog.errorMessage = result;
				dialog.connectionFailed = false;
				
				dialog.addEventListener(ConfigInfoDialog.COMPLETE,
					function( evt:Event=null ):void {
						var dialog:ConfigInfoDialog = ConfigInfoDialog( evt.target );
						PopUpManager.removePopUp( dialog );
						configFinished();
					}
				);
				PopUpManager.addPopUp(dialog, DisplayObject( FlexGlobals.topLevelApplication ), true);
				PopUpManager.centerPopUp(dialog);
			}
		}
		
		private function configFinished():void {
			this._configFinished = true;
			if( this._applicationComplete ) {
				fireComplete();
			}
		}
		
		private function fireComplete():void {
			dispatchEvent( new Event( Event.COMPLETE ) );
		}
		
		public function get contextRoot():String {
			if( null != this._workingConfig ) {
				return this._workingConfig.contextRoot;
			}
			return null;
		}
		
		public function get serviceExtension():String {
			if( null != this._workingConfig ) {
				return this._workingConfig.serviceExtension;
			}
			return null;
		}
		
		private function getSharedObject():SharedObject { 
			return SharedObject.getLocal(SHARED_OBJECT_KEY);
		}
		
		private function storeConfigInCache( configStruct:ConfigStruct ):void {
			var sharedObject:SharedObject = getSharedObject();
			sharedObject.data.serverConfig = configStruct;
		}
		
		private function isConfigStoredInCache():Boolean {
			return null != getConfigFromCache();
		}
		
		private function getConfigFromCache():ConfigStruct {
			var sharedObject:SharedObject = getSharedObject();
			return sharedObject.data.serverConfig as ConfigStruct;
		}
		
		private function getProperty( key:String ):String {
			return XML( CONFIG_XML.child( key )[0] ).text()[0];
		}
		
	}
}