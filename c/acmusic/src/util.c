#include <acmusic.h>
#include <stdint.h>

uint16_t read_u16le(uint8_t *buf) {
	return buf[0] | (buf[1] << 8);
}
uint32_t read_u32le(uint8_t *buf) {
	return buf[0] | (buf[1] << 8) | (buf[2] << 16) | (buf[3] << 24);
}

void write_u16le(uint8_t *buf, uint16_t x) {
	buf[0] = x & 0xFF;
	buf[1] = (x >> 8);
}
void write_u32le(uint8_t *buf, uint32_t x) {
	buf[0] = x & 0xFF;
	buf[1] = (x >> 8) & 0xFF;
	buf[2] = (x >> 16) & 0xFF;
	buf[3] = (x >> 24);
}

int id_equals(uint8_t * const buf, const char * const name) {
	for(int i = 0; i < 4; i++) {
		if(buf[i] != name[i])
			return 0;
	}
	return 1;
}
