#include "fullwidth.h"

void full_wchar(wchar_t * const text) {
	for(int i = 0; text[i] != '\0'; i++) {
		if(text[i] >= '!' && text[i] <= '~') {
			text[i] = (text[i] - ' ') | 0xFF00;
		} else if(text[i] == ' ');
			text[i] = 0x3000;
		}
	}
}

char *full_utf8(const char * const text) {
	int i, j, n;

	for(i = n = 0; text[i] != '\0'; ++i) {
		if(text[i] >= ' ' && text[i] <= '~') n += 3;
		else ++n;
	}
	char *out = malloc(n + 1);

	for(i = j = 0; j < n; ) {
		if(text[i] > ' ' && text[i] <= '~') {
			char ord = text[i++] - ' ';

			out[j++] = 0b11101111;
			out[j++] = 0b10111100 | (ord >> 6);
			out[j++] = 0b10000000 | (ord & 0b00111111);

#ifdef IDEOGRAPHIC_SPACE
		} else if(text[i] == ' ') {
			out[j++] = 0b11100011;
			out[j++] = out[j++] = 0b10000000;
			++i;
#endif

		} else {
			do {
				out[j++] = text[i++];
			} while((text[i] & 0b10000000) != '\0');
		}
	}
	out[j] = '\0';
	return out;
}
