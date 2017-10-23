import string, random, argparse

INVALID_NATURAL = lambda x: "{} is not a positive number".format(x)
DEFAULT_CHARSET = string.ascii_lowercase + string.ascii_uppercase \
	+ string.digits + "_-"
parser = argparse.ArgumentParser()

def check_natural(x):
	x = int(x)
	if x <= 0:
		raise argparse.ArgumentTypeError(INVALID_NATURAL(x))
	return x

# General options #
parser.add_argument("-l", "--length", default=10, type=check_natural)
parser.add_argument("-n", "--number", default=1,  type=check_natural)

# Character set customisation #
parser.add_argument("-c", "--charset", action="append")
parser.add_argument("--lower", "--lowercase",
	dest="charset", action="append_const", const=string.ascii_lowercase)
parser.add_argument("--upper", "--uppercase",
	dest="charset", action="append_const", const=string.ascii_uppercase)
parser.add_argument("--digits",
	dest="charset", action="append_const", const=string.digits)

args = parser.parse_args()

args.charset = "".join(args.charset) \
	if args.charset else DEFAULT_CHARSET

max_number = len(args.charset) ** args.length
if args.number > max_number:
	print("There {} only {} possible {}-character string{} with the current character set. No more will be generated." \
		.format("is" if max_number == 1 else "are", max_number,
			args.length, "" if max_number == 1 else "s"))
	args.number = max_number

def next_password():
	return "".join(random.choice(args.charset) \
		for i in range(args.length))

passwords = []
for i in range(args.number):
	password = next_password()
	while password in passwords:
		password = next_password()

	passwords.append(password)
	print(password)
input()
