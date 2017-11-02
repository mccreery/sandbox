#include <stdint.h>

#define SQUARE    0
#define SAWTOOTH  1
#define ISAWTOOTH 2
#define TRIANGLE  3
#define NOISE     4
#define SINE      5

uint8_t wave(uint8_t i, uint8_t wave_type);
void draw_wave(uint8_t wave_type, int range);

uint8_t wave(uint8_t i, uint8_t wave_type) {
	switch(wave_type) {
		case SAWTOOTH:
			return i;
		case ISAWTOOTH:
			return ~i;
		case SQUARE:
			return (i >> 7) - 1;
		case TRIANGLE:
			return (i << 1) ^ ((i >> 7) - 1);
		case NOISE:
			i ^= i >> 6;
			i ^= i << 5;
			i ^= i << 3;
			return i;
		case SINE:;
			uint8_t mask = (i >> 7) - 1;
			i &= 127;
			i = ((i * (i - 128)) >> 5) + 128;
			i ^= mask;
			return i;
		default:
			return 0;
	}
}

void draw_wave(uint8_t wave_type, int range) {
	int i, y;

	uint8_t buf[range];
	for(i = 0; i < range; i++) buf[i] = wave(i, wave_type);

	#define STEP 8
	for(y = 255; y >= 0; y -= STEP) {
		for(i = 0; i < range; i += STEP) {
			putchar((buf[i] / STEP) == (y / STEP) ? '#' : ' ');
		}
		putchar('\n');
	}
}
