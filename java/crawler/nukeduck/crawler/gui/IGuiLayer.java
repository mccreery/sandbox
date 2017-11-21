package nukeduck.crawler.gui;

import nukeduck.crawler.event.input.KeyEvent;
import nukeduck.crawler.util.Vec2;

import org.lwjgl.opengl.GL11;

public abstract class IGuiLayer {
	private int pos = 0;

	private int size;
	private IGuiLayer[] children;

	public IGuiLayer(int size) {
		this.size = size;
		this.children = new IGuiLayer[size];
	}

	public IGuiLayer push(IGuiLayer layer) {
		if(pos < size) children[pos++] = layer;
		return this;
	}

	public IGuiLayer pop() {
		if(pos > 0) children[--pos] = null;
		return this;
	}

	/** @return {@code true} to break the key event tree. */
	public boolean onKeyEvent(KeyEvent event) {
		return false;
	}

	public boolean onKeyEventTree(KeyEvent event) {
		for(int i = 0; i < pos; i++) {
			if(this.children[i].onKeyEventTree(event)) return true;
		}
		return this.onKeyEvent(event);
	}

	/** Render the layer and its children.
	 * @param size The size, in pixels, of the screen */
	public void renderTree(Vec2 size) {
		GL11.glPushMatrix();
		this.render(size);
		GL11.glPopMatrix();

		for(int i = 0; i < pos; i++) {
			GL11.glPushMatrix();
			this.children[i].renderTree(size);
			GL11.glPopMatrix();
		}
	}

	/** Render the layer.
	 * @param size The size, in pixels, of the screen */
	public abstract void render(Vec2 size);
}
