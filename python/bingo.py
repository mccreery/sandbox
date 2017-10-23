import random

NUMBERS_ON_GRID = 10
BOARD_WIDTH = 5
BOARD_HEIGHT = 5

special = {1: "At the beginning", 4: "Knock at the door", 5: "Man alive", 7: "Lucky seven",
    13: "Unlucky for some", 66: "Clickety click", 88: "Two fat ladies"}

def validate(board, value):
    try:
        value = int(value)
        if value >= 1 and value <= len(board):
            return value
        else:
            return -1
    except ValueError:
        return -1

def get_number_msg(no, ignore_special=False):
    if not ignore_special and no in special.keys():
        return special[no] + ", " + get_number_msg(no, True)
    else:
        return "#%d!" % no

def draw_grid(values, width, cell_width=4, line="-", bar="|", edge="+"):
    for y in range(len(values) // width):
        print((edge + line * cell_width) * width + edge)
        for x in range(width):
            print(bar + values[y * width + x].ljust(cell_width), end="")
        print(bar)
    print((edge + line * cell_width) * width + edge)

board = list("" for i in range(BOARD_WIDTH * BOARD_HEIGHT))
for i in range(NUMBERS_ON_GRID):
    n = random.randrange(len(board))
    while board[n] != "":
        n = random.randrange(len(board))
    board[n] = str(n + 1)

tries = 0

while sum(len(i) > 0 and not i.endswith("X") for i in board) > 0:
    draw_grid(board, BOARD_WIDTH)
    i = -1
    while i == -1:
        i = validate(board, input("What number has been called? "))
    print(get_number_msg(i))
    i -= 1
    if not board[i].endswith("X"):
        board[i] = board[i].ljust(3) + "X"
    tries += 1
draw_grid(board, BOARD_WIDTH)

print("Bingo! It took you %d called numbers to win." % tries)
if tries == NUMBERS_ON_GRID: print("Did you cheat?")