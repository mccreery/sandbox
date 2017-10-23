# Test question for interviews:
# Read a file line by line (where each line contains a single integer), and count up all the numbers.

def count(file):
    f = open(file)
    total = 0
    a = f.readline()
    while a:
        total += int(a)
        a = f.readline()
    return total

print("\n".join(">\t" + a for a in open("file.txt").read().splitlines()))
print(count("file.txt"))