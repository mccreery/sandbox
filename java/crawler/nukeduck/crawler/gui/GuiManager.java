package nukeduck.crawler.gui;

import nukeduck.crawler.util.Vec2;

public class GuiManager {
	private int pos = 0;

	private int size;
	private final IGuiLayer[] stack = new IGuiLayer[size];

	public GuiManager(int size) {
		this.size = size;
	}

	public GuiManager push(IGuiLayer layer) {
		if(pos < size) stack[pos++] = layer;
		return this;
	}

	public GuiManager pop() {
		if(pos > 0) stack[--pos] = null;
		return this;
	}

	public void render(Vec2 size) {
		for(int i = 0; i < pos; i++) {
			stack[i].render(size);
		}
	}
}
