package nukeduck.crawler.util;

import java.util.ArrayList;
import java.util.List;

import nukeduck.crawler.graphics.TextureFont;
import nukeduck.crawler.graphics.text.ITextFormatter;

public class FuncsUtil {
	public static final List<CharSequence> split(TextureFont font, CharSequence text, int width) {
		List<CharSequence> list = new ArrayList<CharSequence>();
		String s = text.toString();

		int lineStart = 0, lineWidth = 0;
		int prevSpace = 0, currSpace = 0;

		while((currSpace = s.indexOf(' ', prevSpace + 1)) != -1) {
			int a = getWidth(font, text.subSequence(prevSpace, currSpace));

			lineWidth += a;
			if(lineWidth > width) {
				list.add(text.subSequence(lineStart, prevSpace));

				lineStart = prevSpace + 1;
				lineWidth = a - font.getCharWidth(' ');
			}
			prevSpace = currSpace;
		}
		list.add(text.subSequence(lineStart, text.length()));

		return list;
	}

	public static final int getWidth(TextureFont font, CharSequence text) {
		int width = 0;
		for(int i = 0; i < text.length(); i++) {
			width += font.getCharWidth(text.charAt(i)) + ITextFormatter.DEFAULT_SPACING;
		}
		return width;
	}
}
