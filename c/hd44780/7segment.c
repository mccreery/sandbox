#include <stdint.h>
#include <stdio.h>
#include <time.h>
#include <string.h>
#include "gpio.h"

const uint8_t *digits = "\xFC\x60\xDA\xF2\x66\xB6\xBE\xE0\xFE\xF6\xEE\x3E\x9C\x7A\x9E\x8E";
#define PIN0 5

void write_data(uint8_t val) {
	int i;
	for(i = 0; i < 8; i++) {
		writep(PIN0 + i, (val >> (7 - i)) & 1);
	}
	writep(PIN0 + 8, !(val&1));
}

void outputDigit(uint8_t n, uint8_t msb) {
	write_data(digits[n] | msb);
}

uint8_t buf[256];
uint16_t current = 0;
uint16_t end = 0;

void push(uint8_t n) {
	buf[end >> 8] = n;
	end += 256;
}

#define PERIOD (CLOCKS_PER_SEC / 128)
void update() {
	static time_t lastTime = 0;
	if(lastTime == 0) lastTime = clock();

	time_t nextTime;
	for(nextTime = clock(); nextTime > lastTime + PERIOD; lastTime += PERIOD) {
		//if(current != end) {
			if(current & 1) {
				outputDigit(buf[current >> 8] >> 4, 1);
			} else {
				outputDigit(buf[current >> 8] & 0xF, 0);
			}
			current++;
		//}
	}
}

int main(int argc, char **argv) {
	memset(buf, 0, 256);

	int i;
	for(i = PIN0; i < PIN0 + 9; i++) {
		exportp(i);
		directp(i, DIR_OUT);
	}

	for(i = 0; i < 256; i++) push(i);
	while(1) {
		update();
	}
}
