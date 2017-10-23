#include "gpio.h"
#include "hd44780.h"
#include <time.h>

void send_no(int val);
int get_pin(char *arg);
const int locs[6] = {
	DDRAM | 0x03, DDRAM | 0x09, DDRAM | 0x0F,
	DDRAM | 0x43, DDRAM | 0x49, DDRAM | 0x4F
};

const uint8_t loading[32] = {
	0x00, 0x04, 0x04, 0x04, 0x04, 0x04, 0x00, 0x00,
	0x00, 0x01, 0x02, 0x04, 0x08, 0x10, 0x00, 0x00,
	0x00, 0x00, 0x00, 0x1F, 0x00, 0x00, 0x00, 0x00,
	0x00, 0x10, 0x08, 0x04, 0x02, 0x01, 0x00, 0x00
};

int main(int argc, char **argv) {
	--argc; ++argv;
	if(argc > 6) argc = 6;

	int i;

	int pins[argc];
	for(i = 0; i < argc; i++) {
		pins[i] = get_pin(argv[i]);
		exportp(pins[i]);
		directp(pins[i], DIR_IN);
	}
	for(i = RS; i <= BUSY; i++) {
		exportp(i);
		directp(i, DIR_OUT);
	}

	send_data(SETUP | BYTE_INPUT | TWO_LINE,
	                               RW_WRITE, RS_COMMAND);
	send_data(CLEAR,               RW_WRITE, RS_COMMAND);
	send_data(ENTRY_MODE,          RW_WRITE, RS_COMMAND);
	send_data(DISPLAY | DISPLAY_T, RW_WRITE, RS_COMMAND);

	send_data(CGRAM | 31, RW_WRITE, RS_COMMAND);
	for(i = 31; i >= 0; i--) {
		send_data(loading[i], RW_WRITE, RS_DATA);
	}

	for(i = 0; i < argc; i++) {
		send_data(locs[i] - 1, RW_WRITE, RS_COMMAND);
		send_data(':', RW_WRITE, RS_DATA);
		send_no(pins[i]);
	}

	int j;
	const struct timespec t = {0, 250000000};
	for(j = 0; 1; nanosleep(&t, NULL), j++) {
		for(i = 0; i < argc; i++) {
			send_data(locs[i], RW_WRITE, RS_COMMAND);
			send_data('0' + readp(pins[i]), RW_WRITE, RS_DATA);
		}
		send_data(DDRAM | 0x4F, RW_WRITE, RS_COMMAND);
		send_data(j&3, RW_WRITE, RS_DATA);
	}
}

void send_no(int val) {
	int rem = val;
	val /= 10;
	rem -= val * 10;
	send_data('0' + rem, RW_WRITE, RS_DATA);
	send_data('0' + val, RW_WRITE, RS_DATA);
}

int get_pin(char *arg) {
	int val;
	for(val = 0; *arg; val *= 10, val += *arg - '0', arg++);
	return val;
}
