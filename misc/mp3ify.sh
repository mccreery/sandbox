#!/usr/bin/env bash

shopt -s globstar
shopt -s nullglob

losslessroot=${1:-FLAC}
lossyroot=${2:-MP3-320}
#lossyroot=${2:-OPUS-128}

echo Mirroring from \"$losslessroot\" to \"$lossyroot\"
skipped=0

skiptotal=0
mirrortotal=0

function resume {
    if [ $skipped -gt 0 ]
    then
        echo Skipped a group of $skipped files.
        skiptotal=$((skiptotal+skipped))
        skipped=0
    fi
}

function tryconvert {
    if [ -e "$dest" ]; then return 1; fi
    resume
    echo Starting \"$dest\"

    ffmpeg -n -loglevel error -i "$src" -i "$srcdir/Folder.jpg" \
        -c:a libmp3lame -b:a 320k -ar 44100 -map 0:0 \
        -id3v2_version 3 -c:v copy -map_metadata 0 -map 1:0 \
        "$dest"

    #ffmpeg -n -loglevel error -i "$src" -i "$srcdir/Folder.jpg" \
    #    -c:a libopus -b:a 128k -map 0:0 \
    #    "$dest"
}

for src in "$losslessroot"/**/*.flac; do
    dest=$lossyroot/$(temp=${src#$losslessroot/}; echo ${temp%.flac}).mp3
    #dest=$lossyroot/$(temp=${src#$losslessroot/}; echo ${temp%.flac}).opus

    srcdir=$(dirname "$src")
    destdir=$(dirname "$dest")

    mkdir -p "$destdir"

    if [ ! -e "$destdir/Folder.jpg" ]; then
        resume
        cp -u "$srcdir/Folder.jpg" "$destdir" && echo Artwork copied.
    fi

    if tryconvert; then
        echo $dest complete.
        ((++mirrortotal))
    else
        echo -ne Skipped $((++skipped)) files...\\r
    fi
done
resume

echo Totals: Mirrored $mirrortotal, skipped $skiptotal
