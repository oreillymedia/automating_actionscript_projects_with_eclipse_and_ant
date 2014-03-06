
			package com.sample
			{
				import flash.display.Sprite;
				import flash.events.ContextMenuEvent;
				import flash.ui.ContextMenu;
				import flash.ui.ContextMenuItem;
				import flash.net.URLRequest;
				import flash.net.navigateToURL;
				import com.sample.utils.ApplicationUtils;
				
				/**
				 * @author sidneydekoning
				 */
				public class Version
				{
					public static var DATE:String = "21-08-2011 22:41";
					public static var PROJECT_NAME:String = "sample";
					public static var BUILT_ON:String = "Mac OS X 10.6.8";
					public static var BUILD_NUMBER:String = "276";
					public static var USER_NAME:String = "sidneydekoning";
					
					public function Version( menuSprite : Sprite) 
					{
						var cm : ContextMenu = new ContextMenu();
						cm.hideBuiltInItems();
						
						switch(ApplicationUtils.getDevelopmentMode()) {
									
							case ApplicationUtils.DEVELOPMENT:
								// Do specific deveopment stuff here
								cm.customItems.push(new ContextMenuItem("Project : " + Version.PROJECT_NAME));
								cm.customItems.push(new ContextMenuItem("Date : " + Version.DATE));
								cm.customItems.push(new ContextMenuItem("Built on : " + Version.BUILT_ON));
								cm.customItems.push(new ContextMenuItem("Built by : " + Version.USER_NAME));
								cm.customItems.push(new ContextMenuItem("Built # : " + Version.BUILD_NUMBER));
								
								break;
							case ApplicationUtils.PRODUCTION:
								// Do specific production stuff here
								cm.customItems.push(new ContextMenuItem("Project : " + Version.PROJECT_NAME));
								cm.customItems.push(new ContextMenuItem("Date : " + Version.DATE));
								cm.customItems.push(new ContextMenuItem("Built on : " + Version.BUILT_ON));
								cm.customItems.push(new ContextMenuItem("Built # : " + Version.BUILD_NUMBER));
								break;
						}
			
						var notice : ContextMenuItem = new ContextMenuItem( "My Company Name, Year" );
						notice.enabled = true;
						notice.separatorBefore = true;
						notice.addEventListener( ContextMenuEvent.MENU_ITEM_SELECT, menuItemSelect );
						cm.customItems.push( notice );
			
						menuSprite.contextMenu = cm;
					}
						
					private function menuItemSelect(evt : ContextMenuEvent) : void {
						navigateToURL(new URLRequest("http://www.justfunky.com/"));
					}
				}
			}
		