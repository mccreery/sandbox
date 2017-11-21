package nukeduck.crawler.event.input;

import org.lwjgl.glfw.GLFW;

import nukeduck.crawler.util.Keys;

public class KeyEvent {
	public KeyAction action;
	public Keys key;

	public KeyEvent(Keys button, KeyAction action) {
		this.key = button;
		this.action = action;
	}

	public enum KeyAction {
		UNKNOWN,
		PRESS,
		RELEASE,
		REPEAT;

		public static KeyAction fromId(int id) {
			switch(id) {
				case GLFW.GLFW_PRESS:
					return PRESS;
				case GLFW.GLFW_RELEASE:
					return RELEASE;
				case GLFW.GLFW_REPEAT:
					return REPEAT;
				default:
					return UNKNOWN;
			}
		}
	}
}
