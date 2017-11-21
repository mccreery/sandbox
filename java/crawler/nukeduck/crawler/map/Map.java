package nukeduck.crawler.map;

import java.util.ArrayList;
import java.util.List;

import nukeduck.crawler.graphics.Color;
import nukeduck.crawler.graphics.IRenderable;
import nukeduck.crawler.graphics.TextureMap;
import nukeduck.crawler.graphics.VBO;
import nukeduck.crawler.graphics.VBO.IVertices;
import nukeduck.crawler.graphics.VBO.VertexData;
import nukeduck.crawler.util.BoundingBox;
import nukeduck.crawler.util.ResourcePath;
import nukeduck.crawler.util.Vec2;

public class Map implements IVertices, IRenderable {
	private VBO vbo;

	public int getVertexCount() {
		int count = 0;
		for(int x = 0; x < this.size.x; x++) {
			for(int y = 0; y < this.size.y; y++) {
				count += this.getTile(x, y).getVertexCount();
			}
		}
		//System.out.println("Vertex count is " + count);
		return count;
	}

	public VertexData getVertex(int i) {
		for(int x = 0; x < this.size.x; x++) {
			for(int y = 0; y < this.size.y; y++) {
				int count = this.getTile(x, y).getVertexCount();

				if(i - count < 0) {
					VertexData data = this.getTile(x, y).getVertex(i);
					data.pos = data.pos.add(x * 16, y * 16);
					return data;
				} else {
					i -= count;
				}
			}
		}
		return null;
	}

	private Vec2 size;
	private Tile[][] mapTiles;

	public Map(int width, int height) {
		this(new Vec2(width, height));
	}
	public Map(Vec2 size) {
		this.size = size;

		this.mapTiles = new Tile[(int) this.size.x][(int) this.size.y];
		for(int x = 0; x < this.size.x; x++) {
			for(int y = 0; y < this.size.x; y++) {
				this.setTile(x, y, Tile.air);
			}
		}

		this.vbo = new VBO(this);
	}

	public Map(ResourcePath texture) {
		TextureMap map = new TextureMap(texture);
		this.size = map.size;
		this.mapTiles = map.map;
		this.vbo = new VBO(this);
	}

	public Vec2 getSize() {
		return this.size;
	}

	public Map setTile(Vec2 pos, Tile tile) {
		return this.setTile((int) pos.x, (int) pos.y, tile);
	}
	public Map setTile(int x, int y, Tile tile) {
		this.mapTiles[x][y] = tile;
		return this;
	}

	public Tile getTile(Vec2 pos) {
		return this.getTile((int) pos.x, (int) pos.y);
	}
	public Tile getTile(int x, int y) {
		return this.mapTiles[x][y];
	}

	public void prepare() {
		this.vbo.prepare();
	}

	public void render(float partialTicks) {
		Color.WHITE.applyRGBA();
		this.vbo.render();
	}

	public List<BoundingBox> getBoundingBoxes() {
		List<BoundingBox> boxes = new ArrayList<BoundingBox>();

		int i, j;
		for(int x = 0; x < this.size.x; x++) {
			for(int y = 0; y < this.size.y; y++) {
				if(this.getTile(x, y).collide()) {
					boxes.add(new BoundingBox(new Vec2(i = x * 16, j = y * 16), new Vec2(i + 16, j + 16)));
				}
			}
		}
		return boxes;
	}
}
