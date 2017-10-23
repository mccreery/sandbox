import random

def get_val(msg):
    difficulty = input(msg)
    try:
        difficulty = int(difficulty)
        if difficulty > 0:
            return difficulty
        else:
            return get_val(msg)
    except ValueError:
        return get_val(msg)

difficulty = get_val("Difficulty? ")
print("Sequences will be %d character%s long" % (difficulty, "" if difficulty == 1 else "s"))

correct_digits = 0
guessed = 0

number = "".join(random.choice("123456789") for i in range(difficulty))
while correct_digits < difficulty:
    guess = str(get_val("Guess? ")).rjust(difficulty, "0")
    guessed += 1

    correct_digits = sum(number[i] == guess[i] for i in range(difficulty))
    print(guess + ":", "*" * correct_digits)

print("Took %d guess%s" % (guessed, "" if guessed == 1 else "es"))