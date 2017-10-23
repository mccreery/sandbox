import csv, requests, os
from PIL import Image

FLAG_SIZE = 16

def convert_box(*box):
	return (box[0], box[1], box[0]+box[2], box[1]+box[3])

CSS = """.flag {{
	display: inline-block;
	width: {}px;
	height: {}px;
	background-image: url(sheet.png);
}}

""".format(FLAG_SIZE, FLAG_SIZE)

dimension = 16
flag_max = dimension * dimension
flag_count = 0
successful_flags = []

with open("flags.csv", "r") as file:
	rows = iter(csv.reader(file))
	next(rows)

	sheet = Image.new("RGBA", (dimension * FLAG_SIZE, dimension * FLAG_SIZE))

	for row in rows:
		# Download flag
		filename = row[1].lower() + ".png"
		response = requests.get("http://www.speedrun.com/images/flags/{}.png".format(row[1].lower()))
		if response.status_code == 404: continue

		with open(filename, "wb") as flag:
			for chunk in response.iter_content(chunk_size=128):
				flag.write(chunk)

		# Copy flag
		image = Image.open(filename)
		height = FLAG_SIZE * image.height // image.width

		sheet.paste(image.resize((FLAG_SIZE, height), Image.LANCZOS), (
			(flag_count%dimension)*FLAG_SIZE,
			(flag_count//dimension)*FLAG_SIZE + (FLAG_SIZE-height) // 2
		))

		successful_flags.append(row[1].lower())

		# Delete flag again
		os.remove(filename)

		# Resize if too big
		flag_count += 1
		if flag_count >= flag_max:
			dimension <<= 1
			flag_max <<= 2

			new_sheet = Image.new("RGBA", (dimension * FLAG_SIZE, dimension * FLAG_SIZE))
			for i in range(dimension >> 1):
				new_sheet.paste(sheet.crop(convert_box(0, i*FLAG_SIZE, sheet.width, FLAG_SIZE)),
					convert_box((i&1)*sheet.width, (i>>1)*FLAG_SIZE, sheet.width, FLAG_SIZE))

			sheet = new_sheet

	sheet.save("sheet.png")

with open("flags.css", "w") as css:
	css.write(CSS)

	for i in range(len(successful_flags)):
		css.write(".flag.{} {{background-position: -{}px -{}px}}\n".format(
			successful_flags[i], (i%dimension) * FLAG_SIZE, (i//dimension) * FLAG_SIZE))
