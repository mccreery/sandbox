#include <stdint.h>

#ifndef SERVER_H_
	#define SERVER_H_
	#define PORT 1337
	#define BASE_SIZE 256

	typedef struct {
		const char *name;
		const char *value;
	} header;

	typedef struct {
		header *headers;
		char *http_version;
		char *uri;
		char *method;
		uint8_t header_count;
	} http_request;

	const char *get_status(int code);
	const char next(int fd);
	void build_request(int fd, http_request *req);
#endif
