import random

top_score = 0
print("*** Drop Dead ***")

option = "yes"
while option.lower() in ["y", "yes"]:
    dice_count = 5
    total_score = 0
    while dice_count > 0:
        dice = list(random.randint(1, 6) for i in range(dice_count))
        lost = 0
        for die in dice:
            if die in [2, 5]:
                lost += 1
        score = sum(dice) if lost == 0 else 0
        total_score += score
        dice_count -= lost
        print(dice, "scored", score, "and lost", lost, "die" if lost == 1 else "dice")
    print("Your final score was", total_score)
    if total_score > top_score:
        top_score = total_score
    print("Your best score is", top_score)
    option = input("Would you like to play again? ")

# #################################### #

import random
d = [0]*5
t = 0
while len(d) > 0:
    d = list(random.randint(1, 6) for i in range(len(d)))
    s = sum(d)
    print(d, end=": ")
    for i in d:
        if i in [2, 5]:
            d.remove(i)
            s = 0
    t += s
    print(t)