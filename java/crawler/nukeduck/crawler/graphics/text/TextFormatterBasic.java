package nukeduck.crawler.graphics.text;

public class TextFormatterBasic extends ITextFormatter {
	public static final TextFormatterBasic INSTANCE = new TextFormatterBasic();
	private byte format;

	public TextFormatterBasic() {
		this(NO_FORMATTING);
	}
	public TextFormatterBasic(byte format) {
		this.format = format;
	}

	@Override
	public boolean include(CharSequence text, int i) {
		return true;
	}

	@Override
	public byte format(CharSequence text, int i) {
		return this.format;
	}

	@Override
	public int getSpacing() {
		return DEFAULT_SPACING;
	}
}
