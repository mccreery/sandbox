package nukeduck.crawler.graphics;

import nukeduck.crawler.graphics.VBO.IFaces;
import nukeduck.crawler.graphics.VBO.VertexData;
import nukeduck.crawler.graphics.text.ITextFormatter;
import nukeduck.crawler.util.Vec2;

public class TextMesh extends IFaces {
	private TextureFont font;
	private CharSequence text;
	private int curWidth = 0;

	public TextMesh(TextureFont font, CharSequence text) {
		super(3);
		this.font = font;
		this.text = text;
	}

	@Override
	public VertexData[] getFace(int i) {
		int width = this.curWidth;
		boolean flag = i % 2 == 1;

		char c = this.text.charAt(i / 2);
		if(flag) this.curWidth += this.font.getCharWidth(c) + ITextFormatter.DEFAULT_SPACING;

		float u = (c % 16) * RenderUtil.TILE_SIZE;
		float v = (int) (c / 16) * RenderUtil.TILE_SIZE;

		if(flag) {
			return new VertexData[] {
				new VertexData(new Vec2(width, 0), u, v, Color.WHITE),
				new VertexData(new Vec2(width + RenderUtil.TEXT_HEIGHT, RenderUtil.TEXT_HEIGHT), u + RenderUtil.TILE_SIZE, v + RenderUtil.TILE_SIZE, Color.WHITE),
				new VertexData(new Vec2(width, RenderUtil.TEXT_HEIGHT), u, v + RenderUtil.TILE_SIZE, Color.WHITE)
			};
		} else {
			return new VertexData[] {
				new VertexData(new Vec2(width, 0), u, v, Color.WHITE),
				new VertexData(new Vec2(width + RenderUtil.TEXT_HEIGHT, 0), u + RenderUtil.TILE_SIZE, v, Color.WHITE),
				new VertexData(new Vec2(width + RenderUtil.TEXT_HEIGHT, RenderUtil.TEXT_HEIGHT), u + RenderUtil.TILE_SIZE, v + RenderUtil.TILE_SIZE, Color.WHITE)
			};
		}
	}

	@Override
	public int getFaceCount() {
		return this.text.length() * 2;
	}
}
