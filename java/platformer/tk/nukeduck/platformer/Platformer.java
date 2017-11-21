package tk.nukeduck.platformer;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import tk.nukeduck.platformer.map.Map;
import tk.nukeduck.platformer.player.Player;

public class Platformer extends BasicGame
{
	static AppGameContainer app;
	static Map currentMap;
	static Player player;
	
	public Platformer()
	{
		super("Platformer Game");
	}
	
	public static void main(String[] args)
	{
		try
		{
			app = new AppGameContainer(new Platformer());
			app.setTitle("Platformer");
			app.setDisplayMode(1920, 1080, true);
			app.start();
		} catch (SlickException e)
		{
			e.printStackTrace();
		}
	}
	
	@Override
	public void render(GameContainer container, Graphics g) throws SlickException
	{
		g.setColor(Color.cyan);
		g.fillRect(0, 0, app.getWidth(), app.getHeight());
		Image i = new Image("src/PlayerAnim.png");
		i.setFilter(Image.FILTER_NEAREST);
		i.draw(player.location.x, player.location.y, player.location.x + 128, player.location.y + 128, 0, 0, 16, 16);
		currentMap.render();
	}
	
	@Override
	public void init(GameContainer container) throws SlickException
	{
		currentMap = new Map();
		player = new Player(0, 0);
		try {
			Keyboard.create();
		} catch (LWJGLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void update(GameContainer container, int delta) throws SlickException
	{
		if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE))
		{
			app.destroy();
		}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_A))
		{
			Player.moveLeft(currentMap, player);
		} else if(Keyboard.isKeyDown(Keyboard.KEY_D))
		{
			Player.moveRight(currentMap, player);
		}
		
		if(!currentMap.getTile((int) (player.location.x) / 512, (int) (player.location.y - player.yVelocity) / 512).isOpaque) {
			player.yVelocity += 0.01;
			player.location.y += player.yVelocity;
		} else {
			player.yVelocity = 0.0F;
		}
	}
}
