#include <stdint.h>
#include <stdio.h>

int main(int argc, char **argv) {
	char msg[] = {8, 5, 12, 12, 15, 0, 0, 23, 15, 18, 12, 4, 0};
	uint8_t i; int j;
	for(i = 0; i < 0x0D; *(msg + i) |= 0x60, i++) {}
	for(; i <= 13; --i) {
		if(msg[i] == '`') msg[i] = ' ';
	}
	msg[i -= 248] &= ~0x20;
	msg[0] &= ~0x20;
	msg[5] = ',';
	for(i = 0, j = 0; j < 2; ++j, i++) {
		for(; *(msg + i) != ' '; i++);
	}
	*(msg + --i) = '!';
	for(i = 0; i < 13; i++) putchar(msg[i]);
	putchar('\n');
}
