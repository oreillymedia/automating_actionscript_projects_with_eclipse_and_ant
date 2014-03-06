package com.sample {
	import com.sample.utils.ApplicationUtils;

	import com.sample.Version;

	import flash.display.Sprite;
	import flash.display.StageAlign;
	import flash.display.StageQuality;
	import flash.display.StageScaleMode;
	import flash.events.Event;
	
	[SWF(backgroundColor="#000000", frameRate="31", width="200", height="200")]
	
	public class Main extends Sprite {
		public function Main() {
			
			addEventListener(Event.ADDED_TO_STAGE, handleAddedToStage);
		}

		private function start() : void {
			
			var s : Sprite = new Sprite();
			s.graphics.beginFill(0xff00aa, 1);
			s.graphics.drawRect(50, 50, 100, 100);
			s.graphics.endFill();
			addChild(s);
		}

		private function handleAddedToStage(event : Event) : void {
			
			stage.scaleMode = StageScaleMode.NO_SCALE;
			stage.align = StageAlign.TOP_LEFT;
			stage.quality = StageQuality.BEST;
			stage.stageFocusRect = false;
			
			init();
			start();
		}

		private function init() : void {
			//TODO: Init App utils for loader objects and flashvars
			ApplicationUtils.init(stage);
			
			//TODO: Add contextMenu with information
			var versionInfo : Version = new Version(this);
		}
	}
}