from random import*
r=randint
e="%s"*3%(r(1,10),choice("+-*"),r(1,10))
print("NY"[int(input(e))==eval(e)])