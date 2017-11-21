package nukeduck.crawler.entity;

import nukeduck.crawler.graphics.IRenderable;
import nukeduck.crawler.util.BoundingBox;
import nukeduck.crawler.util.Vec2;

public abstract class Entity implements IRenderable {
	public BoundingBox pos = new BoundingBox(new Vec2(16, 0), new Vec2(31, 15));

	protected int health;
	protected int maxHealth;

	public Entity() {this(10);}
	public Entity(int maxHealth) {
		this.health = this.maxHealth = maxHealth;
	}

	public int getHealth() {
		return this.health;
	}
	public int getMaxHealth() {
		return this.maxHealth;
	}
	public void setHealth(int health) {
		this.health = health;
		updateDead();
	}
	public void heal(int x) {
		this.setHealth(this.health + x);
		updateDead();
	}
	public void kill() {
		this.setHealth(0);
		updateDead();
	}
	public boolean hit(IDamage damage) {
		if(!damage.canDamage(this)) return false;
		damage.damage(this);
		return true;
	}

	protected void updateDead() {
		if(this.health <= 0) {
			this.onDeath();
		}
	}

	public void onDeath() {}

	public abstract void render(float partialTicks);
}
