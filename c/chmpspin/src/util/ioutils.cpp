#include <ioutils.hpp>
#include <stddef.h>
#include <winsock2.h>
#include <cstdint>
#include <fstream>

size_t readRaw(char *&dest, const char * const path, bool terminate) {
	std::ifstream file(path, std::ios::binary | std::ios::ate);
	const size_t size = file.tellg();
	file.seekg(0, std::ios::beg);

	if(terminate) dest = new char[size + 1];
	else dest = new char[size];

	file.read(dest, size);
	if(terminate) dest[size] = '\0';

	return size;
}

uint16_t read16(std::istream &in) {
	uint16_t x;
	in.read(reinterpret_cast<char *>(&x), 2);
	return ntohs(x);
}

uint32_t read32(std::istream &in) {
	uint32_t x;
	in.read(reinterpret_cast<char *>(&x), 4);
	return ntohl(x);
}
