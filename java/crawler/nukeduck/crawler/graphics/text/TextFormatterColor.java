package nukeduck.crawler.graphics.text;

import nukeduck.crawler.graphics.Color;

public class TextFormatterColor extends ITextFormatter {
	private byte color;
	private byte format;

	public TextFormatterColor(byte color) {
		this(NO_FORMATTING, color);
	}
	public TextFormatterColor(byte format, byte color) {
		this.color = color;
	}

	public Color getColor() {
		return getColor(this.color);
	}
	public TextFormatterColor setColor(byte color) {
		this.color = color;
		return this;
	}

	@Override
	public boolean include(CharSequence text, int i) {
		return true;
	}

	@Override
	public byte format(CharSequence text, int i) {
		return (byte) (this.format | (this.color & COLOR_MASK));
	}

	@Override
	public int getSpacing() {
		return DEFAULT_SPACING;
	}
}
