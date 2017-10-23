import gzip, os, sys
EXT = ".gz"

for src_path in sys.argv[1:]:
	if not os.path.isfile(src_path): continue

	if src_path.endswith(EXT): # Unzipping
		reader, writer = gzip.open, open
		dst_path = src_path[:-len(EXT)]
	else: # Zipping
		reader, writer = open, gzip.open
		dst_path = src_path + EXT

	with reader(src_path, "rb") as src:
		with writer(dst_path, "wb") as dst:
			dst.write(src.read())
	os.remove(src_path)
