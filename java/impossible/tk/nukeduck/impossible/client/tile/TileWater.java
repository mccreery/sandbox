package tk.nukeduck.impossible.client.tile;

import org.newdawn.slick.Color;

import tk.nukeduck.impossible.client.Main;
import tk.nukeduck.impossible.client.Map;
import tk.nukeduck.impossible.client.WorldGenerator;

public class TileWater extends Tile
{
	public TileWater(String textureFilePath)
	{
		super(textureFilePath);
		this.setWalkingSpeed(0.6F);
	}
	
	@Override
	public void renderTile(int x, int y, Map map)
	{
		Main.g.drawImage(map.getTileAt(x, y).getTexture(), 0 + (x * 16) - Main.mapOffsetX, 0 + (y * 16) - Main.mapOffsetY - map.getTileHeightAt(x, y), 16 + (x * 16) - Main.mapOffsetX, 16 + (y * 16) - Main.mapOffsetY - map.getTileHeightAt(x, y), 0, 0, 16, 16, new Color((float)(WorldGenerator.perlinNoise(x, y, 100, 1) + 0.8), (float)(WorldGenerator.perlinNoise(x, y, 100, 1) + 0.8), (float)(WorldGenerator.perlinNoise(x, y, 100, 1) + 0.8)));
	}
}
