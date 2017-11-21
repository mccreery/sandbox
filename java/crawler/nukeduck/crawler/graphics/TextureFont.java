package nukeduck.crawler.graphics;

import java.nio.ByteBuffer;

import nukeduck.crawler.graphics.text.ITextFormatter;
import nukeduck.crawler.util.ResourcePath;

public class TextureFont extends Texture {
	public TextureFont(ResourcePath resource) {
		super(resource);
	}

	protected int[] charWidth;

	@Override
	public void processImage(ByteBuffer buffer, int width, int height) {
		this.charWidth = new int[256];
		int stepX = width / 16;
		int stepY = height / 16;
		float ratioX = (float) width / 128.0F;

		int x = 0, y = 0;
		for(int n = 0; n < 256; n++, x += stepX) {
			if(x == width) {
				y += stepY;
				x = 0;
			}

			int i;
			for(i = 0; i < stepX; i++) {
				boolean flag = false;
				for(int j = 0; j < stepY; j++) {
					if(buffer.get(((x + i) + (y + j) * width) * 4 + 3) != (byte) 0) {
						flag = true;
					}
				}
				if(!flag) break;
			}
			this.charWidth[n] = (int) (i / ratioX);
		}
		this.charWidth[' '] = 4;
	}

	public int getCharWidth(int c) {
		return this.getCharWidth((char) c);
	}
	public int getCharWidth(char c) {
		return this.charWidth[c];
	}
}
