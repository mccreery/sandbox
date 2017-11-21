package nukeduck.crawler.gui;

import org.lwjgl.opengl.GL11;

import nukeduck.crawler.Crawler;
import nukeduck.crawler.util.Keys;
import nukeduck.crawler.util.Vec2;

public class GuiInputs extends IGuiLayer {
	public GuiInputs() {
		super(0);
	}

	@Override
	public void render(Vec2 size) {
		GL11.glEnable(GL11.GL_BLEND);
		Crawler.INSTANCE.font.bind();
		GL11.glColor4f(1, 1, 1, Keys.W.state ? 1.0F : 0.5F);
			GL11.glTranslatef(8, 0, 0);
			Crawler.INSTANCE.textCache.get("W").render();
			GL11.glTranslatef(-8, 0, 0);
		GL11.glColor4f(1, 1, 1, Keys.S.state ? 1.0F : 0.5F);
			GL11.glTranslatef(8, 8, 0);
			Crawler.INSTANCE.textCache.get("S").render();
			GL11.glTranslatef(-8, -8, 0);
		GL11.glColor4f(1, 1, 1, Keys.A.state ? 1.0F : 0.5F);
			GL11.glTranslatef(0, 8, 0);
			Crawler.INSTANCE.textCache.get("A").render();
			GL11.glTranslatef(0, -8, 0);
		GL11.glColor4f(1, 1, 1, Keys.D.state ? 1.0F : 0.5F);
			GL11.glTranslatef(16, 8, 0);
			Crawler.INSTANCE.textCache.get("D").render();
			GL11.glTranslatef(-16, -8, 0);
	}
}
