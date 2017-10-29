import string

def parse_address(addr):
	flags = None
	addr = addr[:addr.index("@")]

	whitelist = True
	for i in range(len(addr)):
		if addr[i] in "+-":
			whitelist = addr[i] == "+"
			flags = addr[i:]
			addr = addr[:i]
			break

	if not flags: print("Mail to all: " + addr)
	else:
		print("Whitelist mode" if whitelist else "Blacklist mode", flags)
		i = 0
		while i < len(flags):
			if flags[i] in "+-":
				print("Add ", end="")
				i += 1
				j = i
				while j < len(flags) and flags[j] in string.ascii_letters + string.digits:
					j += 1
				print("Name " + flags[i:j], end="")

				i = j
				if i < len(flags) and flags[i] == "=":
					i += 1
					j = i
					while j < len(flags) and flags[j] in string.ascii_letters + string.digits:
						j += 1
					print(" = " + flags[i:j], end="")
				print()
			else: i += 1

parse_address("sam@mail.com")
parse_address("sam+f@my.net")
parse_address("mail.list-year=5+flag@site.net")
