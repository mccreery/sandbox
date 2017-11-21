package nukeduck.crawler.event.input;

import nukeduck.crawler.event.input.KeyEvent.KeyAction;

public abstract class IKeyListener {
	private KeyAction[] acceptedActions;

	public IKeyListener(KeyAction... acceptedActions) {
		this.acceptedActions = acceptedActions;
	}

	/** Handle the event.
	 * @return {@code true} if the event should be cancelled. */
	public abstract boolean handleEvent(KeyEvent event);

	public boolean canHandle(KeyEvent event) {
		for(KeyAction action : this.acceptedActions) {
			if(action == event.action) {
				return true;
			}
		}
		return false;
	}
}
