package tk.nukeduck.impossible.client.entity;

import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import tk.nukeduck.impossible.client.Main;
import tk.nukeduck.impossible.client.render.particle.ParticleText;

public class EntityZombie extends Entity
{
	Image texture;
	Image textureLeft;
	
	public EntityZombie(float x, float y, int health)
	{
		super(x, y, health, 0.1F);
		this.setName("Zombie");
		
		try
		{
			texture = new Image("zombie_anim.png");
			texture.setFilter(Image.FILTER_NEAREST);
			textureLeft = new Image("zombie_anim_left.png");
			textureLeft.setFilter(Image.FILTER_NEAREST);
		} catch(SlickException e)
		{
			e.printStackTrace();
		}
	}
	
	public void tick()
	{
		if(Main.playerXPosition > this.xPosition - 8)
		{
			xPosition += speed;
			right = true;
		}
		else if(Main.playerXPosition < this.xPosition + 8)
		{
			xPosition -= speed;
			right = false;
		}
		
		if(Main.playerYPosition > this.yPosition) yPosition += speed;
		else if(Main.playerYPosition < this.yPosition) yPosition -= speed;
		
		xPosition += xVelocity;
		yPosition += yVelocity;
		
		if(xVelocity >= 0.1)
		{
			xVelocity -= 0.1;
		} else if(xVelocity <= -0.1)
		{
			xVelocity += 0.1;
		} else
		{
			xVelocity = 0;
		}
		
		if(yVelocity > 0)
		{
			yVelocity -= 0.1;
		} else if(yVelocity < 0)
		{
			yVelocity += 0.1;
		}
		
		if(getDistance(this, Main.playerXPosition, Main.playerYPosition) < 8)
		{
			if(right) Main.playerXVelocity = 2;
			else Main.playerXVelocity = -2;
			Main.playerHealth -= 2;
			Main.gameMap.getParticles().add(new ParticleText(Math.round(Main.playerXPosition), Math.round(Main.playerYPosition), 2, -1, 30, Main.g, "-" + Integer.toString(2), Color.red));
			Main.lastAttacker = this;
		}
		
		animationCounter += speed / 3;
		if(Math.floor(animationCounter) + 1 >= Main.playerTexture.getWidth() / 16) animationCounter = 0;
	}
	
	@Override
	public void render()
	{
		if(!right)
		{
			Main.g.drawImage(textureLeft, this.getXPosition() - 8 - Main.mapOffsetX, this.getYPosition() - 8 - Main.mapOffsetY - Main.currentTileHeight, 8 + this.getXPosition() - Main.mapOffsetX, 8 + this.getYPosition() - Main.mapOffsetY - Main.currentTileHeight, 16 * Math.round(animationCounter), 0, 16 * Math.round(animationCounter + 1), 16, Color.white);
		} else
		{
			Main.g.drawImage(texture, this.getXPosition() - 8 - Main.mapOffsetX, this.getYPosition() - 8 - Main.mapOffsetY - Main.currentTileHeight, 8 + this.getXPosition() - Main.mapOffsetX, 8 + this.getYPosition() - Main.mapOffsetY - Main.currentTileHeight, 16 * Math.round(animationCounter), 0, 16 * Math.round(animationCounter + 1), 16, Color.white);
		}
	}
}
