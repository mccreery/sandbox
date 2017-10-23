#include "gpio.h"
#include "hd44780.h"
#include "glyphs.h"
#include "clock.h"
#include <unistd.h>
#include <stdio.h>
#include <time.h>

int main() {
	int i;
	for(i = RS; i <= BUSY; i++) {
		exportp(i);
		directp(i, DIR_OUT);
	}
	setup();

	send_data(DDRAM | 0x0C & DDRAM_MASK, RW_WRITE, RS_COMMAND);
	timef(0, 1);
	send_data(' ',  RW_WRITE, RS_DATA);
	send_data(0x00, RW_WRITE, RS_DATA);

	time_t last, next;
	time(&last);

	while(1) {
		for(time(&next); next > last; last += 1) {
			send_data(DDRAM | 0x0C & DDRAM_MASK, RW_WRITE, RS_COMMAND);
			timef((last + 3600) % (60 * 60 * 24), 1);
		}
	}

	for(i = RS; i <= BUSY; i++) {
		unexportp(i);
	}
}

void setup() {
	send_data(SETUP | BYTE_INPUT | TWO_LINE, RW_WRITE, RS_COMMAND);
	send_data(CLEAR,                         RW_WRITE, RS_COMMAND);
	register_char(0x00, GLYPH_CLOCK);
	send_data(ENTRY_MODE,                    RW_WRITE, RS_COMMAND);
	send_data(DISPLAY | DISPLAY_T,           RW_WRITE, RS_COMMAND);
}

void timef(unsigned int time, int seconds) {
	int colon_flash = time & 1 ? ':' : ' ';

	if(seconds) {
		send_data('0' + time - (time /= 10) * 10, RW_WRITE, RS_DATA);
		send_data('0' + time - (time /= 6) * 6,   RW_WRITE, RS_DATA);
		send_data(colon_flash,                    RW_WRITE, RS_DATA);
	} else {
		time /= 60;
	}

	send_data('0' + time - (time /= 10) * 10, RW_WRITE, RS_DATA);
	send_data('0' + time - (time /= 6) * 6,   RW_WRITE, RS_DATA);
	send_data(colon_flash,                    RW_WRITE, RS_DATA);
	send_data('0' + time - (time /= 10) * 10, RW_WRITE, RS_DATA);
	send_data('0' + time,                     RW_WRITE, RS_DATA);
}
