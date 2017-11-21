package tk.nukeduck.impossible.client;

import java.util.ArrayList;
import java.util.Random;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import tk.nukeduck.impossible.client.entity.Entity;
import tk.nukeduck.impossible.client.render.Render;
import tk.nukeduck.impossible.client.render.particle.Particle;
import tk.nukeduck.impossible.client.tile.Tile;

public class Map
{
	private Tile[][] tiles;
	private int[][] tileHeights;
	private ArrayList<Particle> particles;
	private ArrayList<Entity> entities;
	
	public Map(int width, int height)
	{
		Display.update();
		tiles = new Tile[width][height];
		tileHeights = new int[width][height];
		particles = new ArrayList<Particle>();
		entities = new ArrayList<Entity>();
		int tilesDone = 0;
		float loadingFrame = 0;
		Image load = null;
		try
		{
			load = new Image("load_anim.png");
			load.setFilter(Image.FILTER_NEAREST);
		} catch (SlickException e)
		{
			e.printStackTrace();
		}
		
		Random random = new Random();
		int offsetX = random.nextInt(2147483647) - 1073741823;
		int offsetY = random.nextInt(2147483647) - 1073741823;
		
		int heightOffsetX = random.nextInt(2147483647) - 1073741823;
		int heightOffsetY = random.nextInt(2147483647) - 1073741823;
		
		for(int x = 0; x < width; x++)
		{
			for(int y = 0; y < height; y++)
			{
				tilesDone += 1;
				if(loadingFrame > load.getWidth() / 16) loadingFrame = 0;
				Render.renderText(Math.round(Display.getWidth() / Main.scaleFactor / 2) - Main.font.getWidth("Loading terrain...") / 2, Math.round(Display.getHeight() / Main.scaleFactor / 2) - 16, "Loading terrain...", Color.white, Main.font);
				Render.renderText(Math.round(Display.getWidth() / Main.scaleFactor / 2) - Main.font.getWidth((tilesDone * 100) / (width * height) + "% Complete.") / 2, Math.round(Display.getHeight() / Main.scaleFactor / 2) - 9, (tilesDone * 100) / (width * height) + "% Complete.", Color.white, Main.font);
				Render.renderStatusBar(Math.round(Display.getWidth() / Main.scaleFactor / 2) - 20, Math.round(Display.getHeight() / Main.scaleFactor / 2), 40, 10, tilesDone, (width * height), Color.white);
				Display.update();
				tiles[x][y] = WorldGenerator.perlinNoise(x + offsetX, y + offsetY, 100, 1) > 0 ? Tile.sand : Tile.grass;
				tileHeights[x][y] = (int)Math.round(WorldGenerator.perlinNoise(x + heightOffsetX, y + heightOffsetY, 100, 1) * 20);
				GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
				if(Display.isCloseRequested()) Display.destroy();
			}
		}
		
		for(int x = 0; x < width; x++)
		{
			for(int y = 0; y < height; y++)
			{
				if(new Random().nextInt(50) == 0 && x > 0 && y > 0 && x < width - 1 && y < height - 1)
				{
					tiles[x][y] = Tile.water;
					tileHeights[x][y] = -8;
					
					tiles[x - 1][y] = Tile.water;
					tileHeights[x - 1][y] = -8;
					
					tiles[x + 1][y] = Tile.water;
					tileHeights[x + 1][y] = -8;
					
					tiles[x][y - 1] = Tile.water;
					tileHeights[x][y - 1] = -8;
					
					tiles[x][y + 1] = Tile.water;
					tileHeights[x][y + 1] = -8;
				}
			}
		}
	}
	
	public void renderBlockShadows(int x, int y)
	{
		if(getTileHeightAt(x + 1, y) > getTileHeightAt(x, y))
		{
			// Main.g.drawGradientLine(x * 16, y * 16 + 8, 0, 0, 0, 0, y * 16 + 16, y * 16 + 8, 0, 0, 0, 255);
		}
	}
	
	public void spawnParticle(Tile source, int x, int y, double velX, double velY, Graphics g)
	{
		if(source != null && g != null)
		{
			particles.add(new Particle(source, x, y, velX, velY, 20, g));
		}
	}
	
	public void particleTick()
	{
		for(int i = 0; i < particles.size(); i++)
		{
			particles.get(i).tick();
			if(particles.get(i).getLife() == 0)
			{
				particles.remove(i);
			}
		}
	}
	
	public void entityTick()
	{
		for(int i = 0; i < entities.size(); i++)
		{
			entities.get(i).tick();
			entities.get(i).lifeTime += 1;
			
			if(entities.get(i).getHealth() == 0)
			{
				entities.remove(i);
			}
		}
	}
	
	public void renderTick()
	{
		for(int i = 0; i < entities.size(); i++)
		{
			entities.get(i).render();
		}
		
		for(int i = 0; i < particles.size(); i++)
		{
			particles.get(i).render();
		}
	}
	
	public ArrayList<Particle> getParticles()
	{
		return this.particles;
	}
	
	public ArrayList<Entity> getEntities()
	{
		return this.entities;
	}
	
	public Tile getTileAt(int x, int y)
	{
		return tiles[x][y];
	}
	
	public int getTileHeightAt(int x, int y)
	{
		return tileHeights[x][y];
	}
	
	public int getWidth()
	{
		return tiles.length;
	}
	
	public int getHeight()
	{
		return tiles[0].length;
	}
}
