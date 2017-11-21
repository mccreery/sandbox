package tk.nukeduck.impossible.client.render.particle;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import tk.nukeduck.impossible.client.Main;
import tk.nukeduck.impossible.client.tile.Tile;

public class Particle
{
	/** The Tile object this particle came from. **/
	protected Tile sourceTile;
	
	/** The X and Y positions this particle is currently at. **/
	protected int positionX;
	protected int positionY;
	
	protected double velocityX;
	protected double velocityY;
	
	protected int lifeLeft;
	
	protected Graphics g;
	
	public Particle(Tile source, int x, int y, double velX, double velY, int life, Graphics g)
	{
		this.sourceTile = source;
		this.positionX = x;
		this.positionY = y;
		
		this.velocityX = velX;
		this.velocityY = velY;
		
		this.lifeLeft = life;
		this.g = g;
	}
	
	public void tick()
	{
		this.positionX += velocityX;
		this.positionY += velocityY;
		this.velocityY += 0.2;
		this.lifeLeft -= 1;
	}
	
	public int getLife()
	{
		return this.lifeLeft;
	}
	
	public void render()
	{
		g.setColor(new Color(0, 0, 0, 50));
		g.fillRect(this.positionX + 1 - Main.mapOffsetX, this.positionY + 1 - Main.mapOffsetY, 2, 2);
		g.drawImage(this.sourceTile.getTexture(), this.positionX - Main.mapOffsetX, this.positionY - Main.mapOffsetY, this.positionX + 2 - Main.mapOffsetX, this.positionY + 2 - Main.mapOffsetY, 0, 0, 2, 2, Color.white);
		g.setColor(Color.white);
	}
}
