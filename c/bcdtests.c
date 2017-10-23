#include <stdint.h>

int is_bcd(uint8_t *x) {
	return *x < 0xA0 && *x&0x0F < 0x0A;
}

void increment_bcd(uint8_t *x) {
	(*x)++;
	if(((*x)&0x0F) >= 10) {
		(*x) += 6;

		if((*x) >= (10<<4)) {
			(*x) -= 10<<4;
		}
	}
}

int main() {
	int i;
	uint8_t bcd = 0;
	for(i = 0; i < 256; i++, increment_bcd(&bcd)) {
		putchar('0' + (bcd >> 4));
		putchar('0' + (bcd & 0x0F));
		putchar('\n');
	}
}

static uint8_t coin_count;

void get_coin() {
	coin_count++;
	if(coin_count & 0x0F >= 0x0A) {
		coin_count += 6;

		if(coin_count >= 0xA0) {
			coin_count -= 0xA0;
		}
	}
	screen[18] = '0' + (coin_count>>4);
	screen[19] = '0' + (coin_count&0x0F);
}
