import math

def prime(n):
    if n <= 1: return False
    if n % 2 == 0 and not n == 2: return False
    for i in range(3, math.ceil(math.sqrt(n)) + 1):
        if n / i == n // i: return False
    return True

for i in range(1, 10000):
    if prime(i): print(i, end=", ")


#print(prime(46367645777777))