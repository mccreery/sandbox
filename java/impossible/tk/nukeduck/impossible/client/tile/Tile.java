package tk.nukeduck.impossible.client.tile;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.ShapeFill;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.fills.GradientFill;
import org.newdawn.slick.geom.Rectangle;

import tk.nukeduck.impossible.client.Main;
import tk.nukeduck.impossible.client.Map;
import tk.nukeduck.impossible.client.WorldGenerator;

public class Tile
{
	public static Tile sand = new Tile("sand.png").setWalkingSpeed(0.6F).setName("Sand");
	public static Tile grass = new Tile("grass.png").setName("Grass");
	public static Tile water = new TileWater("water.png").setName("Water");
	
	private Image texture;
	private float walkingSpeedMultiplier = 1.0F;
	private String name = "Unknown";
	
	public Tile(String textureFilePath)
	{
		try
		{
			this.texture = new Image(textureFilePath);
			this.texture.setFilter(Image.FILTER_NEAREST);
		} catch (SlickException e)
		{
			Logger.getLogger("Impossible").log(Level.WARNING, "Texture was unable to load for tile: " + textureFilePath + ".");
		}
	}
	
	public Tile(String textureFilePath, float walkingSpeedMultiplier)
	{
		this(textureFilePath);
		this.walkingSpeedMultiplier = walkingSpeedMultiplier;
	}
	
	public Tile setName(String name)
	{
		this.name = name;
		return this;
	}
	
	public Tile setWalkingSpeed(float speed)
	{
		this.walkingSpeedMultiplier = speed;
		return this;
	}
	
	public String getName()
	{
		return this.name;
	}
	
	public float getWalkingSpeed()
	{
		return this.walkingSpeedMultiplier;
	}
	
	public Image getTexture()
	{
		return this.texture;
	}
	
	public void renderTile(int x, int y, Map map)
	{
		Main.g.drawImage(map.getTileAt(x, y).getTexture(), 0 + (x * 16) - Main.mapOffsetX, 0 + (y * 16) - Main.mapOffsetY - map.getTileHeightAt(x, y), 16 + (x * 16) - Main.mapOffsetX, 16 + (y * 16) - Main.mapOffsetY - map.getTileHeightAt(x, y), 0, 0, 16, 16, new Color((float)(WorldGenerator.perlinNoise(x, y, 100, 1) + 0.8), (float)(WorldGenerator.perlinNoise(x, y, 100, 1) + 0.8), (float)(WorldGenerator.perlinNoise(x, y, 100, 1) + 0.8)));
		Main.g.drawImage(map.getTileAt(x, y).getTexture(), 0 + (x * 16) - Main.mapOffsetX, 0 + (y * 16) - Main.mapOffsetY + 16 - map.getTileHeightAt(x, y), 16 + (x * 16) - Main.mapOffsetX, 32 + (y * 16) - Main.mapOffsetY - map.getTileHeightAt(x, y), 0, 0, 16, 16, new Color((float)(WorldGenerator.perlinNoise(x, y, 100, 1) + 0.4), (float)(WorldGenerator.perlinNoise(x, y, 100, 1) + 0.4), (float)(WorldGenerator.perlinNoise(x, y, 100, 1) + 0.4)));
	}
	
	public void renderGradient(int x, int y, Map map)
	{
		Rectangle shadowRight = new Rectangle(x * 16 - Main.mapOffsetX, y * 16 - Main.mapOffsetY - map.getTileHeightAt(x, y), 4, 16);
        ShapeFill gradientRight = new GradientFill(0, 0, new Color(0, 0, 0, 0.5F), 1, 0, Color.transparent, true);
        
		Rectangle shadowLeft = new Rectangle(x * 16 + 12 - Main.mapOffsetX, y * 16 - Main.mapOffsetY - map.getTileHeightAt(x, y), 4, 16);
        ShapeFill gradientLeft = new GradientFill(1, 0, new Color(0, 0, 0, 0.5F), 0, 0, Color.transparent, true);
        
		Rectangle shadowTop = new Rectangle(x * 16 - Main.mapOffsetX, y * 16 - Main.mapOffsetY - map.getTileHeightAt(x, y), 16, 4);
        ShapeFill gradientTop = new GradientFill(0, 0, new Color(0, 0, 0, 0.5F), 0, 1, Color.transparent, true);
        
		Rectangle shadowBottom = new Rectangle(x * 16 - Main.mapOffsetX, y * 16 + 16 - Main.mapOffsetY - map.getTileHeightAt(x, y), 16, 4);
        ShapeFill gradientBottom = new GradientFill(0, 0, new Color(0, 0, 0, 0.5F), 0, 1, Color.transparent, true);
        
        if(map.getTileHeightAt(x + 1, y) < map.getTileHeightAt(x, y)) Main.g.fill(shadowRight, gradientRight);
        if(map.getTileHeightAt(x - 1, y) < map.getTileHeightAt(x, y)) Main.g.fill(shadowLeft, gradientLeft);
        if(map.getTileHeightAt(x, y + 1) < map.getTileHeightAt(x, y)) Main.g.fill(shadowBottom, gradientBottom);
        if(map.getTileHeightAt(x, y - 1) < map.getTileHeightAt(x, y)) Main.g.fill(shadowTop, gradientTop);
	}
}
