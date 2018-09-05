.PHONY: all clean

all: build build/resources.o build/gameboy.o
	gcc build/gameboy.o build/resources.o -o build/gameboy -lgdi32 -mwindows

build:
	mkdir -p build

build/resources.o: resources.h build/icon.ico
	windres resources.rc -O coff -o build/resources.o

icons := $(wildcard img/icon/*.png)
build/icon.ico: $(icons)
	magick convert $(icons) build/icon.ico

build/gameboy.o: gameboy.c
	gcc -Wall -DUNICODE -D_UNICODE -c gameboy.c -o build/gameboy.o -std=c99 -Os

clean:
	rm -rf build
