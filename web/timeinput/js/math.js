export function lerp(a, b, t) {
  return a * (1 - t) + b * t;
}

export function inverseLerp(x, a, b) {
  return (x - a) / (b - a);
}

export function clamp(x, min, max) {
  return Math.min(Math.max(x, min), max);
}
