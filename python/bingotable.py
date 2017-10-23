import random, math, time, os

width = 10
height = 10

numbers = []
for i in range(width * height):
    numbers.append(random.randint(1, 99))

def printTable(width, height, numbers):
    for y in range(0, (height * 2) + 1):
        for x in range(0, (width * 2) + 1):
            if not x % 2:
                if not y % 2:
                    print("+", end = "")
                else:
                    print("|", end = "")
            elif not y % 2:
                print("----", end = "")
            else:
                index = math.ceil((y - 1) / 2) * 5 + math.ceil((x - 1) / 2)
                print(" " + str(numbers[index]).rjust(2), end = " ")
        print()

for i in range(10):
    #"\033[0;0H"
    printTable(width, height, numbers)
    number = random.randint(1, 99)
    if number in numbers:
        numbers = ["##" if number == b else b for b in numbers]
    time.sleep(1)

input()