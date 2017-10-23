import os, sys, re

if len(sys.argv) < 3:
	print("Please enter a search and replace pattern.")
else:
	search  = re.compile(sys.argv[1])
	print("Replacing \"{}\" with \"{}\"".format(sys.argv[1], sys.argv[2]))
	replaces = []

	for dir, dirs, files in os.walk("."):
		for file in files:
			file = os.path.join(dir, file)[2:]
			replaced = search.sub(sys.argv[2], file)

			if replaced != file:
				replaces.append((file, replaced))

	if len(replaces) != 0:
		print('\n'.join("\"{}\" -> \"{}\"".format(x[0], x[1]) for x in replaces))
		if input("\nApply changes?").lower() in ("yes", "y"):
			for x in replaces:
				os.rename(x[0], x[1])
	else:
		print("No matches found.")
