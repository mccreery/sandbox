package nukeduck.crawler.util;

public class BoundingBox {
	public Vec2 min, max;

	public BoundingBox(Vec2 min, Vec2 max) {
		this.min = min;
		this.max = max;
	}

	@Override
	public BoundingBox clone() {
		return new BoundingBox(new Vec2(this.min.x, this.min.y), new Vec2(this.max.x, this.max.y));
	}
	public BoundingBox offset(Vec2 vec) {
		this.min.x += vec.x;
		this.max.x += vec.x;
		this.min.y += vec.y;
		this.max.y += vec.y;
		return this;
	}

	private boolean lineCollision(float al, float ar, float bl, float br) {
		return (al >= bl && al < br) ||
			(ar > bl && ar <= br);
	}

	public boolean collideX(BoundingBox last, BoundingBox next) {
		boolean collided = false;

		if(/*lineCollision(last.min.y, last.max.y, this.min.y, this.max.y) || */lineCollision(next.min.y, next.max.y, this.min.y, this.max.y)) {
			if(last.max.x <= this.min.x && next.max.x > this.min.x) { // Left -> Right
				next.offset(new Vec2(this.min.x - next.max.x, 0));
				//collided = true;
			}
			if(last.min.x >= this.max.x && next.min.x < this.max.x) { // Right -> Left
				next.offset(new Vec2(this.max.x - next.min.x, 0));
				//collided = true;
			}
		}

		return collided;
	}

	public boolean collideY(BoundingBox last, BoundingBox next) {
		boolean collided = false;

		if(/*lineCollision(last.min.x, last.max.x, this.min.x, this.max.x) || */lineCollision(next.min.x, next.max.x, this.min.x, this.max.x)) {
			System.out.println(next.min.x + ", " + next.max.x + ", " + this.min.x + ", " + this.max.x);
			if(last.max.y <= this.min.y && next.max.y > this.min.y) { // Top -> Bottom
				System.out.println("Colliding top -> bottom");
				next.offset(new Vec2(0, this.min.y - next.max.y));
				collided = true;
			}
			if(last.min.y >= this.max.y && next.min.y < this.max.y) { // Bottom -> Top
				System.out.println("Colliding bottom -> top");
				next.offset(new Vec2(0, this.max.y - next.min.y));
				//collided = true;
			}
		}
		return collided;
	}
}
