import random, math

width = 2
height = 2

numbers = []
for i in range(25):
    numbers.append(random.randint(0, 99))
print(numbers)

for y in range(0, ((height + 1) * 5) + 1):
    for x in range(0, ((width + 1) * 5) + 1):
        if x % (width + 1) == 0 and y % (height + 1) == 0:
            print("+", end = "")
        elif x % (width + 1) == 0:
            print("|", end = "")
        elif y % (height + 1) == 0:
            print("-", end = "")
        else:
            index = math.ceil(y / (height + 1)) * 5 + math.ceil(x / (width + 1)) - 6
            if len(numbers) > index:
                print(numbers[index], end = "") #" "
            else:
                print(" ", end = "")
    print()