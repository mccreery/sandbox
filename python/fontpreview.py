import sys, os
from PIL import Image

CHARSET_PATH = sys.argv[1]
OUTPUT_PATH = os.path.splitext(CHARSET_PATH) + "-preview.png"

charset = Image.open(CHARSET_PATH)
tile = (charset.width // 16, charset.height // 16)
print(tile)

TEXT = ' '.join(sys.argv[2:]).split('\n')
max_width = 0
for line in TEXT:
	if len(line) > max_width:
		max_width = len(line)

output = Image.new("RGBA", (tile[0] * max_width, tile[1] * len(TEXT)), (255, 255, 255, 0))

dest = [0, 0, tile[0], tile[1]]

for l in range(len(TEXT)):
	for c in range(len(TEXT[l])):
		x = tile[0] * (ord(TEXT[l][c])&15)
		y = tile[1] * (ord(TEXT[l][c])//16)
		source = (x, y, x+tile[0], y+tile[1])

		output.paste(charset.crop(source), dest)
		dest[0] += tile[0]
		dest[2] += tile[0]
	dest[0] = 0
	dest[1] += tile[1]
	dest[2] = tile[0]
	dest[3] += tile[1]

output.save(OUTPUT_PATH)
