#include "gpio.h"
#include "hd44780.h"
#include <stdint.h>
#include <unistd.h>
#include <stdio.h>

void block() {
	directp(BUSY, DIR_IN);

	writep(RS, 0);
	writep(RW, 1);

	writep(ENABLE, 1);
	while(readp(BUSY)) usleep(1);
	writep(ENABLE, 0);

	writep(RS, 0);
	writep(RW, 0);

	directp(BUSY, DIR_OUT);
}

void write_data(uint8_t val) {
	int i;
	for(i = 0; i < 8; i++) {
		writep(DATA0 + i, (val >> i) & 1);
	}
}

void pulse() {
	writep(ENABLE, 1);
	usleep(1);
	writep(ENABLE, 0);
}

void send_data(uint8_t c, int rw, int rs) {
	block();
	writep(RW, rw);
	writep(RS, rs);
	write_data(c);
	pulse();
}

void register_char(uint8_t c, uint64_t data) {
	send_data(CGRAM | (c*8) & CGRAM_MASK, 0, 0);
	int i;
	for(i = 0; i < 8; i++) {
		send_data((data >> ((7 - i) * 8)) & 0xFF, 0, 1);
	}
}

void setup(uint8_t setup, uint8_t entry_mode, uint8_t display) {
	send_data(SETUP | setup, RW_WRITE, RS_COMMAND);
	send_data(CLEAR,                         RW_WRITE, RS_COMMAND);
	send_data(ENTRY_MODE | entry_mode,                    RW_WRITE, RS_COMMAND);
	send_data(DISPLAY | display,           RW_WRITE, RS_COMMAND);
}
