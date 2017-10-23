#include "hd44780.h"
#include <time.h>
#include <stdint.h>

void register_bars() {
	send_data(CGRAM, RW_WRITE, RS_COMMAND);
	int i;
	for(i = 0; i < 64; i++) {
		send_data(((7-(i&7)) > (i>>3)) - 1, RW_WRITE, RS_DATA);
	}
}

void draw_bar(int x, uint8_t y) {
	y >>= 4;

	send_data(DDRAM | (0x40 + x) & DDRAM_MASK, RW_WRITE, RS_COMMAND);
	send_data(y>=8 ? 7 : y,                    RW_WRITE, RS_DATA);

	send_data(DDRAM | x & DDRAM_MASK,          RW_WRITE, RS_COMMAND);
	send_data(y>=8 ? y-8 : ' ',                   RW_WRITE, RS_DATA);
}

int main() {
	int i;
	for(i = RS; i <= BUSY; i++) {
		exportp(i);
		directp(i, DIR_OUT);
	}
	setup(BYTE_INPUT | TWO_LINE, CUR_RIGHT, DISPLAY_T);

	register_bars();
	send_data(DDRAM | 0 & DDRAM_MASK, RW_WRITE, RS_COMMAND);
	for(i = 0; i < 16; i++) {
		draw_bar(i, 16 * i);
	}
}
