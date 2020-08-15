class Vec2i {
  static ZERO = Object.freeze(new Vec2i());

  constructor(x = 0, y = 0) {
    this.x = x;
    this.y = y;
  }

  get squareMagnitude() {
    return this.dot(this);
  }

  get magnitude() {
    return Math.sqrt(this.squareMagnitude);
  }

  dot(v) {
    return this.x * v.x + this.y * v.y;
  }

  add(v) {
    return new Vec2i(this.x + v.x, this.y + v.y);
  }

  sub(v) {
    return new Vec2i(this.x - v.x, this.y - v.y);
  }

  scale(f) {
    return new Vec2i(this.x * f, this.y * f);
  }
}

class Piece {
  constructor(node, correctPosition) {
    this.node = node;
    this.correctPosition = correctPosition;
  }
}

const puzzle = document.getElementById("puzzle");
