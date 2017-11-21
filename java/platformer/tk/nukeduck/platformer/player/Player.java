package tk.nukeduck.platformer.player;

import org.newdawn.slick.geom.Vector2f;

import tk.nukeduck.platformer.map.Map;

public class Player
{
	public Vector2f location;
	public float yVelocity = 0.0F;
	public int health = 100;
	
	public Player(Vector2f location)
	{
		this.location = location;
	}
	
	public Player(float x, float y)
	{
		this.location = new Vector2f(x, y);
	}
	
	public float xVelocity = 0.0F;
	
	public static void moveLeft(Map map, Player player) {
		player.xVelocity = -0.5F;
		if(map.getTile((int) (player.location.x / 512) - 1, (int) (player.location.y / 512)) != null && map.getTile((int) (player.location.x / 512) - 1, (int) (player.location.y / 512)).isOpaque && player.location.x + player.xVelocity % 1F < 0.1F) {
			player.location.x = (int) player.location.x + 0.1F;
		} else {
			player.location.x += player.xVelocity;
		}
	}
	
	public static void moveRight(Map map, Player player) {
		player.xVelocity = 0.5F;
		if(map.getTile((int) (player.location.x / 512) + 1, (int) (player.location.y / 512)).isOpaque && player.location.x + player.xVelocity % 1F > 0.9F) {
			player.location.x = (int) player.location.x + 0.1F;
		} else {
			player.location.x += player.xVelocity;
		}
	}
}