package tk.nukeduck.impossible.client.render.particle;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import tk.nukeduck.impossible.client.Main;
import tk.nukeduck.impossible.client.render.Render;
import tk.nukeduck.impossible.client.tile.Tile;

public class ParticleText extends Particle
{
	protected String text = "";
	protected Color color = Color.white;
	
	public ParticleText(int x, int y, double velX, double velY,int life, Graphics g, String text, Color color)
	{
		super(Tile.grass, x, y, velX, velY, life, g);
		this.text = text;
		this.color = color;
	}
	
	public void render()
	{
		Render.renderText(Math.round(this.positionX - Main.mapOffsetX), Math.round(this.positionY - Main.mapOffsetY), text, color, Main.font);
	}
	
	public void tick()
	{
		this.positionX += velocityX;
		this.positionY += velocityY;
		this.velocityY += 0.1;
		this.lifeLeft -= 1;
	}
}