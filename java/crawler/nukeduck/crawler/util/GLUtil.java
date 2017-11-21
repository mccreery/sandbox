package nukeduck.crawler.util;

import nukeduck.crawler.graphics.WindowHandle;

import org.lwjgl.opengl.GL11;

public class GLUtil {
	public static final void setup2D(WindowHandle handle) {
		Vec2 size = handle.getSize();

		GL11.glViewport(0, 0, (int) size.x, (int) size.y);
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, size.x, size.y, 0, 0, 1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
	}
}
