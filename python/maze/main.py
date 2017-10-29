from microbit import *
import os

flash = [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 8, 7, 6, 5, 4, 3, 2, 1]
current_flash = 0

def get_flash():
	global current_flash
	current_flash = (current_flash + 1) % len(flash)
	return flash[current_flash]

PLAYER    = 9
COLLISION = 5
AIR       = 0
START     = 1
FINISH    = 2

mazes = os.listdir()
maze_path = ""
while maze_path == "":
	for maze in mazes:
		display.scroll(maze, delay=100)

		b_pressed = False
		while not button_a.is_pressed() and not b_pressed:
			b_pressed = button_b.is_pressed()

		if b_pressed:
			maze_path = maze
			break

with open(maze_path) as file:
	maze = file.read()

# Convert maze
maze = Image(maze.replace('#', str(COLLISION)).replace(' ', str(AIR))
	.replace('S', str(START)).replace('F', str(FINISH)))

player = [0, 0]
for y in range(maze.height()):
	for x in range(maze.width()):
		if maze.get_pixel(x, y) == START:
			player[0] = x
			player[1] = y
			break

player_direction = 0
showing_arrow = False

arrows = [Image.ARROW_N, Image.ARROW_E, Image.ARROW_S, Image.ARROW_W]

def turn():
	global player_direction
	global showing_arrow
	global last_time

	player_direction = (player_direction + 1) % 4
	display.show(arrows[player_direction])
	showing_arrow = True
	last_time = running_time()

def allow_movement(x, y):
	return x in range(maze.width()) and y in range(maze.height()) and maze.get_pixel(x, y) != COLLISION

def advance():
	direction = arrows[player_direction]

	if direction == Image.ARROW_N:
		if allow_movement(player[0], player[1] - 1):
			player[1] -= 1
			return True
	elif direction == Image.ARROW_E:
		if allow_movement(player[0] + 1, player[1]):
			player[0] += 1
			return True
	elif direction == Image.ARROW_S:
		if allow_movement(player[0], player[1] + 1):
			player[1] += 1
			return True
	else:
		if allow_movement(player[0] - 1, player[1]):
			player[0] -= 1
			return True
	return False

def draw_display():
	map_moved = maze.crop(player[0] - 2, player[1] - 2, 5, 5)
	display.show(map_moved)
	display.set_pixel(2, 2, PLAYER)

display.scroll("MAZE", delay=100)
sleep(500)
#display.show(Image.ARROW_W)
#sleep(500)
#display.scroll("A to turn", delay=100)
#sleep(500)
#display.show(Image.ARROW_E)
#sleep(500)
#display.scroll("B to move", delay=100)

last_time = running_time()
a_pressed = False
b_pressed = False
draw_display()

won = False
while not won:
	# Animation
	new_time = running_time()

	if showing_arrow:
		if new_time > last_time + 1000:
			draw_display()
			showing_arrow = False
			last_time = new_time
	else:
		if new_time > last_time + 100:
			next_flash = get_flash()

			for y in range(5):
				for x in range(5):
					x2 = x - 2 + player[0]
					y2 = y - 2 + player[1]
					if not x2 in range(maze.width()) or not y2 in range(maze.height()):
						continue

					pixel = maze.get_pixel(x2, y2)
					if pixel == START or pixel == FINISH:
						display.set_pixel(x, y, next_flash)
			last_time = new_time

	a_is_pressed = button_a.is_pressed()
	b_is_pressed = button_b.is_pressed()

	skip_draw = False
	if a_is_pressed and not a_pressed:
		turn()
	elif b_is_pressed and not b_pressed:
		if advance():
			# Redraw the display because we've moved
			draw_display()
			won = maze.get_pixel(player[0], player[1]) == FINISH
	a_pressed = a_is_pressed
	b_pressed = b_is_pressed

display.scroll("Solved in " + str(running_time() // 1000) + "s!", delay=100)

# Ask the user to reset to play again
display.scroll("Reset", delay=100)
while True:
	display.show(Image.ARROW_NW)
	sleep(500)
	display.clear()
	sleep(500)
