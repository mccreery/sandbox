#!/usr/bin/env python3
"""Corrupts text files by repeatedly encoding them as UTF-8 and interpreting it as CP1252"""

import sys, io, os

def do(file, times=1):
	"""Corrupt the file the given number of times, creating mojibake errors"""
	if isinstance(file, io.TextIOBase):
		text = file.read()

		for i in range(times):
			text = text.encode("utf_8").decode("cp1252")

		file.seek(0)
		file.write(text)

if __name__ == "__main__":
	if len(sys.argv) > 2:
		with open("pound.txt", "r+") as file:
			do(file, int(sys.argv[2]) if len(sys.argv) > 2 else 1)
	else:
		print("Usage: {} file [iterations]".format(os.path.basename(__file__)), file=sys.stderr)
