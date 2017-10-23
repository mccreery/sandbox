#include <stdio.h>
int convert(const char * const in_str, char * const out_str);

int main(int argc, char **argv) {
	char out[10];

	for(int i = 1; i < argc; i++) {
		if(!convert(argv[i], out)) {
			puts(out);
		}
	}
}

int convert(const char * const in_str, char * const out_str) {
	int i, o, expected;

	if(in_str[0] >= '0' && in_str[0] <= '9') { // Convert to string
		expected = 3;

		for(o = i = 0; i < 3 && in_str[i] >= '0' && in_str[i] <= '9'; i++) {
			char in = in_str[i];

			for(int bit = 0; bit < 3; bit++) {
				out_str[o++] = in & 4 ? "rwx"[bit] : '-';
				in <<= 1;
			}
		}
	} else { // Convert to octal
		expected = 9;

		for(o = i = 0; i < 9 && o < 3; o++) {
			out_str[o] = 0;

			for(int bit = 0; bit < 3; i++, bit++) {
				if(!in_str[i]) {o = 3; break;}
				out_str[o] <<= 1;
				out_str[o] |= in_str[i] != '-';
			}
			out_str[o] += '0';
		}
	}

	if(i == expected) {
		out_str[o] = '\0';
		return 0;
	} else {
		if(!in_str[i]) {
			fprintf(stderr, "String \"%s\" too short: expected %d characters\n", in_str, expected);
		} else {
			fprintf(stderr, "Invalid digit in \"%s\" at index %d: \"%c\"\n", in_str, i, in_str[i]);
		}
		return 1;
	}
}
