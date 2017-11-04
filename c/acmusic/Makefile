targets = $(wildcard src/*.c)
output = acmusic.exe

acmusic.exe: $(targets)
	gcc -Iinclude -std=c99 $(targets) -o $(output)
