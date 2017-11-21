package nukeduck.crawler.event.input;

import java.util.ArrayList;
import java.util.List;

public class KeyEvents {
	public static final KeyEvents INSTANCE = new KeyEvents();

	private List<IKeyListener> listeners = new ArrayList<IKeyListener>();

	public void post(KeyEvent event) {
		for(IKeyListener listener : this.listeners) {
			if(listener.canHandle(event)) listener.handleEvent(event);
		}
	}

	public void register(IKeyListener listener) {
		this.listeners.add(listener);
	}
}
