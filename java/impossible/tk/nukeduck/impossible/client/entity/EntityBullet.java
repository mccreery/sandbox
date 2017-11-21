package tk.nukeduck.impossible.client.entity;

import org.newdawn.slick.Color;

import tk.nukeduck.impossible.client.Main;
import tk.nukeduck.impossible.client.render.particle.ParticleText;

public class EntityBullet extends Entity
{
	protected double damage = 1.0;
	protected double damageVariance = 0.0;
	protected Color color = Color.black;
	protected double angle = 1.0;
	
	public EntityBullet(float x, float y, float speed)
	{
		super(x, y, 1.0F, speed);
		this.damage = 1.5;
		this.damageVariance = 0.5;
		this.setInvulnerable();
		this.setName("Bullet");
	}
	
	public void render()
	{
		Main.g.setColor(this.color);
		Main.g.fillRect(this.getXPosition() - 1 - Main.mapOffsetX, this.getYPosition() - 1 - Main.mapOffsetY, 2, 2);
		// Render.renderText(Math.round(this.getXPosition() - Main.mapOffsetX + 2), Math.round(this.getYPosition() - Main.mapOffsetY + 2), "Angle: " + angle, color, Main.font);
	}
	
	public void tick()
	{
		xPosition += Math.cos(Math.toRadians(angle)) * speed;
		yPosition += Math.sin(Math.toRadians(angle)) * speed;
		
		if(this.lifeTime == 80)
		{
			this.setHealth(0);
		}
		if(Entity.getDistance(this, Main.playerXPosition, Main.playerYPosition) < 8)
		{
			this.setHealth(0);
			
			if(right) Main.playerXVelocity = 2;
			else Main.playerXVelocity = -2;
			double damage;
			damage = this.damage/* + new Random().nextDouble() * this.damageVariance - (this.damageVariance / 2)*/;
			Main.playerHealth -= damage;
			
			Main.gameMap.getParticles().add(new ParticleText(Math.round(Main.playerXPosition), Math.round(Main.playerYPosition), 2, -1, 30, Main.g, "-" + Double.toString(damage), Color.red));
			Main.lastAttacker = this;
		}
	}

	public EntityBullet setAngle(double angle)
	{
		this.angle = angle;
		return this;
	}
}
