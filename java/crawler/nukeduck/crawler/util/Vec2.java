package nukeduck.crawler.util;

public class Vec2 {
	public float x, y;

	public Vec2(float x, float y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public Vec2 clone() {
		return new Vec2(this.x, this.y);
	}

	public Vec2 moveUp() {return this.moveUp(1);}
	public Vec2 moveUp(float n) {
		this.y -= n;
		return this;
	}
	public Vec2 moveDown() {return this.moveDown(1);}
	public Vec2 moveDown(float n) {
		this.y += n;
		return this;
	}

	public Vec2 moveLeft() {return this.moveLeft(1);}
	public Vec2 moveLeft(float n) {
		this.x -= n;
		return this;
	}
	public Vec2 moveRight() {return this.moveRight(1);}
	public Vec2 moveRight(float n) {
		this.x += n;
		return this;
	}

	public Vec2 add(Vec2 vec) {return this.add(vec.x, vec.y);}
	public Vec2 subtract(Vec2 vec) {return this.add(-vec.x, -vec.y);}

	public Vec2 multiply(Vec2 vec) {return this.multiply(vec.x, vec.y);}
	public Vec2 multiply(float factor) {return this.multiply(factor, factor);}

	public Vec2 add(float x, float y) {
		return new Vec2(this.x + x, this.y + y);
	}
	public Vec2 multiply(float x, float y) {
		return new Vec2(this.x * x, this.y * y);
	}

	public double getDistanceSquared(Vec2 vec) {
		double dx = vec.x - this.x;
		double dy = vec.y - this.y;
		return dx * dx + dy * dy;
	}
	public double getDistance(Vec2 vec) {
		return Math.sqrt(this.getDistanceSquared(vec));
	}

	@Override
	public boolean equals(Object object) {
		Vec2 vec = (Vec2) object;
		return this.x == vec.x && this.y == vec.y;
	}

	@Override
	public String toString() {
		return "(" + this.x + ", " + this.y + ")";
	}
}
