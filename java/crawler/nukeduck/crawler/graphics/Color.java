package nukeduck.crawler.graphics;

import org.lwjgl.opengl.GL11;

public class Color {
	public static final Color WHITE = new Color(255, 255, 255);
	public static final Color BLACK = new Color(0, 0, 0);
	public static final Color RED = new Color(255, 0, 0);
	public static final Color GREEN = new Color(0, 255, 0);
	public static final Color BLUE = new Color(0, 0, 255);

	public int r, g, b, a;

	public Color(int r, int g, int b) {
		this(r, g, b, 0xFF);
	}
	public Color(int r, int g, int b, int a) {
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
	}

	public int pack() {
		return (r & 0xFF) << 24 | (g & 0xFF) << 16 | (b & 0xFF) << 8 | (a & 0xFF);
	}
	public static Color unpack(int rgba) {
		return new Color(
			(rgba >> 24) & 0xFF,
			(rgba >> 16) & 0xFF,
			(rgba >> 8) & 0xFF,
			rgba & 0xFF
		);
	}

	public void applyRGBA() {
		GL11.glColor4f(
			(float) this.r / 255.0F,
			(float) this.g / 255.0F,
			(float) this.b / 255.0F,
			(float) this.a / 255.0F
		);
	}
	public void applyRGB() {
		GL11.glColor3f(
			(float) this.r / 255.0F,
			(float) this.g / 255.0F,
			(float) this.b / 255.0F
		);
	}

	@Override
	public String toString() {
		return "{" + this.r + ", " + this.g + ", " + this.b + ", " + this.a + "}";
	}
}
