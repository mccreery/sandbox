#include <stdint.h>
#include <time.h>
#include <stdio.h>
#include "gpio.h"
#include "hd44780.h"

#define LARGE0 0x03070F1F1F1F1F1F
#define LARGE1 0x1F1F1F0000001F1F
#define LARGE2 0x181C1E1F1F1F1F1F
#define LARGE3 0x1F1F1F0000000000
#define LARGE4 0x1F1F1F1F1F0F0703
#define LARGE5 0x00000000001F1F1F
#define LARGE6 0x1F1F1F1F1F1E1C18
#define LARGE7 0x1C141C070D0C0D07

const uint8_t large_chars[6*10] = {
	0x00, 0x03, 0x02, 0x04, 0x05, 0x06,
	' ',  0x00, ' ',  ' ',  0x06, ' ',
	0x03, 0x01, 0x02, 0x00, 0x05, 0x05,
	0x03, 0x01, 0x02, 0x05, 0x05, 0x06,
	0x00, 0x05, 0x02, ' ',  ' ',  0x06,
	0x00, 0x01, 0x03, 0x05, 0x05, 0x02,
	0x00, 0x01, ' ',  0x04, 0x05, 0x06,
	0x03, 0x03, 0x02, ' ',  ' ',  0x06,
	0x00, 0x01, 0x02, 0x04, 0x05, 0x06,
	0x00, 0x01, 0x02, ' ',  ' ',  0x06
};

void draw_large(uint8_t c, uint8_t x) {
	int i;
	send_data(DDRAM | x, RW_WRITE, RS_COMMAND);
	c = (c - '0') * 6;

	for(i = 0; i < 3; i++) {
		send_data(large_chars[c++], RW_WRITE, RS_DATA);
	}
	send_data(DDRAM | x | 0x40, RW_WRITE, RS_COMMAND);
	for(; i < 6; i++) {
		send_data(large_chars[c++], RW_WRITE, RS_DATA);
	}
}

int main() {
	int i;
	for(i = RS; i <= BUSY; i++) {
		exportp(i);
		directp(i, DIR_OUT);
	}

	setup(BYTE_INPUT | TWO_LINE, CUR_RIGHT, DISPLAY_T);
	register_char(0x00, LARGE0);
	register_char(0x01, LARGE1);
	register_char(0x02, LARGE2);
	register_char(0x03, LARGE3);
	register_char(0x04, LARGE4);
	register_char(0x05, LARGE5);
	register_char(0x06, LARGE6);
	register_char(0x07, LARGE7);

	send_data(DDRAM | 0x0F, RW_WRITE, RS_COMMAND);
	send_data(0x07, RW_WRITE, RS_DATA);
	send_data(DDRAM | 0x4B, RW_WRITE, RS_COMMAND);
	send_data('.', RW_WRITE, RS_DATA);

	uint32_t temp;
	while(1) {
		sleep(1);
		FILE *handle = fopen("/sys/class/thermal/thermal_zone0/temp", "r");
		if(handle == NULL) continue;
		fscanf(handle, "%u", &temp);
		fclose(handle);

		temp /= 100;
		draw_large('0' + temp - (temp /= 10) * 10, 0x0C);
		draw_large('0' + temp - (temp /= 10) * 10, 0x08);
		draw_large('0' + temp - (temp /= 10) * 10, 0x04);
	}
}
