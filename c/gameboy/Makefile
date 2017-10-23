all: resources.o gameboy.o
	gcc gameboy.o resources.o -o gameboy.exe -lgdi32 -mwindows

resources.o: resources.h img/icon.ico
	windres resources.rc -O coff -o resources.o

icons := $(wildcard img/*.png)
img/icon.ico: $(icons)
	magick convert $(icons) icon.ico

gameboy.o: gameboy.c
	gcc -c gameboy.c -std=c99 -Os
