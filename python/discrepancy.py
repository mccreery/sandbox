import sys, os, magic
wizard = magic.Magic(magic_file=r"C:\utils\file\share\misc\magic", mime=True)

TYPES = {
	"image/png": "png",
	"image/jpeg": ("jpg", "jpeg"),
	"video/mp4": "mp4",
	"image/gif": "gif",
	"image/webp": "webp",
	"image/svg+xml": "svg"
}

def type_check(ext, mime):
	exts = TYPES[mime]
	if hasattr(exts, "__iter__"):
		return ext in exts
	else:
		return ext == exts

start_in = sys.argv[1] if len(sys.argv) > 1 else "."

for root, branches, leaves in os.walk(start_in):
	for file in leaves:
		file = os.path.join(root, file)
		ext = file[file.rfind(".")+1:].lower()

		if ext:
			mime = wizard.from_file(file)
			#print(file, ext, mime)

			if mime in TYPES:
				if not type_check(ext, mime):
					print(file, "is actually of type", mime)
			#elif mime != "application/octet-stream":
			#	print(file, "has unknown MIME type", mime)

input("Done.")
