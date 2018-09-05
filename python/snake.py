import tkinter, random

LEFT, UP, RIGHT, DOWN = 0, 1, 2, 3
WASD = list(ord(c) for c in ('A', 'W', 'D', 'S'))
ARROWS = [37, 38, 39, 40]

SIZE, SCALE = 16, 32
FPS   = 10
DELAY = 1000 // FPS

PROVIDE, CONSUME = 1, 2
BOTH = PROVIDE | CONSUME

class Marker(object):
	def __init__(self, x=0, y=0, direction=RIGHT):
		self.x = x
		self.y = y
		self.direction = direction

	def advance(self, distance=1):
		distance *= (self.direction & 2) - 1
		if self.direction & 1:
			self.y += distance
			overflow = not self.y in range(SIZE)
			if overflow: self.y %= SIZE
		else:
			self.x += distance
			overflow = not self.x in range(SIZE)
			if overflow: self.x %= SIZE
		return overflow

	def __eq__(self, other):
		return self.x == other.x and self.y == other.y

class Segment(object):
	def __init__(self, canvas, start):
		self.canvas = canvas
		self.direction = start.direction
		self.distance = 1

		x = start.x * SCALE
		y = start.y * SCALE
		coords = [x + 1, y + 1, x + SCALE - 1, y + SCALE - 1]

		coords[self.direction ^ 2] += 2 * (1 - (self.direction & 2))

		self.rect = self.canvas.create_rectangle(*coords,
			fill="green", outline="")

	def advance(self, mode):
		if not mode: return
		coords = list(self.canvas.coords(self.rect))

		distance = ((self.direction & 2) - 1) * SCALE
		if mode & PROVIDE:
			coords[self.direction] += distance
			self.distance += 1
		if mode & CONSUME:
			coords[self.direction ^ 2] += distance
			self.distance -= 1

		self.canvas.coords(self.rect, *coords)

class Snake(tkinter.Canvas):
	def __init__(self):
		self.canvas_size = SIZE * SCALE
		self.max_score = SIZE * SIZE - 1
		self.digits = len(str(self.max_score))
		tkinter.Canvas.__init__(self, width=self.canvas_size, height=self.canvas_size, highlightthickness=0)

		self.pack()
		self.master.title("Snake")

		self.bind("<Key>", self.key_pressed)
		self.bind("<space>", self.reset)
		self.focus_set()
		self.segments = []
		self.gameover = None
		self.apple_shape = self.create_oval(0, 0, 0, 0, fill="red", outline="")
		self.display = self.create_text(self.canvas_size - 5, 5, font=(None, -48, "bold"), anchor=tkinter.NE)

		self.reset()
		self.after(DELAY, self.advance)

	def update_score(self):
		self.itemconfig(self.display, text=str(self.score).rjust(self.digits, "0"))

	def reset(self, *args, **kwargs):
		for segment in self.segments:
			self.delete(segment.rect)
		self.segments.clear()

		self.delete(self.gameover)
		self.score = 0
		centre = SIZE // 2
		self.head = Marker(centre, centre)
		self.apple = Marker()
		self.segments.append(Segment(self, self.head))
		self.move_apple()
		self.update_score()

		self.playing = True

	def show_score(self):
		self.gameover = self.create_text(self.canvas_size / 2, self.canvas_size / 2, justify=tkinter.CENTER,
			font=(None, -36), text="GAME OVER\nPress SPACE to reset")

	def move_apple(self):
		self.apple.x = random.randint(0, SIZE - 1)
		self.apple.y = random.randint(0, SIZE - 1)
		while self.collides(self.apple, True):
			self.apple.x = random.randint(0, SIZE - 1)
			self.apple.y = random.randint(0, SIZE - 1)

		self.coords(self.apple_shape, self.apple.x * SCALE, self.apple.y * SCALE,
			self.apple.x * SCALE + SCALE, self.apple.y * SCALE + SCALE)

	def collides(self, marker, head=False):
		track = Marker(self.head.x, self.head.y, self.segments[-1].direction)
		if not head:
			track.advance(-self.segments[-1].distance)

		for segment in self.segments[-1 if head else -2::-1]:
			track.direction = segment.direction
			track.advance(-segment.distance)

			if track.direction & 1:
				in_line = marker.x == track.x
				value = track.y - marker.y
			else:
				in_line = marker.y == track.y
				value = track.x - marker.x

			if track.direction & 2: # Going down
				value = -value
			value -= 1
			if in_line and value in range(segment.distance):
				return True
		return False

	def advance(self):
		if self.playing:
			overflow = self.head.advance()
			if overflow or self.head.direction != self.segments[-1].direction:
				self.segments.append(Segment(self, self.head))
				self.tag_raise(self.display)
			else:
				self.segments[-1].advance(PROVIDE)

		if self.playing and self.head == self.apple:
			self.score += 1
			self.update_score()
			self.move_apple()
		elif len(self.segments) > 0:
			self.segments[0].advance(CONSUME)

			if self.segments[0].distance == 0:
				self.delete(self.segments[0].rect)
				self.segments.pop(0)

		if self.playing and self.collides(self.head):
			self.show_score()
			self.playing = False
		self.after(DELAY, self.advance)

	def key_pressed(self, e):
		if self.playing:
			if e.keycode in WASD:
				new_direction = WASD.index(e.keycode)
			elif e.keycode in ARROWS:
				new_direction = ARROWS.index(e.keycode)
			else: return

			if new_direction != self.segments[-1].direction ^ 2:
				self.head.direction = new_direction

Snake().mainloop()
