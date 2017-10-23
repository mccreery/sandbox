#include "hd44780.h"
#include "input.h"
#include <stdint.h>
#include <stdio.h>
#include <unistd.h>

const uint8_t *modes = "BUS DDA ";
const uint8_t coins[8] = {
	1, 2, 5, 10, 20, 50, 100, 200
};

#define SUB 0
#define ADD 1

int main() {
	struct in_state change, ok;
	change.pin = 21;
	change.repeat = 1;
	ok.pin =     20;

	int i;
	for(i = RS; i <= BUSY; i++) {
		exportp(i);
		directp(i, DIR_OUT);
	}
	exportp(20);
	directp(20, DIR_IN);
	exportp(21);
	directp(21, DIR_IN);

	uint32_t x = 0;

	setup(SETUP | BYTE_INPUT | TWO_LINE, CUR_RIGHT, DISPLAY_T);
	register_char(0x00, 0x0608081E08091600);
	send_data(ENTRY_MODE, RW_WRITE, RS_COMMAND);
	send_data(DDRAM, RW_WRITE, RS_COMMAND);
	send_data(0x00, RW_WRITE, RS_DATA);

	int stage = 5, mode = ADD, coin = 0, tens = 0, ones = 0;
	while(1) {
		usleep(100000);
		tick_input(&change);
		tick_input(&ok);
		printf("%d\n\b\r", stage);

		switch(stage) {
			case 0: // Waiting
				if(ok.state & IN_PRESS) {
					stage = 1;
				}
				break;
			case 1: // Set mode
				if(change.state & IN_PRESS) {
					mode = !mode;
					uint8_t i = mode << 2;

					send_data(DDRAM | 0x42, RW_WRITE, RS_COMMAND);
					send_data(modes[i++], RW_WRITE, RS_DATA);
					send_data(modes[i++], RW_WRITE, RS_DATA);
					send_data(modes[i], RW_WRITE, RS_DATA);
				} else if(ok.state & IN_PRESS) {
					stage = 2;
				}
				break;
			case 2: // Set coin type
				if(change.state & IN_PRESS) {
					++coin;
					coin &= 7; // Luckily there are 8 types of coin

					send_data(DDRAM | 0x4B, RW_WRITE, RS_COMMAND);
					if(coin < 100) {
						uint8_t y = coin;
						send_data('p', RW_WRITE, RS_DATA);
						send_data('0' + y - (y /= 10) * 10, RW_WRITE, RS_DATA);
						if(y) send_data('0' + y, RW_WRITE, RS_DATA);
						else send_data(' ', RW_WRITE, RS_DATA);
					} else {
						send_data('0' + coin / 100, RW_WRITE, RS_DATA);
						send_data(0x00, RW_WRITE, RS_DATA);
						send_data(' ', RW_WRITE, RS_DATA);
					}
				} else if(ok.state & IN_PRESS) {
					tens = 0;
					ones = 0;
					stage = 3;
				}
				break;
			case 3:
				if(change.state & IN_PRESS) {
					tens++;
					tens %= 10;

					send_data(DDRAM | 0x4E, RW_WRITE, RS_COMMAND);
					send_data('0' + tens, RW_WRITE, RS_DATA);
				} else if(ok.state & IN_PRESS) {
					stage = 4;
				}
			case 4:
				if(change.state & IN_PRESS) {
					ones++;
					ones %= 10;

					send_data(DDRAM | 0x4F, RW_WRITE, RS_COMMAND);
					send_data('0' + ones, RW_WRITE, RS_DATA);
				} else if(ok.state & IN_PRESS) {
					stage = 5;
				}
			case 5:
				if(mode) {
					x += coin * (tens * 10 + ones);
				} else {
					x -= coin * (tens * 10 + ones);
				}

				uint32_t y = x;
				send_data(DDRAM | 0x0F, RW_WRITE, RS_COMMAND);
				send_data('0' + y - (y /= 10) * 10, RW_WRITE, RS_DATA);
				send_data('0' + y - (y /= 10) * 10, RW_WRITE, RS_DATA);
				send_data('.', RW_WRITE, RS_DATA);
				do {
					send_data('0' + y - (y /= 10) * 10, RW_WRITE, RS_DATA);
				} while(y);

				stage = 0;
				break;
		}
	}
}
