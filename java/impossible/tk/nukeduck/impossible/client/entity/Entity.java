package tk.nukeduck.impossible.client.entity;

import java.util.Random;

import org.newdawn.slick.Color;

import tk.nukeduck.impossible.client.Main;
import tk.nukeduck.impossible.client.Map;

public class Entity
{
	public Entity creator;
	
	protected float xPosition = 0;
	protected float yPosition = 0;
	
	/** The time in game ticks this entity has lived for. **/
	public int lifeTime = 0;
	
	public float xVelocity = 0.0F;
	public float yVelocity = 0.0F;
	
	protected float speed = 1.0F;
	
	protected float health = 10;
	protected boolean right = false;
	
	private String name = "";
	
	public Entity(float x, float y, float health, float speed)
	{
		this.xPosition = x;
		this.yPosition = y;
		this.xVelocity = 0.0F;
		this.yVelocity = 0.0F;
		this.health = health;
		this.speed = speed;
	}
	
	public Entity setCreator(Entity e)
	{
		this.creator = e;
		return this;
	}
	
	public Entity setInvulnerable()
	{
		return this.setHealth(Float.POSITIVE_INFINITY);
	}
	
	public Entity setHealth(float health)
	{
		this.health = health;
		return this;
	}
	
	public Entity setName(String name)
	{
		this.name = name;
		return this;
	}
	
	public static void spawnEntity(Map map, Entity entity)
	{
		map.getEntities().add(entity);
	}
	
	public void onDeath()
	{
		// Do nothing
	}
	
	public float getHealth()
	{
		return health;
	}
	
	float animationCounter = 0;
	
	public void render()
	{
		animationCounter += speed / 3;
		if(Math.floor(animationCounter) + 1 >= Main.playerTexture.getWidth() / 32) animationCounter = 0;
		if(!right)
		{
			Main.g.drawImage(Main.playerTextureLeft, this.getXPosition() - 16 - Main.mapOffsetX, this.getYPosition() - 16 - Main.mapOffsetY, 16 + this.getXPosition() - Main.mapOffsetX, 16 + this.getYPosition() - Main.mapOffsetY, 32 * Math.round(animationCounter), 0, 32 * Math.round(animationCounter + 1), 32, Color.green);
		} else
		{
			Main.g.drawImage(Main.playerTexture, this.getXPosition() - 16 - Main.mapOffsetX, this.getYPosition() - 16 - Main.mapOffsetY, 16 + this.getXPosition() - Main.mapOffsetX, 16 + this.getYPosition() - Main.mapOffsetY, 32 * Math.round(animationCounter), 0, 32 * Math.round(animationCounter + 1), 32, Color.green);
		}
	}
	
	Random random = new Random();
	
	public double getDistance(Entity entity1, Entity entity2)
	{
		//    ____________________
		//   /       2          2
		// \/ (y2-y1)  + (x2-x1)
		return Math.sqrt(Math.pow(entity2.getYPosition() - entity1.getYPosition(), 2) + Math.pow(entity2.getXPosition() - entity1.getXPosition(), 2));
	}
	
	public static double getDistance(Entity entity, float x, float y)
	{
		return Math.sqrt(Math.pow(y - entity.getYPosition(), 2) + Math.pow(x - entity.getXPosition(), 2));
	}
	
	public void tick()
	{
		if(Main.playerXPosition > this.xPosition)
		{
			xPosition += speed;
			right = true;
		}
		else if(Main.playerXPosition < this.xPosition)
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
	}
	
	public float getXPosition()
	{
		return xPosition;
	}
	
	public float getYPosition()
	{
		return yPosition;
	}

	public String getName()
	{
		return name;
	}
}
