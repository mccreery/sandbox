const canvas = document.getElementById("canvas");
const train = canvas.getElementById("train");

function lerp(a, b, t) {
  return a * (1 - t) + b * t;
}

function inverseLerp(a, b, x) {
  return (x - a) / (b - a);
}

class Point {
  constructor(x, y) {
    this.x = x;
    this.y = y;
  }

  add(point) {
    return new Point(this.x + point.x, this.y + point.y);
  }

  sub(point) {
    return new Point(this.x - point.x, this.y - point.y);
  }

  scale(factor) {
    return new Point(this.x * factor, this.y * factor);
  }

  get lengthSq() {
    return this.x * this.x + this.y * this.y;
  }

  get length() {
    return Math.sqrt(this.lengthSq);
  }

  get angle() {
    return Math.atan2(this.y, this.x);
  }

  normalized() {
    return this.scale(1 / this.length);
  }

  rotate90() {
    return new Point(this.y, -this.x);
  }

  static lerp(a, b, t) {
    return new Point(lerp(a.x, b.x, t), lerp(a.y, b.y, t));
  }
}

class Circle {
  constructor(center, radius) {
    this.center = center;
    this.radius = radius;
  }

  static fromChord(pointA, pointB, radius, sign) {
    const chordCenter = pointA.add(pointB).scale(0.5);
    const normal = pointB.sub(pointA).normalized().rotate90();

    const knownSideSq = pointB.sub(pointA).lengthSq / (2 * 2);
    const missingSide = Math.sqrt(radius * radius - knownSideSq);

    return new Circle(chordCenter.add(normal.scale(missingSide * sign)), radius);
  }

  at(angle) {
    return this.center.add(new Point(Math.cos(angle), Math.sin(angle)).scale(this.radius));
  }
}

class ArcSegment {
  constructor(pointA, pointB, radius, circleSign, counterClockwise) {
    this.circle = Circle.fromChord(pointA, pointB, radius, circleSign);
    this.angleA = pointA.sub(this.circle.center).angle;
    this.angleB = pointB.sub(this.circle.center).angle;

    // Fix discontinuity at pi/-pi
    if (counterClockwise && this.angleA > this.angleB) {
      this.angleB += Math.PI * 2;
    } else if (!counterClockwise && this.angleA < this.angleB) {
      this.angleA += Math.PI * 2;
    }
  }

  get length() {
    return this.circle.radius * Math.abs(this.angleB - this.angleA);
  }

  at(distance) {
    const angle = lerp(this.angleA, this.angleB, distance / this.length);
    return this.circle.at(angle);
  }
}

class LineSegment {
  constructor(pointA, pointB) {
    this.pointA = pointA;
    this.pointB = pointB;
  }

  get length() {
    return this.pointB.sub(this.pointA).length;
  }

  at(distance) {
    return Point.lerp(this.pointA, this.pointB, distance / this.length);
  }
}

const path = [
  new LineSegment(new Point(4, 20), new Point(4, 8)),
  new ArcSegment(new Point(4, 8), new Point(8, 4), 4, -1, true),
  new ArcSegment(new Point(8, 4), new Point(12, 8), 4, -1, true),
  new ArcSegment(new Point(12, 8), new Point(10, 12), 6, -1, true),
  new ArcSegment(new Point(10, 12), new Point(8, 16), 6, 1, false),
  new LineSegment(new Point(8, 16), new Point(8, 20)),
];
const totalLength = path.reduce((a, b) => a + b.length, 0);

const speed = 5; // pixels per second
const scale = 20; // pixels per unit

function frame(time) {
  time = time / 1000;
  let distance = (speed * time) % totalLength;

  for (let i = 0; i < path.length; i++) {
    const segmentLength = path[i].length;

    if (distance < segmentLength) {
      const center = path[i].at(distance).scale(scale);

      train.setAttributeNS(null, "cx", center.x);
      train.setAttributeNS(null, "cy", center.y);
      break;
    } else {
      distance -= segmentLength;
    }
  }
  requestAnimationFrame(frame);
}
requestAnimationFrame(frame);
