package nukeduck.crawler.event.input;

public interface IMouseListener<T extends MouseEvent> {
	/** Handle the event.
	 * @return {@code true} if the event should be cancelled. */
	public boolean handleEvent(T event);
}
