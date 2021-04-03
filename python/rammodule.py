#!/usr/bin/env python3
# RAM Downloader :^)
import sys, random, datetime, socket, time

FILE_NAMES = ("Sys", "BufferObject", "Object", "Exception", "Initialisation",
	"GL", "NGL", "RM", "Append", "Download",
	"Authenticate", "Active", "Algorithm", "Compute", "Calculation",
	"RAM", "Disk", "Alert", "Applet", "GUI",
	"Array", "Pointer", "ASCII", "Drive", "Compilation",
	"Network", "Bitwise", "Boolean", "Operator", "Bit")

PATHS = (
	r"C:\Windows\MemoryData",
	r"C:\Windows\UserData\Downloading",
	r"C:\temp",
	r"D:\Cache",
	r"C:\Users\Default",
	r"C:\Windows\Config"
)
FOLDERS = ("sys", "setup", "RAM", "Module", "bin", "src", "source", "recomp", "en_")
EXCEPTIONS = ("MEMORY_VIOLATION", "Exception", "LookupError", "BufferError", "COMPILATION_FAILURE")
EXTENSIONS = ("dat", "b", "c", "h", "bin", "jar", "bat", "ram", "py", "exe", "rar")
LOG_LEVELS = ("INFO", "WARNING", "ERROR")

INIT_MSG = "Initialising ver. 1.12_w16i1a_dev"
PAD = " " * 20

def randbool(): return random.random() < 0.5

def log(msg, level=0):
	print("[STDOUT]{{{}}} {}: {}".format(datetime.datetime.now().strftime("%X"), LOG_LEVELS[level], msg))

def filename():
	name = "".join("_"*randbool() + random.choice(FILE_NAMES) for i in range(random.randint(0, 3)))
	if randbool():
		name += randbool()*"_" + str(random.randint(0, 257))

	return ".".join((name, random.choice(EXTENSIONS)))

def loadBar(current, total):
	size = int((current / total) * 10)
	return "[" + ("=" * size) + ("-" * (10-size)) + "]"

if sys.platform != "win32":
	print("Downloading RAM using this utility only works on Windows operating systems.")
	print("Press enter to continue...")

	input()
	sys.exit()

ip = socket.gethostbyname(socket.gethostname())

for i in range(4):
	print(INIT_MSG + "."*i, end="\r")
	time.sleep(0.5)

print("Initialisation complete.".ljust(len(INIT_MSG)+3))
time.sleep(1)
print("Pushing changes to disk... This may take several minutes.")

path = PATHS[0]
indent = 0

op_count = random.randint(150, 500)
for i in range(op_count):
	choice = random.random()

	if choice < 0.8:
		message = "Creating" if choice < 0.6 else "Removing"
		message = "> {} file {} in directory {}".format(message, filename(), path)

		print(message.rjust(len(message) + indent*2))
	elif choice < 0.95:
		message = "Formatting data" if choice < 0.9 else "Writing to disk"

		for load in range(4):
			print(message + ("." * load), end="\r")
		print((message + " complete"))
	else:
		print("End of directory.", "-" * 40)
		print("Creating diretory " + path + "\\" + random.choice(FOLDERS))

	if random.random() < 0.1: path = random.choice(PATHS)
	if random.random() < 0.05:
		log(random.choice(EXCEPTIONS), 2)
		log("If this error occurs more than ten times, the operation may fail.")

		if random.random() < 0.25:
			print("Bit check:")
			for b in range(random.randint(1, 10)):
				print(randbool(), end="\r" if randbool() else "\n")
				time.sleep(0.1)
			log("Error has been resolved.")

		log("Excessive errors may cause problems, but should be expected with an older system.", 1)

	indent = min(10, max(0, indent + random.randint(-1, 1)))

	# Display completion bar
	print("%f%% complete (%d out of %d operations). %s" % (int((i / op_count) * 1000) / 10, i, op_count, loadBar(i, op_count) + PAD), end="\r")

	delay = random.random()
	time.sleep(delay * delay)

print("Setup complete.".ljust(65))

for i in range(5):
	print("Localising network connection" + "."*i + PAD, end="\r")
	time.sleep(random.random() / 5)
log("Most systems have a maximum memory capability of 16 or 32GB of RAM. Going over this limit could cause instability.")

print("\nTesting network connection...")
time.sleep(.5)

print("-"*40)
internet = False
i = 1
while not internet:
	print("Attempting connection (" + str(i) + " tries)")
	i += 1
	time.sleep(random.random() * 2)
	if randbool():
		print("Network connection established.")
		print("-"*40)
		internet = True
	else:
		print("No internet connection is available. You may want to try again in a couple of hours, or contact a network administrator.")
for i in range(64):
	print("Pinging data connection to %s (%d out of 64 bytes)%s" % (ip, i, PAD), end="\r")
	time.sleep(.01)

print("\n%dms server response.\n" % random.randint(500, 5000))

for message in ["Clearing data cache", "Finalising data transfers", "Resetting current memory", "Scanning system"]:
	for i in range(4):
		print(message + "."*i + PAD, end="\r")
		time.sleep(random.random())
	print()

print("\nReady to begin downloading RAM to the system memory.")

gigs = 1000
while gigs > 32 or gigs <= 0:
	print("Please enter a numeric value less than or equal to 32.")
	try:
		gigs = int(input("How many gigabytes of data would you like to download (suggested 4)? "))
	except:
		gigs = 1000

y = gigs * 1024
print("\nDownloading %d gigabytes of RAM (%d meyabytes)" % (gigs, y))
indent = 0

for i in range(y):
	# Display completion bar
	if i % 100 == 0:
		print(("%s> Byte transfer sample: %dms" % ("  "*indent, random.randint(50,150))).ljust(65))

	if i % 1024 == 0:
		print("-" * 40)
		print("Byte transfers completed for gigabyte.")
		for x in range(4):
			print("Seating module" + ("." * x) + " " * 40, end="\r")
			time.sleep(random.random())
		print()
		for p in range(3):
			for x in range(4):
				print("Reseating module" + ("." * x) + " " * 40, end="\r")
				time.sleep(random.random())
			print()

	if random.random() > .9:
		log(random.choice(EXCEPTIONS).ljust(65), 2)
		log("If this error occurs more than ten times, the operation may fail.")

	print("%f%% complete (%d out of %d operations). %s" % (int((i / y) * 1000) / 10, i, y, loadBar(i, y) + PAD), end="\r")

	time.sleep(random.random() / 200)
	a = random.random()
	if a <= .05:
		time.sleep(random.random() / 5)
	elif a >= .95:
		time.sleep(random.random() / 20)

	if random.random() <= .1:
		if randbool(): indent += 1
		else: indent -= 1
		if indent < 0: indent = 0
		elif indent > 10: indent = 10

print()
for i in range(4):
    print("Finalising" + "."*i + PAD, end="\r")
    time.sleep(random.random())
print()

print("RAM download complete. Please restart your system." + PAD)
input("Press enter to continue...")
