package nukeduck.crawler.event.input;

import nukeduck.crawler.util.Vec2;

public abstract class MouseEvent {
	public Vec2 pos;

	public MouseEvent(Vec2 pos) {
		this.pos = pos;
	}

	public static class MouseMoveEvent extends MouseEvent {
		public Vec2 lastPos;

		public MouseMoveEvent(Vec2 lastPos, Vec2 pos) {
			super(pos);
			this.lastPos = lastPos;
		}
	}

	public static abstract class MouseButtonEvent extends MouseEvent {
		public MouseButtonEvent(Vec2 pos, MouseButton button) {
			super(pos);
			this.button = button;
		}

		public MouseButton button;
	}

	public static class MouseDownEvent extends MouseButtonEvent {
		public MouseDownEvent(Vec2 pos, MouseButton button) {
			super(pos, button);
		}
	}
	public static class MouseUpEvent extends MouseButtonEvent {
		public MouseUpEvent(Vec2 pos, MouseButton button) {
			super(pos, button);
		}
	}

	public static class ClickEvent extends MouseButtonEvent {
		public Vec2 startPos;

		public ClickEvent(Vec2 startPos, Vec2 pos, MouseButton button) {
			super(pos, button);
			this.startPos = startPos;
		}

		public double getDistanceSquared() {
			return this.pos.getDistanceSquared(this.startPos);
		}
	}

	public enum MouseButton {
		LEFT,
		RIGHT,
		WHEEL,
		FOUR,
		FIVE,
		SIX,
		SEVEN,
		EIGHT;

		public int getId() {
			return this.ordinal();
		}
		public static MouseButton get(int id) {
			MouseButton[] values = values();
			if(id < 0 || id >= values.length) return null;
			return values[id];
		}
	}
}
