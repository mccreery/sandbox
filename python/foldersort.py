#!/usr/bin/env python
"""foldersort.py: Sorts folders by size in a given directory."""

import os, sys, time, ctypes
from operator import itemgetter

def getSize(startPath="."):
    """Totals up the size of an entire directory using os.walk."""
    global averageTime
    global count

    totalSize = 0
    for dirpath, dirnames, filenames in os.walk(startPath):
        for f in filenames:
            totalSize += os.path.getsize(dirpath + os.sep + f)
    return totalSize

def no_format(x):
	if type(x) is float:
		x = round(x, 2)
	return "{:,g}".format(x)

symbols = (" KiB", " MiB", " GiB", " TiB")
limit_high = 1 << (10 * len(symbols))

def format_size(length):
	limit = limit_high
	for i in range(len(symbols) - 1, -1, -1):
		if length >= limit:
			return no_format(length / limit) + symbols[i]
		limit >>= 10
	return no_format(length) + " B  "

dir = sys.argv[1] if len(sys.argv) > 1 else "."

# Bail out if the directory doesn't exist, to prevent any errors.
if not os.path.isdir(dir):
    print("Directory %s is not a valid directory." % dir)
    input("Press Enter to continue...")
    sys.exit()

folderGenerator = ((getSize(dir + os.sep + folder), folder) for folder in os.listdir(dir) if os.path.isdir(dir + os.sep + folder)) # Generator to loop through all the folders in the start directory

folders = []
length = len(list(folder for folder in os.listdir(dir) if os.path.isdir(dir + os.sep + folder))) # Quick generator without file size to count the total before doing the process.
# Loop through all the folders and update the progress bar
for i in range(length):
    try:
        folders.append(next(folderGenerator))
        print("Loading [" + ("." * ((i % 3) + 1)).ljust(3) + "] (" + str(i + 1) + " out of " + str(length) + ") [          ]", end = "\r" if i < length - 1 else "\n")
    except:
        continue

# Sort folders
sortedSizes = sorted(folders, key = itemgetter(0), reverse = True)

folderList = []
for i in sortedSizes:
    folderList.append(format_size(i[0]).rjust(15) + " (" + str(no_format(i[0])) + " bytes): " + i[1])

# Finally print out all the results
print("Subdirectories, in order of size:")
print(dir, "(ROOT)")
for c in folderList:
    print("\t> " + c)
