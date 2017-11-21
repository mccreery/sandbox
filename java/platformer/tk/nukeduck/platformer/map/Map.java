package tk.nukeduck.platformer.map;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Map
{
	Tile[][] tiles = {
			{Tile.air,Tile.air,Tile.air,Tile.air,Tile.air,Tile.air,Tile.air,Tile.air,Tile.air,Tile.air,Tile.air,Tile.air,Tile.air,Tile.air,Tile.air,Tile.air,Tile.air,Tile.air,Tile.air,Tile.air,Tile.air,Tile.air,Tile.air,Tile.air,Tile.air,Tile.air},  
			{Tile.air, Tile.air, Tile.air, Tile.air, Tile.air, Tile.air,Tile.air,Tile.air,Tile.air,Tile.air,Tile.air,Tile.air,Tile.air,Tile.air,Tile.air,Tile.air,Tile.air,Tile.air,Tile.air,Tile.air,Tile.air,Tile.air,Tile.air,Tile.air,Tile.air,Tile.air},  
			{Tile.air,Tile.air,Tile.air,Tile.air,Tile.air,Tile.air,Tile.air,Tile.air,Tile.air,Tile.air,Tile.air,Tile.air,Tile.air,Tile.air,Tile.air,Tile.air,Tile.air,Tile.air,Tile.air,Tile.air,Tile.air,Tile.air,Tile.air,Tile.emerald,Tile.emerald,Tile.air},  
			{Tile.air,Tile.air,Tile.air,Tile.air,Tile.air,Tile.sapphire,Tile.air,Tile.air,Tile.air, Tile.air,Tile.air,Tile.air,Tile.air,Tile.air,Tile.air,Tile.air,Tile.air,Tile.air,Tile.air, Tile.grassPlatformLeft,Tile.grassPlatformRight,Tile.air,Tile.grassLeft,Tile.grass,Tile.grass,Tile.grassRight},  
			{Tile.air,Tile.air,Tile.grassLeft,Tile.grass,Tile.grass,Tile.grass,Tile.grass,Tile.grass,Tile.grass,Tile.grass,Tile.grassRight,Tile.air,Tile.air,Tile.grassLeft,Tile.grassRight,Tile.air,Tile.air,Tile.air,Tile.air,Tile.air,Tile.air,Tile.air,Tile.dirtLeft,Tile.dirt,Tile.dirt,Tile.dirtRight},  
			{Tile.air,Tile.ruby,Tile.dirtLeft,Tile.dirt,Tile.dirt,Tile.dirt,Tile.dirt,Tile.dirt,Tile.dirt,Tile.dirt,Tile.dirtRight,Tile.air,Tile.air,Tile.dirtLeft,Tile.dirtRight,Tile.air, Tile.grassPlatformLeft,Tile.grassPlatformRight,Tile.air,Tile.air, Tile.spikes,Tile.barrel,Tile.dirtLeft,Tile.dirt,Tile.dirt,Tile.dirtRight},  
			{Tile.air,Tile.grass,Tile.grass,Tile.grass,Tile.grassRightInside,Tile.dirt,Tile.dirt,Tile.dirt,Tile.dirt,Tile.dirt,Tile.dirtRight, Tile.spikes, Tile.spikes,Tile.dirtLeft,Tile.dirtRight,Tile.air,Tile.air,Tile.air,Tile.air,Tile.grassLeft,Tile.grass,Tile.grass,Tile.grassRightInside,Tile.dirt,Tile.dirt,Tile.dirtRight},  
			{Tile.dirtLeft,Tile.dirt,Tile.dirt,Tile.dirt,Tile.dirt,Tile.dirt,Tile.dirt,Tile.dirt,Tile.dirt,Tile.dirt,Tile.grassLeftInside,Tile.grass,Tile.grass,Tile.grassRightInside,Tile.dirtRight,Tile.air,Tile.air,Tile.air,Tile.air,Tile.dirtLeft,Tile.dirt,Tile.dirt,Tile.dirt,Tile.dirt,Tile.dirt,Tile.dirtRight},  
			{Tile.dirtLeft,Tile.dirt,Tile.dirt,Tile.dirt,Tile.dirt,Tile.dirt,Tile.dirt,Tile.dirt,Tile.dirt,Tile.dirt,Tile.dirt,Tile.dirt,Tile.dirt,Tile.dirt,Tile.dirtRight,Tile.air,Tile.air,Tile.air,Tile.air,Tile.dirtLeft,Tile.dirt,Tile.dirt,Tile.dirt,Tile.dirt,Tile.dirt,Tile.dirtRight}
	};
	
	public Image texture;
	
	public Map(Tile[][] tiles, String path)
	{
		this.tiles = tiles;
		try 
		{
			this.texture = new Image(path);
		} catch (SlickException e)
		{
			e.printStackTrace();
		}
	}
	
	public Tile getTile(int x, int y) {
		return tiles[x][y] != null ? tiles[x][y] : Tile.air;
	}
	
	public Map()
	{
		try 
		{
			this.texture = new Image("src/PlayerAnim.png");
		} catch (SlickException e)
		{
			e.printStackTrace();
		}
	}
	
	public void render()
	{
		for(int x = 0; x < tiles[0].length; x++)
		{
			for(int y = 0; y < tiles.length; y++)
			{
				tiles[y][x].render(x, y, this);
			}
		}
	}
}
