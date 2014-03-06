package com.sample.utils {
	import flash.display.DisplayObject;
	import flash.display.LoaderInfo;
	import flash.display.Sprite;

	/**
	 * @author Sidney de Koning
	 * 
	 * Example:
	 * If you are testing your SWF file, you can find out if you are testing online of locally. And behave accordingly.
	 * 
	 * The following example will set an XML file location by either getting the FlashVar or manually load the xml file.  
	 * var xmlURL:String = ( ApplicationUtils.getDevelopmentMode( ) == ApplicationUtils.PRODUCTION ) ? ApplicationUtils.getFlashVars( ).xml : "./xml/data.xml";
	 * 
	 * 
	 */
	public class ApplicationUtils extends Sprite {

		public static const DEVELOPMENT : String = "Development";
		public static const PRODUCTION : String  = "Production";
		//
		private static var _docRoot : DisplayObject;
		private static var _topParent : DisplayObject;
		
		
		public static function init( docRoot : DisplayObject) : void {
			
			_docRoot = docRoot;
		}

		public static  function getFlashVars() : Object {
			return Object( getLoaderInfo(_docRoot).parameters );
		}

		public static  function getDevelopmentMode() : String {
			//TODO: Is regex good here?
			var dev : Boolean = new RegExp( "file://" ).test( _docRoot.loaderInfo.loaderURL );

			if( dev ) {
				return ApplicationUtils.DEVELOPMENT;
			} else {
				return ApplicationUtils.PRODUCTION;
			}
		}

		public static  function getContextPath() : String {
			var uri : String = getLoaderURL( );
			return uri.substring( 0, uri.lastIndexOf( "/" ) ) + "/";
		}

		public static function getLoaderURL() : String {
			return _docRoot.loaderInfo.loaderURL;
		}

		public static function getLoaderInfo(dispObj : DisplayObject) : LoaderInfo {
			//todo: just testing my todo items
			var root : DisplayObject = getRootDisplayObject( dispObj );
			if (root != null) {
				return root.loaderInfo;
			}
			return null;
		}

		public static function getRootDisplayObject(dispObj : DisplayObject) : DisplayObject {
			if (_topParent == null) {
				if (dispObj.parent != null) {
					return getRootDisplayObject( dispObj.parent );
				} else {
					_topParent = dispObj;
					return _topParent;
				}
			} else {
				return _topParent;
			}
		}
	}
}