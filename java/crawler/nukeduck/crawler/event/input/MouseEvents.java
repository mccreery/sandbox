package nukeduck.crawler.event.input;

import java.util.ArrayList;
import java.util.List;

import nukeduck.crawler.event.input.MouseEvent.ClickEvent;
import nukeduck.crawler.event.input.MouseEvent.MouseDownEvent;
import nukeduck.crawler.event.input.MouseEvent.MouseMoveEvent;
import nukeduck.crawler.event.input.MouseEvent.MouseUpEvent;

public class MouseEvents {
	public static final MouseEvents INSTANCE = new MouseEvents();

	private List<IMouseListener<MouseMoveEvent>> moveListeners = new ArrayList<IMouseListener<MouseMoveEvent>>();
	private List<IMouseListener<MouseDownEvent>> downListeners = new ArrayList<IMouseListener<MouseDownEvent>>();
	private List<IMouseListener<MouseUpEvent>> upListeners = new ArrayList<IMouseListener<MouseUpEvent>>();
	private List<IMouseListener<ClickEvent>> clickListeners = new ArrayList<IMouseListener<ClickEvent>>();

	public void post(MouseEvent event) {
		if(event.getClass().equals(MouseMoveEvent.class)) {
			MouseMoveEvent e = (MouseMoveEvent) event;
			for(IMouseListener<MouseMoveEvent> listener : this.moveListeners) {
				listener.handleEvent(e);
			}
		} else if(event.getClass().equals(MouseDownEvent.class)) {
			MouseDownEvent e = (MouseDownEvent) event;
			for(IMouseListener<MouseDownEvent> listener : this.downListeners) {
				listener.handleEvent(e);
			}
		} else if(event.getClass().equals(MouseUpEvent.class)) {
			MouseUpEvent e = (MouseUpEvent) event;
			for(IMouseListener<MouseUpEvent> listener : this.upListeners) {
				listener.handleEvent(e);
			}
		} else if(event.getClass().equals(ClickEvent.class)) {
			ClickEvent e = (ClickEvent) event;
			for(IMouseListener<ClickEvent> listener : this.clickListeners) {
				listener.handleEvent(e);
			}
		}
	}

	public void registerMove(IMouseListener<MouseMoveEvent> listener) {
		this.moveListeners.add(listener);
	}
	public void registerDown(IMouseListener<MouseDownEvent> listener) {
		this.downListeners.add(listener);
	}
	public void registerUp(IMouseListener<MouseUpEvent> listener) {
		this.upListeners.add(listener);
	}
	public void registerClick(IMouseListener<ClickEvent> listener) {
		this.clickListeners.add(listener);
	}
}
