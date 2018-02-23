mkdir segments
ffmpeg -i "$1" -f segment -b:a 64k -segment_time 30:00 -map a segments/"$1"-%02d.mp3
