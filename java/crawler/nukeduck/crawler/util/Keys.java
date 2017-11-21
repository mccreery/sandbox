package nukeduck.crawler.util;

public enum Keys {
	UNKNOWN(0),
	SPACE(32),
	APOSTROPHE(39),
	COMMA(44),
	MINUS(45),
	PERIOD(46),
	SLASH(47),
	ZERO(48), ONE(49), TWO(50), THREE(51), FOUR(52),
	FIVE(53), SIX(54), SEVEN(55), EIGHT(56), NINE(57),
	SEMICOLON(59), EQUALS(61),
	A(65), B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z,
	OPEN_BRACKET(91), BACKSLASH(92), CLOSE_BRACKET(93), BACKTICK(96), ESCAPE(256), ENTER(257), TAB(258), BACKSPACE(259), INSERT(260), DELETE(261),
	RIGHT(262), LEFT, DOWN, UP,
	PAGE_UP(266), PAGE_DOWN, HOME(268), END,
	CAPS_LOCK(280), SCROLL_LOCK, NUM_LOCK,
	PRINT_SCREEN(283),
	PAUSE(284),
	F1(290), F2, F3, F4, F5, F6, F7, F8, F9, F10, F11, F12, F13, F14, F15, F16, F17, F18, F19, F20, F21, F22, F23, F24, F25,
	NUMPAD_0(320), NUMPAD_1, NUMPAD_2, NUMPAD_3, NUMPAD_4, NUMPAD_5, NUMPAD_6, NUMPAD_7, NUMPAD_8, NUMPAD_9,
	NUMPAD_DECIMAL(330), NUMPAD_DIVIDE, NUMPAD_MULTIPLY, NUMPAD_SUBTRACT, NUMPAD_ADD, NUMPAD_ENTER, NUMPAD_EQUALS,
	LEFT_SHIFT(340), LEFT_CONTROL, LEFT_ALT, LEFT_SUPER, RIGHT_SHIFT, RIGHT_CONTROL, RIGHT_ALT, RIGHT_SUPER,
	MENU(348);

	private int id;
	public boolean state;

	private Keys() {
		this(Counter.lastId + 1);
	}
	private Keys(int id) {
		Counter.lastId = this.id = id;
	}
	public int getId() {
		return this.id;
	}

	public static final Keys[] mapping = new Keys[512];
	static {
		for(Keys key : values()) {
			mapping[key.getId()] = key;
		}
	}

	public static Keys fromId(int id) {
		if(id < 0 || id >= mapping.length) return UNKNOWN;
		return mapping[id];
	}

	private static class Counter {
		private static int lastId;
	}
}
