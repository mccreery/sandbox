package nukeduck.crawler.event;

import nukeduck.crawler.Crawler;
import nukeduck.crawler.event.input.IKeyListener;
import nukeduck.crawler.event.input.KeyEvent;
import nukeduck.crawler.event.input.KeyEvents;
import nukeduck.crawler.event.input.KeyEvent.KeyAction;

public class Events {
	public static final IKeyListener gui = new IKeyListener(KeyAction.PRESS, KeyAction.RELEASE, KeyAction.REPEAT) {
		@Override
		public boolean handleEvent(KeyEvent event) {
			if(Crawler.INSTANCE.baseGui != null) {
				Crawler.INSTANCE.baseGui.onKeyEventTree(event);
			}
			return false;
		}
	};

	public static final void init() {
		KeyEvents.INSTANCE.register(gui);
	}
}
