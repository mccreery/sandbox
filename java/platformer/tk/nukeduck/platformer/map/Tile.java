package tk.nukeduck.platformer.map;

public class Tile
{
	public static Tile dirt = new Tile(4, 1);
	public static Tile dirtLeft = new Tile(3, 1);
	public static Tile dirtRight = new Tile(5, 1);
	
	public static Tile grass = new Tile(1, 1);
	public static Tile grassLeft = new Tile(0, 1);
	public static Tile grassRight = new Tile(2, 1);
	
	public static Tile grassPlatform = new Tile(1, 2);
	public static Tile grassPlatformLeft = new Tile(0, 2);
	public static Tile grassPlatformRight = new Tile(2, 2);
	
	public static Tile grassLeftInside = new Tile(6, 1);
	public static Tile grassRightInside = new Tile(7, 1);
	
	public static Tile ruby = new Tile(0, 3, false);
	public static Tile sapphire = new Tile(1, 3, false);
	public static Tile emerald = new Tile(2, 3, false);
	
	public static Tile spikes = new Tile(3, 2, false);
	public static Tile barrel = new Tile(3, 3);
	
	public static Tile air = new Tile(7, 0, false);
	
	int srcX;
	int srcY;
	public boolean isOpaque = true;
	
	public Tile(int srcX, int srcY)
	{
		this.srcX = srcX;
		this.srcY = srcY;
	}
	
	public Tile(int srcX, int srcY, boolean isOpaque) {
		this(srcX, srcY);
		this.isOpaque = isOpaque;
	}
	
	public void render(int x, int y, Map map)
	{
		map.texture.draw(x * 128, y * 128, this.srcX * 16, this.srcY * 16, (srcX * 16) + 16, (srcY * 16) + 16);
	}
}