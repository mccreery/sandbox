import re, random

width = 15
height = 20
nameLength = 5

words = list(list("" for i in range(width)) for i in range(height))

for x in range(height):
    for y in range(width):
        while re.search("[aeiouy].*[aeiouy]", words[x][y]) is None:
            words[x][y] = ''.join(chr(random.randint(ord('a'), ord('z'))) for i in range(nameLength)).title()

lineSep = "\n" + "-" * (width * (nameLength + 3) - 3) + "\n"
print(lineSep.join(" | ".join(line) for line in words))