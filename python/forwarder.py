import http.server

redirects = {
	"y": "http://youtube.com",
	"t": "http://twitter.com"
}

PAGE_404 = """<!DOCTYPE html>
<html>
	<head>
		<meta charset="utf-8">
		<title>404 Not Found</title>
	</head>
	<body>
		Nothing to see here, gentlemen.
	</body>
</html>
""".encode("utf-8")

class RecordRequest(http.server.BaseHTTPRequestHandler):
	def do_GET(self):
		if self.path[0] == "/":
			self.path = self.path[1:]

		if self.path in redirects:
			self.send_response(307)
			self.send_header("Location", redirects[self.path])
			self.end_headers()
		else:
			self.send_response(404)
			self.send_header("Content-Type", "text/html; charset=utf-8")
			self.send_header("Content-Length", len(PAGE_404))
			self.end_headers()

			self.wfile.write(PAGE_404)

httpd = http.server.HTTPServer(('127.0.0.1', 80), RecordRequest)

sa = httpd.socket.getsockname()
print("Serving HTTP on", sa[0], "port", sa[1], "...")
httpd.serve_forever()
