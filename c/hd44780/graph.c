#include "hd44780.h"
#include <time.h>
#include <stdio.h>

/*
0 2 4 6
1 3 5 7

x from 0 to 15
y from 0 to 15
*/
void add_data(uint8_t x) {
	static uint8_t y = 0;

	uint8_t ytrack = y;
	for(x >>= 4; ytrack < 64; ytrack += 16) {
		send_data(CGRAM | ytrack, RW_WRITE, RS_COMMAND);
		send_data((0x10 >> x - (ytrack>>4)*5), RW_WRITE, RS_DATA);
	}

	/*send_data(CGRAM | (x/5)*16, RW_WRITE, RS_COMMAND);
	int i;
	for(i = 0; i < 16; i++) {
		send_data(0, RW_WRITE, RS_DATA);
	}
	send_data(CGRAM | (x/5)*16 + (y>>4), RW_WRITE, RS_COMMAND);
	send_data(0x10 >> (x%5),             RW_WRITE, RS_DATA);*/

	y++;
	y &= 15;
}

#define SQUARE    0
#define SAWTOOTH  1
#define ISAWTOOTH 2
#define TRIANGLE  3
#define NOISE     4
#define SINE      5

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

int main() {
	int i;
	for(i = RS; i <= BUSY; i++) {
		exportp(i);
		directp(i, DIR_OUT);
	}
	setup(BYTE_INPUT | TWO_LINE, CUR_RIGHT, DISPLAY_T);

	send_data(CGRAM, RW_WRITE, RS_COMMAND);
	for(i = 0; i < 64; i++) send_data(0, RW_WRITE, RS_DATA);

	send_data(DDRAM | 0x00 & DDRAM_MASK, RW_WRITE, RS_COMMAND);
	for(i = 0; i < 8; i += 2) send_data(i, RW_WRITE, RS_DATA);
	send_data(DDRAM | 0x40 & DDRAM_MASK, RW_WRITE, RS_COMMAND);
	for(i = 1; i < 8; i += 2) send_data(i, RW_WRITE, RS_DATA);

	uint8_t wave_type = SINE, j;
	for(j = 0; 1; usleep(100000), j += 16) {
		if(!j) {
			wave_type++;
			wave_type %= 6;
			sleep(1);
		}

		add_data(wave(j, wave_type));
	}
}
