package nukeduck.crawler.map;

import nukeduck.crawler.graphics.Color;
import nukeduck.crawler.graphics.VBO.IVertices;
import nukeduck.crawler.graphics.VBO.VertexData;
import nukeduck.crawler.util.Vec2;

import org.lwjgl.opengl.GL11;

public class Tile implements IVertices {
	public static final Tile[] tiles = new Tile[256];

	public static final Tile air = new Tile(0) {
		@Override
		public int getVertexCount() {
			return 0;
		}

		@Override
		public boolean collide() {
			return false;
		}
	}.setTextureIndex(255);
	public static final Tile redBrick = new Tile(1).setTextureIndex(0);

	public boolean collide() {
		return true;
	}

	public int getVertexCount() {
		return 6;
	}

	public VertexData getVertex(int i) {
		float u = (float) (this.getTextureIndex() % 16) * 0.0625F;
		float v = (float) (this.getTextureIndex() / 16) * 0.0625F;

		switch(i) {
			default:
			case 0:
			case 3:
				return new VertexData(new Vec2(0, 0), u, v, Color.WHITE);
			case 1:
				return new VertexData(new Vec2(16, 0), u + 0.0625F, v, Color.WHITE);
			case 2:
			case 4:
				return new VertexData(new Vec2(16, 16), u + 0.0625F, v + 0.0625F, Color.WHITE);
			case 5:
				return new VertexData(new Vec2(0, 16), u, v + 0.0625F, Color.WHITE);
		}
	}

	private int id;
	private int textureIndex = 0;

	public static Tile fromId(int id) {
		return tiles[id];
	}

	public Tile(int id) {
		this.id = id;
		tiles[this.id] = this;
	}

	public Tile setTextureIndex(int index) {
		this.textureIndex = index;
		return this;
	}

	public int getId() {
		return this.id;
	}

	public void render(Vec2 pos) {
		int i = this.getTextureIndex();
		float u = (i % 16) * 0.0625F;
		float v = (i / 16) * 0.0625F;

		GL11.glTexCoord2f(u, v);
		GL11.glVertex2f(pos.x, pos.y);
		GL11.glTexCoord2f(u + 0.0625F, v);
		GL11.glVertex2f(pos.x + 16, pos.y);
		GL11.glTexCoord2f(u + 0.0625F, v + 0.0625F);
		GL11.glVertex2f(pos.x + 16, pos.y + 16);
		GL11.glTexCoord2f(u, v + 0.0625F);
		GL11.glVertex2f(pos.x, pos.y + 16);
	}

	public int getTextureIndex() {
		return this.textureIndex;
	}
}
