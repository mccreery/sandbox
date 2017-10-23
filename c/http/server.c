#include "server.h"
#include "../stringbuilder/stringbuilder.h"
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <stdio.h>
#include <unistd.h>
#include <memory.h>
#include <stdlib.h>
#include <errno.h>
#include <stdint.h>

int main(int argc, char **argv) {
	struct sockaddr_in host, peer;
	host.sin_family = AF_INET;

	host.sin_addr.s_addr = INADDR_ANY;
	host.sin_port = htons(PORT);

	int sock, binding;
	if((sock = socket(AF_INET, SOCK_STREAM, 0)) != -1) {
		puts("Created socket");
	} else {
		perror("Unable to open socket");
	}
	if((binding = bind(sock, (struct sockaddr *)&host, sizeof(struct sockaddr_in))) != -1) {
		puts("Bound socket");
	} else {
		perror("Unable to bind socket to host");
	}
	if(listen(sock, 10) != -1) {
		puts("Started listening");
	} else {
		perror("Unable to listen");
	}

	const char * const s = "HTTP/1.1 200\r\nContent-Type:image/svg+xml\r\nContent-Length:144\r\n\r\n<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"16\" height=\"16\"><path fill=\"#FFF\" d=\"M16,0v16H0z\"/><path fill=\"#CCC\" d=\"M0,0h8v16h8V8H0z\"/></svg>";
	size_t len = strlen(s);

	int peer_socket;
	while(1) {
		socklen_t len = sizeof(struct sockaddr_in);
		if((peer_socket = accept(sock, (struct sockaddr *)&peer, &len)) == -1) {
			fprintf(stderr, "Unable to accept connection: %s\n", strerror(errno));
		} else {
			http_request req;
			build_request(peer_socket, &req);
			printf("Method: %s\nURI:    %s\nHTTP:   %s\n", req.method, req.uri, req.http_version);

			write(peer_socket, s, len);
			close(peer_socket);
		}
	}
}

/*int read_all(int fd, char **out) {
	int length = 0;
	int capacity = BASE_SIZE;
	*out = malloc(BASE_SIZE);

	int last_len;
	while(last_len = read(fd, &out + length, BASE_SIZE)) {
		length += last_len;
		*out = realloc(&out, length + BASE_SIZE);
	}
	*out = realloc(&out, length);
	return length;
}*/

/** Reads characters from file descriptor fd until one of the matching
    characters is found.
    @return The character which broke the sequence. */
char read_until(int fd, char **dest, const char *end) {
	static string_builder builder;
	static char c;

	reset(&builder);
	while((c = next(fd)) != end) {
		add_char(&builder, c);
	}
	add_char(&builder, '\0');
	*dest = malloc(builder.length);
	memcpy(*dest, builder.text, builder.length);
}

void build_request(int fd, http_request *req) {
	static string_builder builder;

	read_until(fd, &(req->method), ' ');
	read_until(fd, &(req->uri), ' ');
	read_until(fd, &(req->http_version), '\r');
}

const char next(int fd) {
	static int last_fd = 0;
	static char buf[256];
	static int x = 0;
	static int length = 0;

	if(last_fd != fd) {
		x = length = 0;
		last_fd = fd;
	}

	if(x < length) {
		return buf[x++];
	} else {
		x = 0;
		length = read(fd, buf, 256);
		if(length == -1) {
			perror("Read error");
		}
		return buf[x++];
	}
}

const char *get_status(int code) {
	switch(code) {
		case 200: return "OK";
		case 301: return "Moved Permanently";
		case 302: return "Found";
		case 304: return "Not Modified";
		case 307: return "Temporary Redirect";
		case 308: return "Permanent Redirect";
		case 400: return "Bad Request";
		case 403: return "Forbidden";
		case 404: return "Not Found";
		case 405: return "Method Not Allowed";
		case 406: return "Not Acceptable";
		case 408: return "Request Timeout";
		case 410: return "Gone";
		case 429: return "Too Many Requests";
		case 500: return "Internal Server Error";
		case 501: return "Not Implemented";
		case 503: return "Service Unavailable";
		default:  return "Unknown";
	}
}
