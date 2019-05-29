#!/usr/bin/env python3
# FIXME doesn't seem to choose the correct bitrate
import sys, json, numbers, os
from subprocess import Popen, PIPE, DEVNULL, call

MAX_FILE_SIZE = 8_388_127

FFPROBE_CMD = (
    "ffprobe",
    "-show_entries", "stream=width,height,r_frame_rate,bit_rate,duration",
    "-print_format", "json",
)

AUDIO_REF = 384_000
FRAMERATE_REF = 30
VIDEO_FACTORS = (
    (0, 0),
    (360, 1_000_000 / AUDIO_REF),
    (480, 2_500_000 / AUDIO_REF),
    (720, 5_000_000 / AUDIO_REF),
    (1080, 8_000_000 / AUDIO_REF),
)

def get_video_factor(height):
    # Interpolate
    for i in range(1, len(VIDEO_FACTORS)):
        prev = VIDEO_FACTORS[i-1]
        cur = VIDEO_FACTORS[i]

        if height <= cur[0]:
            return prev[1] + (height - prev[0]) \
                    / (cur[0] - prev[0]) \
                    * (cur[1] - prev[1])

    # Extrapolate
    return (height / VIDEO_FACTORS[-1][0]) * VIDEO_FACTORS[-1][1]

def get_info(filename):
    ffprobe = Popen(FFPROBE_CMD + (filename,), stdout=PIPE, stderr=DEVNULL)
    info = json.loads(ffprobe.communicate()[0], encoding=sys.stdout.encoding)["streams"]

    info[0]["audio_bit_rate"] = info[1]["bit_rate"]
    info = info[0]

    for key in info:
        if not isinstance(info[key], numbers.Real):
            parts = info[key].split("/", 1)
            d = 1
            try:
                n = float(parts[0])
                if len(parts) == 2: d = float(parts[1])
            except ValueError:
                continue

            info[key] = n / d
    return info

if __name__ == "__main__" and len(sys.argv) >= 2:
    ifile = sys.argv[1]
    ofile = sys.argv[2] if len(sys.argv) >= 3 else ifile

    if os.path.getsize(ifile) <= MAX_FILE_SIZE:
        print(ifile, "is already below", MAX_FILE_SIZE, "bytes")
        sys.exit()

    info = get_info(sys.argv[1])
    video_factor = get_video_factor(info["height"])

    total_bitrate = (MAX_FILE_SIZE * 0.98) / info["duration"]
    audio_bitrate = total_bitrate / (1 + video_factor)
    video_bitrate = total_bitrate - audio_bitrate

    print(total_bitrate, audio_bitrate, video_bitrate)
    input()

    ffmpeg = call(("ffmpeg", "-i", ifile,
            "-c:v", "libvpx-vp9", "-c:a", "libopus",
            "-crf", "0", "-b:v", str(video_bitrate * 0.75),
            "-maxrate", str(video_bitrate),
            "-b:a", str(audio_bitrate),
            "-fs", str(MAX_FILE_SIZE),
            ofile))
