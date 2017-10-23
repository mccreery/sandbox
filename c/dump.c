#include <stdint.h>
#include <stdio.h>
#include <errno.h>
#include <string.h>
#include <stdlib.h>
#include <time.h>

char *generate_name(const char * const path);
char filter_char(char x);
char hex_digit(int x);

int main(int argc, char** argv) {
	if(argc < 2) {
		fprintf(stderr, "Usage: %s input [output]\n", argv[0]);
		return 1;
	}

	FILE *in, *out;
	if((in = fopen(argv[1], "rb")) == NULL) {
		fprintf(stderr, "Error attempting to open input file \"%s\": %s\n", argv[1], strerror(errno));
		return 1;
	}
	if(argc == 2 || argv[2][0] == '-' && argv[2][1] == '\0') {
		out = stdout;
	} else if((out = fopen(argv[2], "wb")) == NULL) {
		fprintf(stderr, "Error attempting to open output file \"%s\": %s\n", argv[1], strerror(errno));
		return 1;
	}

	char * const name = generate_name(argv[1]);
	fprintf(out, "const unsigned char %s[] = {\n\t", name);

	size_t i = 0;
	/* Not using printf to keep a tight loop */
	for(int c; (c = fgetc(in)) != EOF; i++) {
		if(i != 0) {
			if((i & 7) != 0) {
				fputs(", 0x", out);
			} else {
				fputs(",\n\t0x", out);
			}
		} else {
			fputs("0x", out);
		}
		fputc(hex_digit(c >> 4), out);
		fputc(hex_digit(c     ), out);
	}
	fclose(in);

	fprintf(out, "\n}\n\nconst unsigned long long %s_LENGTH = %u;\n", name, i);
	free(name);
	fclose(out);
}

char *generate_name(const char * const path) {
	size_t start = 0, end = 0;

	size_t i;
	for(i = 0; path[i] != '\0'; i++) {
		if(path[i] == '\\' || path[i] == '/') {
			start = i + 1;
		} else if(path[i] == '.') {
			end = i;
		}
	}
	if(end <= start) end = i;

	char first = filter_char(path[start]);
	char *name;
	i = 0;

	if(first < 'A' || first > 'Z' && first != '_') {
		/* Invalid C variable name; prepend an underscore */
		name = malloc(end - start + 2);
		name[i++] = '_';
	} else {
		name = malloc(end - start + 1);
	}
	name[i++] = first;

	for(size_t j = start + 1; j < end; i++, j++) {
		name[i] = filter_char(path[j]);
	}
	name[i] = '\0';
	return name;
}

char filter_char(char x) {
	if(x >= 'a' && x <= 'z') {
		x &= ~0x20;
	} else if(x < 'A' || x > 'z' && x < '0' || x > '9') {
		x = '_';
	}
	return x;
}

char hex_digit(int x) {
	x &= 0x0F;

	if(x > 9) {
		x += ('A' - 10);
	} else {
		x += '0';
	}
	return x;
}
