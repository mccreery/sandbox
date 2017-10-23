#include "gpio.h"
#include "hd44780.h"
#include "glyphs.h"
#include <stdint.h>
#include <time.h>

int main() {
	int j;
	send_data(SETUP | BYTE_INPUT | TWO_LINE, RW_WRITE, RS_COMMAND);
	send_data(ENTRY_MODE | CUR_RIGHT,        RW_WRITE, RS_COMMAND);
	send_data(CLEAR,                         RW_WRITE, RS_COMMAND);
	send_data(DISPLAY | DISPLAY_T,           RW_WRITE, RS_COMMAND);

	const uint64_t glyphs[] = {
		GLYPH_CLOCK, GLYPH_HEART, GLYPH_STAR, GLYPH_PIN,
		GLYPH_LIKE,  GLYPH_NOTE,  GLYPH_BELL, GLYPH_BOOK
	};
	for(j = 0; j < 8; j++) register_char(j, glyphs[j]);

	uint8_t i = 0;
	while(1) {
		send_data(DDRAM | 0x00 & DDRAM_MASK, RW_WRITE, RS_COMMAND);
		for(j = 0; j < 16; j++) {
			send_data(i++, RW_WRITE, RS_DATA);
		}
		sleep(1);
	}
}
