package nukeduck.crawler.graphics;

import java.util.ArrayList;
import java.util.List;

import nukeduck.crawler.graphics.text.ITextFormatter;
import nukeduck.crawler.graphics.text.ITextFormatter.TextFormat;
import nukeduck.crawler.util.Vec2;

import org.lwjgl.opengl.GL11;

public class RenderUtil {
	public static final float TILE_SIZE = 1.0F / 16.0F;
	public static final int TEXT_HEIGHT = 8;

	public static final double SHEAR_ANGLE = Math.toRadians(22.5);
	public static final float TEXT_SHEAR = (float) Math.tan(SHEAR_ANGLE) * TEXT_HEIGHT;

	public static void drawString(TextureFont texture, Vec2 pos, CharSequence text, ITextFormatter formatter) {
		GL11.glPushAttrib(GL11.GL_ENABLE_BIT);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getTextureId());

		boolean bold = false, italic = false, underline = false;

		List<int[]> underlineRanges = new ArrayList<int[]>();
		int underlineStart = 0;

		GL11.glBegin(GL11.GL_QUADS);
		for(int i = 0; i < text.length(); i++) {
			if(!formatter.include(text, i)) continue;

			TextFormat format = new TextFormat();
			if(format.unpack(formatter.format(text, i))) {
				if(format.alterColor) format.getColor().applyRGBA();
				if(format.alterWeight) bold = format.weight;
				if(format.alterItalic) italic = format.italic;
				if(format.alterUnderline) {
					boolean newUnderline = format.underline;
					if(newUnderline) {
						if(!underline) underlineStart = (int) pos.x;
					} else {
						if(underline) underlineRanges.add(new int[] {underlineStart, (int) pos.x});
					}
					underline = newUnderline;
				}
			}

			int c = text.charAt(i);
			if(c > 255) c = '?';

			float u = (c % 16) * TILE_SIZE;
			float v = (c / 16) * TILE_SIZE;

			int charWidth = texture.getCharWidth(c);
			int spacedWidth = charWidth + formatter.getSpacing();

			float weight = bold ? 1.5F : 1.0F;
			weight *= TEXT_HEIGHT;
			float shear = italic ? TEXT_SHEAR : 0.0F;

			GL11.glTexCoord2f(u, v);
			GL11.glVertex2f(pos.x + shear, pos.y);
			GL11.glTexCoord2f(u + TILE_SIZE, v);
			GL11.glVertex2f(pos.x + weight + shear, pos.y);
			GL11.glTexCoord2f(u + TILE_SIZE, v + TILE_SIZE);
			GL11.glVertex2f(pos.x + weight, pos.y + TEXT_HEIGHT);
			GL11.glTexCoord2f(u, v + TILE_SIZE);
			GL11.glVertex2f(pos.x, pos.y + TEXT_HEIGHT);

			if(underline) {
				c = '\u007F';
				u = (c % 16) * TILE_SIZE;
				v = (c / 16) * TILE_SIZE;

				GL11.glTexCoord2f(u, v);
				GL11.glVertex2f(pos.x, pos.y + 1);
				GL11.glTexCoord2f(u + TILE_SIZE, v);
				GL11.glVertex2f(pos.x + spacedWidth, pos.y + 1);
				GL11.glTexCoord2f(u + TILE_SIZE, v + TILE_SIZE);
				GL11.glVertex2f(pos.x + spacedWidth, pos.y + TEXT_HEIGHT + 1);
				GL11.glTexCoord2f(u, v + TILE_SIZE);
				GL11.glVertex2f(pos.x, pos.y + TEXT_HEIGHT + 1);
			}

			pos.x += spacedWidth;
		}
		GL11.glEnd();
		GL11.glPopAttrib();
	}
}
