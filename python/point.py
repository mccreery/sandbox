import math

class Point(object):
    def __init__(self, x, y):
        self.x = x
        self.y = y

    def __repr__(self):
        return "(" + str(self.x) + ", " + str(self.y) + ")"

    def rotate(self, angle):
        sin = math.sin(angle)
        cos = math.cos(angle)
        x = self.x * cos - self.y * sin
        y = self.x * sin + self.y * cos
        self.x = x
        self.y = y
        return self

    def __mul__(self, other):
        return type(self)(self.x * other, self.y * other)
    def __rmul__(self, other):
        return self.__mul__(other)

    def __truediv__(self, other):
        return self.__mul__(1.0 / other)

    def __neg__(self):
        return type(self)(-self.x, -self.y)
    def __pos__(self):
        return self
    def __abs__(self):
        return type(self)(abs(self.x), abs(self.y))

    def __add__(self, other):
        return type(self)(self.x + other.x, self.y + other.y)
    def __sub__(self, other):
        return self.__add__(-other)

    def __eq__(self, other):
        return self.x == other.x and self.y == other.y
    def __ne__(self, other):
        return self.x != other.x or self.y != other.y

Point.ORIGIN = Point(0, 0)