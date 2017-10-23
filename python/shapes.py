import math
from point import Point
from itertools import chain

def polygon(sides, radius):
    step = 2.0 * math.pi / sides
    for i in range(sides):
        angle = step * i
        yield Point(radius * math.cos(angle), radius * math.sin(angle))

def star(sides, radius, mult = 0.5):
    step = 2.0 * math.pi / sides
    step_half = step / 2.0
    for i in range(sides):
        angle = step * i
        yield Point(radius * math.cos(angle), radius * math.sin(angle))
        angle += step_half
        yield Point(radius * math.cos(angle), radius * math.sin(angle)) * mult

def rounded_rect(width, height, radius, resolution):
    # Top left
    for i in range(math.pi / 2, step=math.pi / 10):


class Path:
    def __init__(self, points, offset=Point.ORIGIN, rotation=0, fill=None, stroke=None, stroke_width=None):
        self.points = points
        self.offset = offset
        self.rotation = rotation
        self.fill = fill
        self.stroke = stroke
        self.stroke_width = stroke_width

    def __str__(self):
        params = (
            " fill=\"%s\"" % self.fill if self.fill is not None else "",
            " stroke=\"%s\"" % self.stroke if self.stroke is not None else "",
            " stroke-width=\"%d\"" % self.stroke_width if self.stroke_width is not None else ""
        )
        d = "<path%s%s%s d=\"M" % params
        for point in self.points:
            if self.rotation != 0: point.rotate(self.rotation)
            point += self.offset
            d += str(point.x) + "," + str(point.y) + "L"
        return d[:-1] + "Z\"/>"

#print(str(Path(star(5, 100.0), Point(100, 100), rotation=-math.pi / 2, fill="gold")) +
#    str(Path(star(5, 50.0), Point(100, 100), rotation=-math.pi / 2, fill="red")))

a = "<polygon points=\""
points = star(5, 100.0)
for point in points:
    point.rotate(-math.pi / 2)
    point += Point(100.0, 100.0)
    a += str(point.x) + "," + str(point.y) + " "
a = a[:-1] + "\"/>"
print(a)