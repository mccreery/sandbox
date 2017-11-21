package nukeduck.crawler.graphics;

import java.nio.ByteBuffer;

import nukeduck.crawler.map.Tile;
import nukeduck.crawler.util.ResourcePath;

public class TextureMap extends Texture {
	public Tile[][] map;

	public TextureMap(ResourcePath resource) {
		super(resource);
	}

	public void processImage(ByteBuffer buffer, int width, int height) {
		this.map = new Tile[width][height];

		for(int i = 0; i < buffer.capacity(); i += 4) {
			int j = i / 4;
			if(buffer.get(i + 3) != (byte) 0) {
				this.map[j % width][j / width] = Tile.redBrick;
			} else {
				this.map[j % width][j / width] = Tile.air;
			}
		}
	}
}
