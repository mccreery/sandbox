#    ____ ____
#   ||W |||E ||
#   ||__|||__||
# __|/__\|/__\|
#||A |||S ||____
#||__|||__|||D ||
#|/__\|/__\||__||

def generate_message(message):
	msg = len(message)*" ____" + "\n"
	for c in message:
		msg += "||" + c + " |"
	msg += "|\n" + len(message)*"||__|" + "|\n" + len(message)*"|/__\\" + "|\n"
	return msg

print(generate_message("Hello, world!"))
