package nukeduck.crawler.graphics.text;

import nukeduck.crawler.graphics.Color;

public abstract class ITextFormatter {
	public static byte RED, GREEN, BLUE;

	private static final Color[] colors = new Color[64];
	static {
		colors[RED = (byte) 0] = Color.RED;
		colors[GREEN = (byte) 1] = Color.GREEN;
		colors[BLUE = (byte) 2] = Color.BLUE;
	}

	public static final Color getColor(int i) {
		return colors[i];
	}

	public abstract boolean include(CharSequence text, int i);
	/** @return A packed {@code byte} with the following structure (big-endian):
	 * <table border=1>
	 * 	<tr><th>Data length</th><th colspan=4>Description</th>
	 * 	<tr><td rowspan=3>2 bits</td><th colspan=4>Flags</th></tr>
	 * 	<tr><td>1 bit</td><td colspan=3>Format flag - <b>Always set</b></td></tr>
	 * 	<tr><td>1 bit</td><td colspan=3>Color flag - 1 for color, 0 for format</td></tr>
	 * 	<tr><td rowspan=4>6 bits</td><th colspan=2>Color</th><th colspan=2>Format</th></tr>
	 * 	<tr><td rowspan=3>6 bits</td><td rowspan=3>A color from {@link ITextFormatter}'s list</td><td>2 bits</td><td>Weight - 1 bit for alter, 1 bit for enable</td></tr>
	 * 	<tr><td>2 bits</td><td>Italic - 1 bit for alter, 1 bit for enable</td></tr>
	 * 	<tr><td>2 bits</td><td>Underline - 1 bit for alter, 1 bit for enable</td></tr>
	 * </table>
	 * If a blank format code is sent (i.e. {@code 0x80} or {@code 0b10000000}) then the next character is considered UTF-8 encoded. */
	public abstract byte format(CharSequence text, int i);
	/** @return The default spacing between letters, before modifying */
	public abstract int getSpacing();

	public static final long getSpacing(short value) {
		return (value & 0xFFFFL) << 32;
	}

	public static final byte UTF_8 = (byte) 0x80;
	public static final byte FORMAT = (byte) 0x80;

	public static final byte COLOR = 0x40;
	public static final byte COLOR_MASK = 0x3F;

	public static final byte ALTER_WEIGHT = 0x20;
	public static final byte WEIGHT = 0x10;
	public static final byte ALTER_ITALIC = 0x8;
	public static final byte ITALIC = 0x4;
	public static final byte ALTER_UNDERLINE = 0x2;
	public static final byte UNDERLINE = 0x1;

	public static final byte NO_FORMATTING = 0x0;

	public static class TextFormat {
		public boolean alterColor;
		public byte color;

		public boolean alterWeight, weight;
		public boolean alterItalic, italic;
		public boolean alterUnderline, underline;

		public boolean isUTF8() {
			return !(this.alterColor || this.alterWeight || this.alterItalic || this.alterUnderline);
		}
		public Color getColor() {
			return colors[this.color];
		}

		public byte pack() {
			byte packed = FORMAT;
			if(this.alterColor) {
				packed |= COLOR;
				packed |= this.color & COLOR_MASK;
			} else {
				if(this.alterWeight) {
					packed |= ALTER_WEIGHT;
					if(this.weight) packed |= WEIGHT;
				}
				if(this.alterItalic) {
					packed |= ALTER_ITALIC;
					if(this.italic) packed |= ITALIC;
				}
				if(this.alterUnderline) {
					packed |= ALTER_UNDERLINE;
					if(this.underline) packed |= UNDERLINE;
				}
			}
			return packed;
		}

		public boolean unpack(byte packed) {
			if((packed & FORMAT) != FORMAT) return false;

			if(this.alterColor = (packed & COLOR) == COLOR) {
				this.color = (byte) (packed & COLOR_MASK);
			}
			if(this.alterItalic = (packed & ALTER_ITALIC) == ALTER_ITALIC) {
				this.italic = (packed & ITALIC) == ITALIC;
			}
			if(this.alterUnderline = (packed & ALTER_UNDERLINE) == ALTER_UNDERLINE) {
				this.underline = (packed & UNDERLINE) == UNDERLINE;
			}
			return true;
		}
	}

	public static final int DEFAULT_SPACING = 1;
}
