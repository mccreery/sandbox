import math, re, sys
from PIL import Image, ImageOps

def is_yes(answer):
  return answer.lower().strip() in ("y", "yes")

def get_size():
  while True:
    try:
      size = int(input("Size in pixels: "))

      if size <= 0:
        print("Invalid size <= 0", file=sys.stderr)
      elif size & (size - 1) == 0 \
          or is_yes(input("Warning: size is not a power of two (e.g. 1024). Continue?")):
        return size
    except ValueError as e:
      print("Invalid size:", e, file=sys.stderr)

def get_path():
  path = input("Save as: ")

  if not path.endswith(".png"):
    path += ".png"

  return path

def map_range(in_a, in_b, out_a, out_b, x):
  f = (x - in_a) / (in_b - in_a)
  return out_a * (1 - f) + out_b * f

size = get_size()
invert_y = is_yes(input("Invert Y: "))
clamp = is_yes(input("Clamp: "))
path = get_path()

image = Image.new("RGB", (size, size))
pixels = image.load()

for x in range(size):
  for y in range(size):
    world_x = map_range(0, size, -1, 1, x + 0.5)
    world_y = map_range(0, size, -1, 1, y + 0.5)

    m_sq = world_x*world_x + world_y*world_y

    if m_sq > 1:
      if clamp:
        m = math.sqrt(m_sq)
        world_x /= m
        world_y /= m

        m_sq = 1
      else:
        pixels[x, y] = (128, 128, 255)
        continue

    world_z = math.sqrt(1 - m_sq)

    r = round(map_range(-1, 1, 0, 255, world_x))
    g = round(map_range(-1, 1, 0, 255, world_y))
    b = round(map_range(-1, 1, 0, 255, world_z))

    pixels[x, y] = (r, g, b)

if invert_y:
  image = ImageOps.flip(image)
image.save(path)
