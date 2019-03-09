#!/usr/bin/env sh

magick convert "$1" -resize 240x320 \
    -channel R +level 0,7 -evaluate multiply 32 +channel \
    -channel G +level 0,7 -evaluate multiply 4 +channel \
    -channel B +level 0,3 +channel \
    -channel RGB -separate +channel \
    -channel R -evaluate-sequence add +channel \
    -depth 8 R:image.bin
