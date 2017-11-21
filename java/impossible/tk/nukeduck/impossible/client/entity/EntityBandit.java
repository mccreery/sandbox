package tk.nukeduck.impossible.client.entity;

import tk.nukeduck.impossible.client.Main;

public class EntityBandit extends Entity
{
	private int shootCooldown = 0;
	
	public EntityBandit(float x, float y, float health, float speed)
	{
		super(x, y, health, speed);
		this.setName("Bandit");
	}
	
	public void tick()
	{
		if(getDistance((Entity)this, Main.playerXPosition, Main.playerYPosition) > 64)
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
		} else
		{
			if(shootCooldown == 0)
			{
				Entity.spawnEntity(Main.gameMap, new EntityBullet(this.xPosition, this.yPosition, 2.0F).setAngle(Main.getAngleBetweenPoints(this.xPosition, this.yPosition, Main.playerXPosition, Main.playerYPosition)).setCreator(this));
				shootCooldown = 40;
			} else
			{
				shootCooldown -= 1;
			}
		}
		
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
}
