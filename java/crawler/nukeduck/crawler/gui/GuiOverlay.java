package nukeduck.crawler.gui;

import org.lwjgl.opengl.GL11;

import nukeduck.crawler.Crawler;
import nukeduck.crawler.util.FuncsUtil;
import nukeduck.crawler.util.Vec2;

public class GuiOverlay extends IGuiLayer {
	public GuiOverlay() {
		super(1);
	}

	@Override
	public void render(Vec2 size) {
		//GL11.glTranslatef(5, 5, 0);
		//Crawler.INSTANCE.font.bind();
		//Crawler.INSTANCE.textCache.get(Crawler.INSTANCE.player.getHealth() + "/" + Crawler.INSTANCE.player.getMaxHealth()).render();

		//GL11.glTranslatef(size.x - 10 - FuncsUtil.getWidth(Crawler.INSTANCE.font, "test"), 0, 0);
		//Crawler.INSTANCE.textCache.get("test").render();
	}
}
