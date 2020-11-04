import http.server, requests, json

username_cache = {}
whitelist = ("current.html", "runner64.png", "flags.min.css", "flags.png")
mimes = {
	"html": "text/html",
	"css": "text/css",
	"png": "image/png",
	"json": "application/jsonn"
}

PAGE_404 = """<!DOCTYPE html>
<html>
	<head>
		<meta charset="utf-8">
		<title>404 Not Found</title>
	</head>
	<body>
		nuthin to see here bruh
	</body>
</html>
"""

HEADERS     = {"User-Agent": "Jobicade's Magnificent PB/WR grabber"}

GAME_ID     = "4pd0n31e"
CATEGORY_ID = "wk6pexd1"
USER_ID     = "48g0ln2x"

BASE        = "http://www.speedrun.com/api/v1/"
PB_QUERY    = BASE + "users/" + USER_ID + "/personal-bests"
WR_QUERY    = BASE + "leaderboards/" + GAME_ID + "/category/" + CATEGORY_ID

class RecordRequest(http.server.BaseHTTPRequestHandler):
	@staticmethod
	def json(url, path=None, **kwargs):
		root = requests.get(url, **kwargs, headers=HEADERS).json()

		if path:
			for key in path:
				root = root[key]
		return root

	def do_GET(self):
		if self.path[0] == "/":
			self.path = self.path[1:]

		headers_out = {}
		code = 200

		redirects = ("current", "current.html", "index", "index.html")
		if self.path in redirects:
			code = 301
			headers_out["Location"] = "/"
			self.path = "current.html"

		# This is the correct path, don't complain about it
		if self.path == "": self.path = "current.html"

		if self.path == "data":
			headers_out["Content-Type"] = "application/json; charset=utf-8"

			# Grab responses from speedrun.com...
			pb = RecordRequest.json(PB_QUERY, ("data", 0), params={"game": GAME_ID, "category": CATEGORY_ID})
			wr = RecordRequest.json(WR_QUERY, ("data", "runs", 0, "run"), params={"top": 1, "timing": "realtime_noloads"})

			wr_runner = wr["players"][0]
			if not wr_runner["id"] in username_cache:
				username_cache[wr_runner["id"]] = RecordRequest.json(wr_runner["uri"], ("data",))

			pb["run"]["place"] = pb["place"]
			pb = pb["run"]
			wr["player"] = username_cache[wr_runner["id"]]

			response = json.dumps({"pb": pb, "wr": wr}).encode("utf-8")
		elif self.path in whitelist:
			headers_out["Content-Type"] = mimes[self.path[self.path.rfind(".")+1:]]

			with open(self.path, "rb") as f:
				response = f.read()
		else:
			code = 404
			response = PAGE_404.encode("utf-8")
			headers_out["Content-Type"] = "text/html; charset=utf-8"
			headers_out["Content-Length"] = len(response)

		self.send_response(code)
		for key in headers_out:
			self.send_header(key, headers_out[key])
		self.end_headers()

		self.wfile.write(response)

httpd = http.server.HTTPServer(('127.0.0.1', 80), RecordRequest)

sa = httpd.socket.getsockname()
print("Serving HTTP on", sa[0], "port", sa[1], "...")
httpd.serve_forever()
