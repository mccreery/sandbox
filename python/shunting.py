#Shunting yard algorithm for strings

import math

prec = {
	"*": 1,
	"+": 0,
	"-": 0,
	"/": 1,
	"^": 1
}
rass = {
	"*": False,
	"+": False,
	"-": False,
	"/": False,
	"^": True
}
funcs = ["sin", "cos", "tan"]

def shunting_yard(expr):
	output = []
	operators = []

	for token in expr.split():
		try:
			output.append(float(token))
		except:
			if token == ",":
				while operators[-1] != "(":
					output.append(operators.pop())
			elif token == "(":
				operators.append(token)
			elif token == ")":
				while operators[-1] != "(":
					output.append(operators.pop())
				operators.pop()
				if len(operators) != 0 and operators[-1] in funcs:
					output.append(operators.pop())
			else:
				if not token in funcs:
					while len(operators) != 0 and operators[-1] in prec and (
							prec[token] < prec[operators[-1]] if rass[token]
							else prec[token] <= prec[operators[-1]]):
						output.append(operators.pop())
				operators.append(token)
	output += operators[::-1]
	return output

x = input()
while x:
	rpn = shunting_yard(x)
	print(rpn)

	stack = []
	for token in rpn:
		if type(token) is float:
			stack.append(token)
		elif token in prec:
			b = stack.pop()
			a = stack.pop()

			if   token == "*": stack.append(a * b)
			elif token == "+": stack.append(a + b)
			elif token == "-": stack.append(a - b)
			elif token == "/": stack.append(a / b)
			elif token == "^": stack.append(a ** b)
		elif token in funcs:
			a = stack.pop()
			if   token == "sin": stack.append(math.sin(a))
			elif token == "cos": stack.append(math.cos(a))
			elif token == "tan": stack.append(math.tan(a))

	print(str(rpn) + " = " + str(stack[0]))
	x = input()
