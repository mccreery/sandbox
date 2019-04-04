#ifndef IOUTILS_H_
#define IOUTILS_H_

#include <stddef.h>
#include <cstdint>
#include <istream>

size_t readRaw(char *&dest, const char * const path, bool terminate = false);
uint16_t read16(std::istream &in);
uint32_t read32(std::istream &in);

#endif
